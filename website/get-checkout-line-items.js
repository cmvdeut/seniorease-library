// Get Line Items from Checkout Session
// Run with: node get-checkout-line-items.js

const stripe = require('stripe')(process.env.STRIPE_SECRET_KEY || 'YOUR_SECRET_KEY_HERE');

const SESSION_ID = 'cs_test_a1JxTJovIux5sf47jxAWwH3qOVq130jORXFTf7pfJyI55DbNwXuIGBhcwW';

async function getLineItems() {
  try {
    console.log('🔍 Fetching Line Items for Checkout Session...');
    console.log(`Session ID: ${SESSION_ID}\n`);

    const lineItems = await stripe.checkout.sessions.listLineItems(SESSION_ID, {
      limit: 100,
    });

    console.log(`✅ Found ${lineItems.data.length} line item(s):\n`);

    lineItems.data.forEach((item, index) => {
      console.log(`--- Line Item ${index + 1} ---`);
      console.log(`Description: ${item.description || 'N/A'}`);
      console.log(`Amount: ${item.amount_total} ${item.currency}`);
      console.log(`Price ID: ${item.price?.id || 'N/A'}`);
      console.log(`Product ID: ${item.price?.product || 'N/A'}`);
      console.log('');
    });

    // Summary
    const priceIds = lineItems.data
      .map(item => item.price?.id)
      .filter(id => id !== null && id !== undefined);

    console.log('📋 Summary:');
    console.log(`Price IDs found: ${JSON.stringify(priceIds, null, 2)}`);
    console.log(`\nExpected Price ID: price_1So2hP3GmccxYlyt6rNoyUxz`);
    
    const hasExpectedPrice = priceIds.includes('price_1So2hP3GmccxYlyt6rNoyUxz');
    if (hasExpectedPrice) {
      console.log('✅ Expected Price ID found!');
    } else {
      console.log('❌ Expected Price ID NOT found!');
      console.log('⚠️  This is why the API returns { paid: false }');
    }

  } catch (error) {
    console.error('❌ Error:', error.message);
    console.error('\n💡 Make sure STRIPE_SECRET_KEY is set:');
    console.error('   export STRIPE_SECRET_KEY=sk_test_...');
    console.error('   or set it in .env file');
  }
}

getLineItems();
