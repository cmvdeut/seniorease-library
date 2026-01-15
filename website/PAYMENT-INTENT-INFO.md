# 💳 Payment Intent ID Info

## 📋 Wat is een Payment Intent ID?

**Payment Intent ID:** `pi_3So7Tw3GmccxYlyt0YlBKDuX`

Dit is een **Payment Intent**, niet een **Price ID**.

## 🔍 Verschil

- **Price ID:** `price_1So2hP3GmccxYlyt6rNoyUxz` - Het product/prijs dat wordt verkocht
- **Payment Intent ID:** `pi_3So7Tw3GmccxYlyt0YlBKDuX` - De betalingspoging/transactie

## 🔗 Relatie

Wanneer je betaalt via een **Payment Link**:
1. Stripe maakt een **Checkout Session** aan
2. De Checkout Session heeft een **Payment Intent**
3. De Payment Intent heeft **Line Items** met **Price IDs**

## ✅ API Zoekt Naar

De API zoekt naar:
- ✅ **Checkout Sessions** met `payment_status:'paid'` en jouw email
- ✅ **Line Items** in die sessions met **Price ID:** `price_1So2hP3GmccxYlyt6rNoyUxz`

## 🔍 Check in Stripe Dashboard

1. **Ga naar:** https://dashboard.stripe.com/test/payment_intents
2. **Zoek:** `pi_3So7Tw3GmccxYlyt0YlBKDuX`
3. **Check:**
   - Status: Moet `succeeded` zijn
   - Customer email: Moet overeenkomen
   - Checkout Session: Klik op de link naar de Checkout Session
   - In Checkout Session: Check Line Items → Price ID

## 🎯 Wat te Doen

Als de API `{ paid: false }` geeft:

1. **Check Checkout Session:**
   - Ga naar de Checkout Session die bij deze Payment Intent hoort
   - Check `customer_details.email`
   - Check Line Items → Price ID

2. **Check Email Adres:**
   - Moet exact overeenkomen (case-insensitive)
   - Check voor spaties of typfouten

3. **Check Price ID:**
   - Moet exact zijn: `price_1So2hP3GmccxYlyt6rNoyUxz`
   - Check in Line Items van de Checkout Session

## ✅ API Status

De API gebruikt nu:
- ✅ Stripe Checkout Session **search** (efficiënt)
- ✅ Zoekt op: `payment_status:'paid' AND customer_details.email:'<email>'`
- ✅ Checkt Line Items voor exacte Price ID

**Als de betaling via een Payment Link is gedaan, wordt er altijd een Checkout Session aangemaakt, dus de API zou het moeten vinden!**

---

**Check de Checkout Session die bij deze Payment Intent hoort!**
