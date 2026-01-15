# 🔍 Check Line Items in Checkout Session

## ✅ Checkout Session Gevonden!

**Session ID:** `cs_test_a1JxTJovIux5sf47jxAWwH3qOVq130jORXFTf7pfJyI55DbNwXuIGBhcwW`
- ✅ Email: `cmvdeut@gmail.com`
- ✅ Payment Status: `paid`

**Nu moeten we de Line Items checken!**

---

## 🔍 Stap 1: Check Line Items in Stripe Dashboard

**In Stripe Dashboard (Test Mode):**

1. **Ga naar:** https://dashboard.stripe.com/test/checkout_sessions
2. **Zoek session:** `cs_test_a1JxTJovIux5sf47jxAWwH3qOVq130jORXFTf7pfJyI55DbNwXuIGBhcwW`
3. **Klik op de session**
4. **Scroll naar:** **Line Items** sectie
5. **Check de Price ID:**
   - Moet zijn: `price_1So2hP3GmccxYlyt6rNoyUxz`
   - Als het een andere Price ID is → Dat is het probleem!

---

## 🔍 Stap 2: Check Payment Link

**De session heeft Payment Link:** `plink_1So33K3GmccxYlytur192t1D`

**Check of deze link de juiste Price ID gebruikt:**

1. **Ga naar:** https://dashboard.stripe.com/test/payment-links
2. **Zoek link:** `plink_1So33K3GmccxYlytur192t1D`
3. **Check:**
   - Welke Price ID gebruikt deze link?
   - Moet zijn: `price_1So2hP3GmccxYlyt6rNoyUxz`

**Als Price ID niet klopt:**
- Deze Payment Link gebruikt een andere Price ID
- Maak nieuwe Payment Link met correcte Price ID
- Update link in app: `MainActivity.kt`

---

## 🔍 Stap 3: Check API Code

**Als Price ID klopt maar API vindt het niet:**

1. **Check Vercel deployment:**
   - Laatste deployment moet commit `218af96` of nieuwer zijn
   - Code moet `stripe.checkout.sessions.list()` gebruiken (niet `search()`)

2. **Check Vercel logs:**
   - Functions tab → Nieuwste logs
   - Moet `[DEBUG] Checked X total session(s)...` tonen
   - Geen `search is not a function` error

**Als je nog steeds `search is not a function` ziet:**
- Deployment heeft oude code
- Redeploy met cache OFF

---

## 📋 Checklist

- [ ] Checkout Session gevonden: `cs_test_a1JxTJovIux5sf47jxAWwH3qOVq130jORXFTf7pfJyI55DbNwXuIGBhcwW`
- [ ] Email matcht: `cmvdeut@gmail.com` ✅
- [ ] Payment Status: `paid` ✅
- [ ] **Line Items → Price ID = `price_1So2hP3GmccxYlyt6rNoyUxz`** ⚠️ (moet je checken!)
- [ ] Payment Link gebruikt correcte Price ID
- [ ] API code gebruikt correcte Price ID
- [ ] Vercel deployment heeft nieuwste code

---

## 🚀 Wat Nu?

**1. Check Line Items in deze Checkout Session**
**2. Verifieer Price ID = `price_1So2hP3GmccxYlyt6rNoyUxz`**
**3. Als Price ID niet klopt → Fix Payment Link**
**4. Als Price ID klopt → Check API deployment**

**De Line Items zijn cruciaal - daar staat de Price ID!**
