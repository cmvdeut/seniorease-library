import type { NextApiRequest, NextApiResponse } from 'next';
import Stripe from 'stripe';

// Initialize Stripe
const stripe = new Stripe(process.env.STRIPE_SECRET_KEY || '', {
  apiVersion: '2024-11-20.acacia',
});

// Test Price ID (hardcoded)
const TEST_PRICE_ID = 'price_1So2hP3GmccxYlyt6rNoyUxz';

// Simple in-memory rate limiting
interface RateLimitEntry {
  count: number;
  resetTime: number;
}

const rateLimitMap = new Map<string, RateLimitEntry>();
const RATE_LIMIT_WINDOW_MS = 15 * 60 * 1000; // 15 minutes
const RATE_LIMIT_MAX_REQUESTS = 10; // 10 requests per window

/**
 * Check and update rate limit for an IP address
 */
function checkRateLimit(ip: string): boolean {
  const now = Date.now();
  const entry = rateLimitMap.get(ip);

  if (!entry || now > entry.resetTime) {
    // Create new entry or reset expired entry
    rateLimitMap.set(ip, {
      count: 1,
      resetTime: now + RATE_LIMIT_WINDOW_MS,
    });
    return true;
  }

  if (entry.count >= RATE_LIMIT_MAX_REQUESTS) {
    return false; // Rate limit exceeded
  }

  // Increment count
  entry.count++;
  return true;
}

/**
 * Clean up expired rate limit entries (simple cleanup)
 */
function cleanupRateLimit() {
  const now = Date.now();
  for (const [ip, entry] of rateLimitMap.entries()) {
    if (now > entry.resetTime) {
      rateLimitMap.delete(ip);
    }
  }
}

/**
 * Validate email format
 */
function isValidEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

/**
 * Get client IP address from request
 */
function getClientIP(req: NextApiRequest): string {
  const forwarded = req.headers['x-forwarded-for'];
  const ip = forwarded
    ? (Array.isArray(forwarded) ? forwarded[0] : forwarded.split(',')[0])
    : req.socket.remoteAddress || 'unknown';
  return ip;
}

/**
 * Verify purchase endpoint
 * POST /api/verify-purchase
 */
export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse<{ paid: boolean } | { paid: boolean; error?: string }>
) {
  // Only allow POST requests
  if (req.method !== 'POST') {
    return res.status(405).json({ paid: false, error: 'Method not allowed' });
  }

  try {
    // Rate limiting
    const clientIP = getClientIP(req);
    cleanupRateLimit(); // Clean up expired entries periodically

    if (!checkRateLimit(clientIP)) {
      return res.status(429).json({
        paid: false,
        error: 'Too many requests. Please try again later.',
      });
    }

    // Validate request body
    const { email } = req.body;

    if (!email || typeof email !== 'string') {
      return res.status(400).json({
        paid: false,
        error: 'Email is required',
      });
    }

    // Validate email format
    if (!isValidEmail(email.trim())) {
      return res.status(400).json({
        paid: false,
        error: 'Invalid email format',
      });
    }

    // Check if Stripe secret key is configured
    if (!process.env.STRIPE_SECRET_KEY) {
      console.error('STRIPE_SECRET_KEY is not configured');
      return res.status(500).json({
        paid: false,
        error: 'Server configuration error',
      });
    }

    const normalizedEmail = email.trim().toLowerCase();
    let foundPayment = false;

    try {
      // Search Checkout Sessions created by Payment Links
      // Payment Links create Checkout Sessions with status 'complete' and payment_status 'paid'
      const checkoutSessions = await stripe.checkout.sessions.list({
        limit: 100, // Adjust based on expected volume
        status: 'complete',
      });

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
          // Get line items to check price ID
          try {
            const lineItems = await stripe.checkout.sessions.listLineItems(
              session.id,
              {
                limit: 100,
              }
            );

            // Check if any line item has the matching price ID
            for (const item of lineItems.data) {
              if (item.price?.id === TEST_PRICE_ID) {
                foundPayment = true;
                break;
              }
            }

            if (foundPayment) {
              break; // Found match, no need to continue searching
            }
          } catch (lineItemsError) {
            // Continue searching if line items retrieval fails
            console.error('Error retrieving line items:', lineItemsError);
            continue;
          }
        }
      }
    } catch (stripeError) {
      console.error('Stripe API error:', stripeError);
      return res.status(500).json({
        paid: false,
        error: 'Payment verification service unavailable',
      });
    }

    // Return result (no Stripe data exposed)
    return res.status(200).json({
      paid: foundPayment,
    });
  } catch (error) {
    console.error('Server error:', error);
    return res.status(500).json({
      paid: false,
      error: 'Internal server error',
    });
  }
}
