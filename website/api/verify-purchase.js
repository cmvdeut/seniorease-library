// /api/verify-purchase.js
const Stripe = require("stripe");

const stripe = new Stripe(process.env.STRIPE_SECRET_KEY, {
  apiVersion: "2024-06-20",
});

// Require STRIPE_PRODUCT_ID - no fallback
const PRODUCT_ID = process.env.STRIPE_PRODUCT_ID;
if (!PRODUCT_ID) {
  throw new Error("Missing STRIPE_PRODUCT_ID environment variable");
}

// Simple in-memory rate limit (per instance)
const rateMap = new Map();
function rateLimit(ip) {
  const now = Date.now();
  const windowMs = 60 * 1000;
  const limit = 20;
  const key = ip || "unknown";
  const entry = rateMap.get(key) || { count: 0, resetAt: now + windowMs };
  if (now > entry.resetAt) {
    entry.count = 0;
    entry.resetAt = now + windowMs;
  }
  entry.count += 1;
  rateMap.set(key, entry);
  return entry.count <= limit;
}

function isValidEmail(email) {
  if (typeof email !== "string") return false;
  const e = email.trim();
  if (e.length < 6 || e.length > 254) return false;
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(e);
}

async function readJsonBody(req) {
  // Vercel may give req.body as object OR string depending on setup
  if (req.body && typeof req.body === "object") return req.body;
  if (typeof req.body === "string") {
    try {
      return JSON.parse(req.body);
    } catch {
      return null;
    }
  }
  return null;
}

module.exports = async (req, res) => {
  // CORS
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
  res.setHeader("Access-Control-Allow-Headers", "Content-Type");

  if (req.method === "OPTIONS") return res.status(200).end();
  if (req.method !== "POST") {
    res.setHeader("Allow", "POST, OPTIONS");
    return res.status(405).json({ paid: false });
  }

  const ip =
    req.headers["x-forwarded-for"]?.toString().split(",")[0]?.trim() ||
    req.socket?.remoteAddress;

  if (!rateLimit(ip)) {
    return res.status(429).json({ paid: false });
  }

  try {
    const body = await readJsonBody(req);
    const email = body?.email;

    if (!isValidEmail(email)) {
      return res.status(400).json({ paid: false });
    }

    const normalizedEmail = email.trim().toLowerCase();

    const matchesEmail = (session) => {
      const sessionEmail =
        session?.customer_details?.email || session?.customer_email || "";
      return sessionEmail.trim().toLowerCase() === normalizedEmail;
    };

    const markPaymentIntentUsed = async (paymentIntentId) => {
      try {
        const intent = await stripe.paymentIntents.retrieve(paymentIntentId);
        const used = intent?.metadata?.download_used === "true";
        if (used) return false;
        const nextMetadata = {
          ...intent.metadata,
          download_used: "true",
          download_used_at: new Date().toISOString(),
        };
        await stripe.paymentIntents.update(paymentIntentId, {
          metadata: nextMetadata,
        });
        return true;
      } catch {
        return false;
      }
    };

    const checkSessionsForProduct = async (sessions) => {
      for (const session of sessions) {
        if (!matchesEmail(session)) continue;
        if (session?.payment_status !== "paid") continue;
        try {
          const lineItems = await stripe.checkout.sessions.listLineItems(session.id, {
            limit: 100,
            expand: ["data.price"],
          });
          for (const item of lineItems.data) {
            if (item.price?.product === PRODUCT_ID) {
              const paymentIntentId =
                typeof session.payment_intent === "string"
                  ? session.payment_intent
                  : session.payment_intent?.id;
              if (!paymentIntentId) {
                return true;
              }
              const allowed = await markPaymentIntentUsed(paymentIntentId);
              if (allowed) return true;
            }
          }
        } catch (lineItemsError) {
          continue;
        }
      }
      return false;
    };

    try {
      // 1) Primary search: paid + exact email in Stripe search
      const emailQuery = `payment_status:'paid' AND customer_details.email:'${normalizedEmail}'`;
      const searchResult = await stripe.checkout.sessions.search({
        query: emailQuery,
        limit: 100,
      });

      if (await checkSessionsForProduct(searchResult.data || [])) {
        return res.status(200).json({ paid: true });
      }
    } catch (searchError) {
      // Continue to fallback search
    }

    try {
      // 2) Fallback: paid only, then match email locally (case-insensitive)
      const paidOnlyResult = await stripe.checkout.sessions.search({
        query: "payment_status:'paid'",
        limit: 100,
      });

      if (await checkSessionsForProduct(paidOnlyResult.data || [])) {
        return res.status(200).json({ paid: true });
      }
    } catch (fallbackError) {
      // Handle search API errors silently
    }

    try {
      // 3) Fallback: list recent sessions (search may not index immediately)
      const listResult = await stripe.checkout.sessions.list({
        limit: 100,
      });

      if (await checkSessionsForProduct(listResult.data || [])) {
        return res.status(200).json({ paid: true });
      }
    } catch (listError) {
      // Handle list API errors silently
    }

    return res.status(200).json({ paid: false });
  } catch (err) {
    // Minimal error logging for production
    console.error("verify-purchase error:", err.message);
    return res.status(200).json({ paid: false });
  }
};
