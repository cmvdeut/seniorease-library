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

    // Search paid checkout sessions by customer email
    // Note: search supports many results; we page a bit, but usually the first page is enough.
    let startingAfter = null;
    let totalSessionsChecked = 0;

    for (let page = 0; page < 5; page++) {
      const result = await stripe.checkout.sessions.search({
        query: `payment_status:'paid' AND customer_details.email:'${normalizedEmail}'`,
        limit: 25,
        ...(startingAfter ? { page: { starting_after: startingAfter } } : {}),
      });

      totalSessionsChecked += result.data.length;

      for (const s of result.data) {
        // Confirm line items contain the exact price id
        const items = await stripe.checkout.sessions.listLineItems(s.id, {
          limit: 100,
        });

        // Debug: Log session ID and line item price IDs if email matches
        const priceIds = items.data
          .map((li) => li.price?.id)
          .filter((id) => id !== null && id !== undefined);
        console.log(`[DEBUG] Session ${s.id} - Line item price IDs: ${JSON.stringify(priceIds)}`);

        const hasPrice = items.data.some((li) => li.price?.id === PRICE_ID);
        if (hasPrice) {
          console.log(`[DEBUG] Match found! Session ${s.id} has Price ID ${PRICE_ID}`);
          return res.status(200).json({ paid: true });
        }
      }

      if (!result.has_more || result.data.length === 0) break;
      startingAfter = result.data[result.data.length - 1].id;
    }

    // Debug: Log how many paid sessions were checked
    console.log(`[DEBUG] Checked ${totalSessionsChecked} paid session(s) for email: ${normalizedEmail}`);
    console.log(`[DEBUG] No matching session found with Price ID ${PRICE_ID} for email: ${normalizedEmail}`);

    return res.status(200).json({ paid: false });
  } catch (err) {
    // Don't leak details
    console.log(`[DEBUG] Error in verify-purchase:`, err.message);
    return res.status(200).json({ paid: false });
  }
};
