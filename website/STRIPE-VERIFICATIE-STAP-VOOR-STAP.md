# 🔍 Stripe Verificatie - Stap voor Stap

## ⚠️ Belangrijk: Wat We Nodig Hebben

**NIET nodig:**
- ❌ Payment Method ID (`pm_...`) - Dit is niet relevant
- ❌ Payment Intent ID (`pi_...`) - Dit is niet relevant

**WEL nodig:**
- ✅ **Price ID:** `price_1So2hP3GmccxYlyt6rNoyUxz` - Dit is wat we zoeken
- ✅ **Checkout Session** met deze Price ID
- ✅ **Email adres** dat exact overeenkomt

---

## 🔍 Stap 1: Check Stripe Dashboard - Checkout Sessions

**In Stripe Dashboard (Test Mode):**

1. **Ga naar:** https://dashboard.stripe.com/test/checkout_sessions
2. **Zoek naar sessies met:**
   - **Payment Status:** `paid`
   - **Customer Email:** `cmvdeut@gmail.com` (of het email dat je gebruikt)

3. **Voor elke gevonden sessie:**
   - Klik op de sessie
   - Scroll naar **Line Items**
   - Check de **Price ID** in de line items
   - **Moet zijn:** `price_1So2hP3GmccxYlyt6rNoyUxz`

---

## 🔍 Stap 2: Check Payment Link

**Check of de Payment Link de juiste Price ID gebruikt:**

1. **Ga naar:** https://dashboard.stripe.com/test/payment-links
2. **Zoek link:** `9B6fZa8SW31K0BNcge6c002`
3. **Check:**
   - Status: **Active**
   - Price ID: Moet `price_1So2hP3GmccxYlyt6rNoyUxz` zijn

**Als Price ID niet klopt:**
- Maak nieuwe Payment Link met correcte Price ID
- Update link in app: `MainActivity.kt`

---

## 🔍 Stap 3: Test Betaling Opnieuw

**Als je geen Checkout Session vindt met de juiste Price ID:**

1. **Test de Payment Link:**
   - Open: https://buy.stripe.com/test_9B6fZa8SW31K0BNcge6c002
   - Gebruik test card: `4242 4242 4242 4242`
   - Email: `cmvdeut@gmail.com` (exact zoals je test)
   - Betaal

2. **Check Stripe Dashboard:**
   - Ga naar Checkout Sessions
   - Zoek de nieuwe sessie
   - Check Price ID in Line Items

---

## 🔍 Stap 4: Check API Code

**Verifieer dat de API de juiste Price ID gebruikt:**

1. **Check Vercel logs:**
   - `[DEBUG] PRICE_ID: price_1So2hP3GmccxYlyt6rNoyUxz`
   - Dit moet kloppen

2. **Check code:**
   - `website/api/verify-purchase.js` regel 8
   - Moet zijn: `price_1So2hP3GmccxYlyt6rNoyUxz`

---

## 🔍 Stap 5: Check Deployment

**Als API nog steeds oude error geeft:**

1. **Check Vercel deployment:**
   - Laatste deployment moet commit `218af96` of nieuwer zijn
   - Status = Production + Current

2. **Check code in deployment:**
   - Vercel → Deployments → Laatste → Source
   - Check `api/verify-purchase.js`
   - Zoek naar: `stripe.checkout.sessions.list` (niet `search`)

**Als je nog steeds `search` ziet:**
- Deployment heeft oude code
- Redeploy met cache OFF

---

## 📋 Checklist

- [ ] Checkout Session gevonden met `payment_status: paid`
- [ ] Customer email matcht exact (`cmvdeut@gmail.com`)
- [ ] Line Items bevatten Price ID: `price_1So2hP3GmccxYlyt6rNoyUxz`
- [ ] Payment Link gebruikt correcte Price ID
- [ ] API code gebruikt correcte Price ID
- [ ] Vercel deployment heeft nieuwste code (`list()` niet `search()`)

---

## 🚀 Wat Nu?

**1. Check Stripe Dashboard voor Checkout Sessions**
**2. Verifieer Price ID in Line Items**
**3. Test betaling opnieuw als nodig**
**4. Check Vercel deployment voor nieuwste code**

**De Payment Method ID die je gaf is niet relevant - we zoeken naar Checkout Sessions met de juiste Price ID!**
