// /api/verify-purchase.js
const Stripe = require("stripe");

const stripe = new Stripe(process.env.STRIPE_SECRET_KEY, {
  apiVersion: "2024-06-20",
});

const PRICE_ID = process.env.STRIPE_PRICE_ID || "price_1So2hP3GmccxYlyt6rNoyUxz";

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

    // Debug: Log PRICE_ID being used (from env or hardcoded)
    console.log(`[DEBUG] PRICE_ID: ${PRICE_ID} (from env: ${process.env.STRIPE_PRICE_ID ? 'yes' : 'no'})`);

    // Debug: Log the normalized email received
    console.log(`[DEBUG] Normalized email: ${normalizedEmail}`);

    // List checkout sessions - same approach as seniorease-project
    // Payment Links create Checkout Sessions with status 'complete' and payment_status 'paid'
    const checkoutSessions = await stripe.checkout.sessions.list({
      limit: 100, // Adjust based on expected volume
      status: 'complete',
    });

    let totalSessionsChecked = checkoutSessions.data.length;
    console.log(`[DEBUG] Found ${totalSessionsChecked} checkout session(s) with status 'complete'`);

    for (const session of checkoutSessions.data) {
      // Check payment status
      if (session.payment_status !== 'paid') {
        continue;
      }

      // Check if email matches (case-insensitive)
      if (
        session.customer_details?.email &&
        session.customer_details.email.toLowerCase() === normalizedEmail
      ) {
        // Debug: Log matching session
        console.log(`[DEBUG] Session from list: id=${session.id}, payment_status=${session.payment_status}, customer_email=${session.customer_details.email}`);

        // Get line items to check price ID
        try {
          const lineItems = await stripe.checkout.sessions.listLineItems(session.id, {
            limit: 100,
          });

          // Debug: Log line item price IDs
          const priceIds = lineItems.data
            .map((li) => li.price?.id)
            .filter((id) => id !== null && id !== undefined);
          console.log(`[DEBUG] Session ${session.id} - Line item price IDs: ${JSON.stringify(priceIds)}`);

          // Check if any line item has the matching price ID
          for (const item of lineItems.data) {
            if (item.price?.id === PRICE_ID) {
              console.log(`[DEBUG] Match found! Session ${session.id} has Price ID ${PRICE_ID}`);
              return res.status(200).json({ paid: true });
            }
          }
        } catch (lineItemsError) {
          // Continue searching if line items retrieval fails
          console.log(`[DEBUG] Error retrieving line items for session ${session.id}:`, lineItemsError.message);
          continue;
        }
      }
    }

    // Debug: Log final result
    console.log(`[DEBUG] No matching session found with Price ID ${PRICE_ID} for email: ${normalizedEmail}`);

    return res.status(200).json({ paid: false });
  } catch (err) {
    // Don't leak details
    console.log(`[DEBUG] Error in verify-purchase:`, err.message);
    console.log(`[DEBUG] Error stack:`, err.stack);
    return res.status(200).json({ paid: false });
  }
};
