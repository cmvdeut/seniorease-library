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

    // List checkout sessions and filter by email
    // Note: We list sessions and filter client-side since search API may not be available
    let startingAfter = null;
    let totalSessionsChecked = 0;
    let matchingSessions = [];

    // List up to 100 sessions (Stripe default limit)
    for (let page = 0; page < 10; page++) {
      const result = await stripe.checkout.sessions.list({
        limit: 100,
        ...(startingAfter ? { starting_after: startingAfter } : {}),
      });

      // Filter by payment status and email
      const filtered = result.data.filter(
        (s) =>
          s.payment_status === "paid" &&
          s.customer_details?.email?.toLowerCase() === normalizedEmail
      );

      matchingSessions = matchingSessions.concat(filtered);
      totalSessionsChecked += result.data.length;

      // Debug: Log all matching sessions
      for (const s of filtered) {
        console.log(`[DEBUG] Session from list: id=${s.id}, payment_status=${s.payment_status}, customer_email=${s.customer_details?.email || 'N/A'}`);
      }

      if (!result.has_more || result.data.length === 0) break;
      startingAfter = result.data[result.data.length - 1].id;
    }

    // Debug: Log how many sessions were checked and matched
    console.log(`[DEBUG] Checked ${totalSessionsChecked} total session(s), found ${matchingSessions.length} matching paid session(s) for email: ${normalizedEmail}`);

    // Check line items for matching sessions
    for (const s of matchingSessions) {
      try {
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
      } catch (lineItemError) {
        console.log(`[DEBUG] Error fetching line items for session ${s.id}:`, lineItemError.message);
        continue;
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
