// /api/verify-purchase.js
const Stripe = require("stripe");

const PRICE_ID = "price_1So2hP3GmccxYlyt6rNoyUxz"; // test price id
const stripe = new Stripe(process.env.STRIPE_SECRET_KEY, {
  apiVersion: "2024-06-20",
});

// Very small in-memory rate limit (per serverless instance)
const rateMap = new Map();
function rateLimit(ip) {
  const now = Date.now();
  const windowMs = 60 * 1000; // 1 minute
  const limit = 20; // 20 requests/min per IP (more than enough)
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

module.exports = async (req, res) => {
  // Allow only POST
  if (req.method !== "POST") {
    res.setHeader("Allow", "POST");
    return res.status(405).json({ paid: false });
  }

  // Basic CORS (only your website + app calls)
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
  res.setHeader("Access-Control-Allow-Headers", "Content-Type");
  if (req.method === "OPTIONS") return res.status(200).end();

  const ip =
    req.headers["x-forwarded-for"]?.toString().split(",")[0]?.trim() ||
    req.socket?.remoteAddress;

  if (!rateLimit(ip)) {
    return res.status(429).json({ paid: false });
  }

  try {
    const { email } = req.body || {};
    if (!isValidEmail(email)) {
      return res.status(400).json({ paid: false });
    }
    const normalizedEmail = email.trim().toLowerCase();

    // Strategy:
    // - Payment Links create Checkout Sessions.
    // - We list recent Checkout Sessions with payment_status=paid,
    //   then check customer email + line items price id.
    //
    // Note: We only need to look back a limited amount (e.g., last 100 sessions).
    const sessions = await stripe.checkout.sessions.list({
      limit: 100,
    });

    for (const s of sessions.data) {
      if (s.payment_status !== "paid") continue;

      const sessionEmail =
        (s.customer_details && s.customer_details.email
          ? s.customer_details.email
          : "")
          .toString()
          .trim()
          .toLowerCase();

      if (!sessionEmail || sessionEmail !== normalizedEmail) continue;

      // Fetch line items to confirm the exact Price ID
      const items = await stripe.checkout.sessions.listLineItems(s.id, {
        limit: 100,
      });

      const hasPrice = items.data.some((li) => li.price && li.price.id === PRICE_ID);
      if (hasPrice) {
        return res.status(200).json({ paid: true });
      }
    }

    return res.status(200).json({ paid: false });
  } catch (err) {
    // Don't leak details
    return res.status(200).json({ paid: false });
  }
};
