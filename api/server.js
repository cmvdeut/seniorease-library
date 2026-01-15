const express = require('express');
const stripe = require('stripe')(process.env.STRIPE_SECRET_KEY);
const rateLimit = require('express-rate-limit');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(express.json());

// Rate limiting: max 10 requests per 15 minutes per IP
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 minutes
  max: 10, // limit each IP to 10 requests per windowMs
  message: { error: 'Too many requests, please try again later.' },
  standardHeaders: true,
  legacyHeaders: false,
});

// Apply rate limiting to all requests
app.use(limiter);

// Test Price ID (hardcoded as requested)
const TEST_PRICE_ID = 'price_1So2hP3GmccxYlyt6rNoyUxz';

/**
 * Validate email format
 */
function isValidEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

/**
 * Verify purchase endpoint
 * POST /api/verify-purchase
 * Body: { "email": "user@example.com" }
 */
app.post('/api/verify-purchase', async (req, res) => {
  try {
    // Validate request body
    const { email } = req.body;

    if (!email) {
      return res.status(400).json({ 
        paid: false, 
        error: 'Email is required' 
      });
    }

    // Validate email format
    if (!isValidEmail(email)) {
      return res.status(400).json({ 
        paid: false, 
        error: 'Invalid email format' 
      });
    }

    // Check if Stripe secret key is configured
    if (!process.env.STRIPE_SECRET_KEY) {
      console.error('STRIPE_SECRET_KEY is not configured');
      return res.status(500).json({ 
        paid: false, 
        error: 'Server configuration error' 
      });
    }

    let foundPayment = false;

    try {
      // Method 1: Search Payment Intents
      // Payment Intents are created for successful payments
      const paymentIntents = await stripe.paymentIntents.list({
        limit: 100, // Adjust based on expected volume
      });

      for (const paymentIntent of paymentIntents.data) {
        if (paymentIntent.status === 'succeeded') {
          // Get the checkout session if it exists
          if (paymentIntent.metadata && paymentIntent.metadata.checkout_session_id) {
            try {
              const session = await stripe.checkout.sessions.retrieve(
                paymentIntent.metadata.checkout_session_id
              );

              // Check if email matches
              if (session.customer_details && 
                  session.customer_details.email && 
                  session.customer_details.email.toLowerCase() === email.toLowerCase()) {
                
                // Check if the price ID matches
                if (session.line_items) {
                  const lineItems = await stripe.checkout.sessions.listLineItems(session.id, {
                    limit: 100
                  });

                  for (const item of lineItems.data) {
                    if (item.price && item.price.id === TEST_PRICE_ID) {
                      foundPayment = true;
                      break;
                    }
                  }
                }
              }
            } catch (err) {
              // Continue searching if session retrieval fails
              continue;
            }
          }
        }
      }

      // Method 2: Search Checkout Sessions directly
      if (!foundPayment) {
        const checkoutSessions = await stripe.checkout.sessions.list({
          limit: 100,
          status: 'complete',
        });

        for (const session of checkoutSessions.data) {
          // Check if email matches
          if (session.customer_details && 
              session.customer_details.email && 
              session.customer_details.email.toLowerCase() === email.toLowerCase()) {
            
            // Check payment status
            if (session.payment_status === 'paid') {
              // Get line items to check price ID
              try {
                const lineItems = await stripe.checkout.sessions.listLineItems(session.id, {
                  limit: 100
                });

                for (const item of lineItems.data) {
                  if (item.price && item.price.id === TEST_PRICE_ID) {
                    foundPayment = true;
                    break;
                  }
                }
              } catch (err) {
                // Continue searching if line items retrieval fails
                continue;
              }
            }
          }

          if (foundPayment) break;
        }
      }

    } catch (stripeError) {
      console.error('Stripe API error:', stripeError.message);
      return res.status(500).json({ 
        paid: false, 
        error: 'Payment verification service unavailable' 
      });
    }

    // Return result (no Stripe data exposed)
    return res.json({ 
      paid: foundPayment 
    });

  } catch (error) {
    console.error('Server error:', error);
    return res.status(500).json({ 
      paid: false, 
      error: 'Internal server error' 
    });
  }
});

// Health check endpoint
app.get('/health', (req, res) => {
  res.json({ status: 'ok' });
});

// Start server
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
  console.log(`Stripe configured: ${process.env.STRIPE_SECRET_KEY ? 'Yes' : 'No'}`);
});
