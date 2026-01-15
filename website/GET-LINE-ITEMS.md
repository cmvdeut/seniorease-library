# 🔍 Check Line Items in Checkout Session

## ⚠️ Belangrijk

**De Checkout Session JSON bevat GEEN Line Items automatisch!**
Je moet ze apart ophalen via de Stripe API.

---

## ✅ Check Line Items in Stripe Dashboard

**In Stripe Dashboard:**

1. **Ga naar:** https://dashboard.stripe.com/test/checkout_sessions
2. **Klik op session:** `cs_test_a1JxTJovIux5sf47jxAWwH3qOVq130jORXFTf7pfJyI55DbNwXuIGBhcwW`
3. **Scroll naar beneden** naar sectie: **"Line Items"** of **"Items"**
4. **Klik op het item** om details te zien
5. **Check de Price ID**

**Of:**

1. **Klik op "View in test mode"** (rechtsboven)
2. **Zoek naar:** "Line Items" sectie
3. **Klik op het item**
4. **Check:** "Price ID" of "Price"

---

## 🔍 Belangrijke Observatie

**In de Checkout Session zie ik:**
- ✅ Email: `cmvdeut@gmail.com`
- ✅ Payment Status: `paid`
- ✅ Status: `complete`
- ⚠️ **Adaptive Pricing: `enabled: true`**

**Adaptive Pricing betekent:**
- De prijs kan dynamisch zijn
- De Price ID kan anders zijn dan verwacht
- We moeten de Line Items checken om de echte Price ID te zien

---

## 📋 Wat te Checken

**In Line Items:**
- **Price ID:** Moet `price_1So2hP3GmccxYlyt6rNoyUxz` zijn
- **Als het een andere Price ID is:** Dat is het probleem!

**Als Price ID niet klopt:**
- Check Payment Link: `plink_1So33K3GmccxYlytur192t1D`
- Deze link gebruikt mogelijk een andere Price ID
- Update Payment Link of Price ID in API

---

## 🚀 Alternatief: Via Stripe API

**Als je de Line Items niet in Dashboard ziet:**

Je kunt ze ophalen via de Stripe API met:
```javascript
const lineItems = await stripe.checkout.sessions.listLineItems(
  'cs_test_a1JxTJovIux5sf47jxAWwH3qOVq130jORXFTf7pfJyI55DbNwXuIGBhcwW'
);
```

**Dit is wat de API doet!**

---

**Check de Line Items in Stripe Dashboard om de Price ID te zien!**
