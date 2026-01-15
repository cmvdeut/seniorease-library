# 🔍 Stripe Verificatie - Stap voor Stap

## ⚠️ Belangrijk: Wat We Nodig Hebben

**NIET nodig:**
- ❌ Payment Method ID (`pm_...`) - Dit is niet relevant
- ❌ Payment Intent ID (`pi_...`) - Dit is niet relevant

**WEL nodig:**
- ✅ **Product ID:** `prod_YOUR_PRODUCT_ID` - Dit is wat we zoeken
- ✅ **Checkout Session** met dit Product ID
- ✅ **Email adres** dat exact overeenkomt

---

## 🔍 Stap 1: Check Stripe Dashboard - Checkout Sessions

**In Stripe Dashboard (Live Mode):**

1. **Ga naar:** https://dashboard.stripe.com/checkout_sessions
2. **Zoek naar sessies met:**
   - **Payment Status:** `paid`
   - **Customer Email:** `cmvdeut@gmail.com` (of het email dat je gebruikt)

3. **Voor elke gevonden sessie:**
   - Klik op de sessie
   - Scroll naar **Line Items**
   - Check het **Product ID** in de line items
   - **Moet zijn:** `prod_YOUR_PRODUCT_ID`

---

## 🔍 Stap 2: Check Payment Link

**Check of de Payment Link het juiste Product gebruikt:**

1. **Ga naar:** https://dashboard.stripe.com/payment-links
2. **Zoek de live link**
3. **Check:**
   - Status: **Active**
   - Product: Moet het juiste product zijn

**Als Product niet klopt:**
- Maak nieuwe Payment Link met het juiste product
- Update link in app: `MainActivity.kt`

---

## 🔍 Stap 3: Test Betaling Opnieuw

**Als je geen Checkout Session vindt met het juiste Product ID:**

1. **Test de Payment Link:**
   - Open: https://buy.stripe.com/aFaaEQ9X0dGogALgwu6c003
  - Doe een echte betaling (live mode)
  - Email: jouw klant email (exact)
  - Betaal

2. **Check Stripe Dashboard:**
   - Ga naar Checkout Sessions
   - Zoek de nieuwe sessie
  - Check Product ID in Line Items

---

## 🔍 Stap 4: Check API Code

**Verifieer dat de API het juiste Product ID gebruikt:**

1. **Check Vercel Environment Variables:**
  - `STRIPE_PRODUCT_ID` = `prod_YOUR_PRODUCT_ID`

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
- [ ] Line Items bevatten Product ID: `prod_YOUR_PRODUCT_ID`
- [ ] Payment Link gebruikt juiste product
- [ ] API gebruikt `STRIPE_PRODUCT_ID`
- [ ] Vercel deployment heeft nieuwste code (`list()` niet `search()`)

---

## 🚀 Wat Nu?

**1. Check Stripe Dashboard voor Checkout Sessions**
**2. Verifieer Product ID in Line Items**
**3. Test betaling opnieuw als nodig**
**4. Check Vercel deployment**

**De Payment Method ID is niet relevant - we zoeken naar Checkout Sessions met het juiste Product ID!**

