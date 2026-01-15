// /api/reset-download.js
const Stripe = require("stripe");

const stripe = new Stripe(process.env.STRIPE_SECRET_KEY, {
  apiVersion: "2024-06-20",
});

const PRODUCT_ID = process.env.STRIPE_PRODUCT_ID;
if (!PRODUCT_ID) {
  throw new Error("Missing STRIPE_PRODUCT_ID environment variable");
}

const SUPPORT_RESET_TOKEN = process.env.SUPPORT_RESET_TOKEN;
if (!SUPPORT_RESET_TOKEN) {
  throw new Error("Missing SUPPORT_RESET_TOKEN environment variable");
}

function isValidEmail(email) {
  if (typeof email !== "string") return false;
  const e = email.trim();
  if (e.length < 6 || e.length > 254) return false;
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(e);
}

async function readJsonBody(req) {
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

async function resetPaymentIntent(paymentIntentId) {
  if (!paymentIntentId) return false;
  const intent = await stripe.paymentIntents.retrieve(paymentIntentId);
  const nextMetadata = {
    ...intent.metadata,
    download_used: "false",
    download_used_at: "",
  };
  await stripe.paymentIntents.update(paymentIntentId, {
    metadata: nextMetadata,
  });
  return true;
}

module.exports = async (req, res) => {
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
  res.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Support-Token");

  if (req.method === "OPTIONS") return res.status(200).end();
  if (req.method !== "POST") {
    res.setHeader("Allow", "POST, OPTIONS");
    return res.status(405).json({ reset: false });
  }

  try {
    const headerToken = req.headers["x-support-token"];
    const body = await readJsonBody(req);
    const token = (headerToken || body?.token || "").toString();

    if (!token || token !== SUPPORT_RESET_TOKEN) {
      return res.status(401).json({ reset: false });
    }

    const paymentIntentId = body?.payment_intent || body?.paymentIntentId;
    if (paymentIntentId) {
      await resetPaymentIntent(paymentIntentId.toString());
      return res.status(200).json({ reset: true });
    }

    const email = body?.email;
    if (!isValidEmail(email)) {
      return res.status(400).json({ reset: false });
    }

    const normalizedEmail = email.trim().toLowerCase();
    const matchesEmail = (session) => {
      const sessionEmail =
        session?.customer_details?.email || session?.customer_email || "";
      return sessionEmail.trim().toLowerCase() === normalizedEmail;
    };

    const resetFromSessions = async (sessions) => {
      const ordered = [...sessions].sort(
        (a, b) => (b.created || 0) - (a.created || 0)
      );
      for (const session of ordered) {
        if (!matchesEmail(session)) continue;
        if (session?.payment_status !== "paid") continue;
        const lineItems = await stripe.checkout.sessions.listLineItems(session.id, {
          limit: 100,
          expand: ["data.price"],
        });
        const hasProduct = lineItems.data.some(
          (item) => item.price?.product === PRODUCT_ID
        );
        if (!hasProduct) continue;
        const intentId =
          typeof session.payment_intent === "string"
            ? session.payment_intent
            : session.payment_intent?.id;
        if (!intentId) continue;
        await resetPaymentIntent(intentId);
        return true;
      }
      return false;
    };

    try {
      const emailQuery = `payment_status:'paid' AND customer_details.email:'${normalizedEmail}'`;
      const searchResult = await stripe.checkout.sessions.search({
        query: emailQuery,
        limit: 100,
      });
      if (await resetFromSessions(searchResult.data || [])) {
        return res.status(200).json({ reset: true });
      }
    } catch {
      // Continue to fallback
    }

    try {
      const paidOnlyResult = await stripe.checkout.sessions.search({
        query: "payment_status:'paid'",
        limit: 100,
      });
      if (await resetFromSessions(paidOnlyResult.data || [])) {
        return res.status(200).json({ reset: true });
      }
    } catch {
      // Continue to fallback
    }

    try {
      const listResult = await stripe.checkout.sessions.list({
        limit: 100,
      });
      if (await resetFromSessions(listResult.data || [])) {
        return res.status(200).json({ reset: true });
      }
    } catch {
      // Continue to response
    }

    return res.status(200).json({ reset: false });
  } catch (err) {
    console.error("reset-download error:", err.message);
    return res.status(200).json({ reset: false });
  }
};
