// /api/verify-purchase.js
const Stripe = require("stripe");

const PRICE_ID = "price_1So2hP3GmccxYlyt6rNoyUxz"; // test price id
const stripe = new Stripe(process.env.STRIPE_SECRET_KEY, {
  apiVersion: "2024-06-20",
});

// Minimal in-memory rate limit (per serverless instance)
const rateMap = new Map();
function rateLimit(ip) {
  const now = Date.now();
  const windowMs = 60 * 1000; // 1 minute
  const limit = 20; // 20 requests/min per IP
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

  // Basic CORS
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

    // Use Stripe Checkout Session search to find paid sessions for this email
    const searchQuery = `payment_status:'paid' AND customer_details.email:'${normalizedEmail}'`;
    
    const sessions = await stripe.checkout.sessions.search({
      query: searchQuery,
      limit: 100, // Max results per search
    });

    // For each matching session, check line items for the specific Price ID
    for (const session of sessions.data) {
      try {
        const lineItems = await stripe.checkout.sessions.listLineItems(session.id, {
          limit: 100,
        });

        const hasPrice = lineItems.data.some(
          (item) => item.price && item.price.id === PRICE_ID
        );

        if (hasPrice) {
          return res.status(200).json({ paid: true });
        }
      } catch (lineItemError) {
        // Skip this session if we can't fetch line items
        continue;
      }
    }

    return res.status(200).json({ paid: false });
  } catch (err) {
    // Don't leak Stripe errors or details
    return res.status(200).json({ paid: false });
  }
};
