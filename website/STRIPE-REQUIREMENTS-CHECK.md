# ✅ Stripe Requirements Checklist

## 🔑 Wat is Nodig voor Stripe Integratie?

### 1. Secret Key (voor Vercel Environment Variables) ✅
```
STRIPE_SECRET_KEY = [Je Stripe Secret Key - haal op uit Stripe Dashboard]
```
**Waar:** Vercel Dashboard → Settings → Environment Variables  
**Status:** ⚠️ **MOET NOG TOEGEVOEGD WORDEN**  
**Gebruikt voor:** Backend API om met Stripe te communiceren

### 2. Payment Link (voor Android App) ✅
```
https://buy.stripe.com/aFaaEQ9X0dGogALgwu6c003
```
**Waar:** `MainActivity.kt` regel 682  
**Status:** ✅ **AL IN CODE**  
**Gebruikt voor:** App opent deze link in browser voor betaling

### 3. Product ID (voor API verificatie) ✅
```
prod_YOUR_PRODUCT_ID
```
**Waar:** Vercel Environment Variables (`STRIPE_PRODUCT_ID`)  
**Status:** ✅ **IN VERCEL NODIG**  
**Gebruikt voor:** API controleert of betaling voor dit Product ID is gedaan

## ✅ Checklist

- [x] **Secret Key** - Code klaar, moet toegevoegd worden aan Vercel
- [x] **Payment Link** - Al in app code
- [x] **Product ID** - In Vercel zetten
- [ ] **Secret Key in Vercel** - ⚠️ **NOG NIET TOEGEVOEGD**

## 🎯 Antwoord op je Vraag

**Is alleen de secret key genoeg?**

**Nee, maar bijna!** Je hebt nodig:

1. ✅ **Secret Key in Vercel** (moet je nog toevoegen)
2. ✅ **Payment Link actief in Stripe** (moet je checken in Stripe Dashboard)
3. ✅ **Product ID moet kloppen** (staat in Vercel)

## 📋 Wat je Moet Doen

### Stap 1: Voeg Secret Key toe aan Vercel
- Ga naar Vercel Dashboard
- Settings → Environment Variables
- Voeg `STRIPE_SECRET_KEY` toe
- Redeploy

### Stap 2: Check Payment Link in Stripe
- Ga naar: https://dashboard.stripe.com/payment-links
- Zoek de live payment link
- Check of status **Active** is
- Check of het juiste product gekoppeld is

### Stap 3: Test
- Test de payment link in browser
- Test de API met `test-api-online.ps1`
- Test de volledige flow in de app

## ✅ Conclusie

**De secret key is het belangrijkste wat nog ontbreekt!**

Als je de secret key toevoegt aan Vercel en redeployt, zou alles moeten werken (mits de payment link actief is in Stripe).

