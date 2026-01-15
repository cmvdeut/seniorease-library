# 🔑 Stripe Keys voor Vercel

## ✅ De 2 Keys die je Nodig Hebt

### 1. Publishable Key (Live)
```
pk_live_...
```
**Gebruik:** Niet nodig voor backend API (alleen voor frontend Stripe.js)
**Status:** ℹ️ Niet nodig voor deze integratie

### 2. Secret Key (Live) - VOOR VERCEL
```
sk_live_...
```
**Gebruik:** ✅ **BELANGRIJK** - Voeg toe aan Vercel Environment Variables
**Waar:** Vercel Dashboard → Settings → Environment Variables
**Key naam:** `STRIPE_SECRET_KEY`
**Value:** De secret key hierboven

### 3. Product ID (Live)
```
prod_YOUR_PRODUCT_ID
```
**Gebruik:** ✅ Gebruikt in API code (via environment variable)
**Waar:** Vercel Environment Variables
**Key naam:** `STRIPE_PRODUCT_ID`

---

## 📋 Wat je Moet Doen in Vercel

### Stap 1: Ga naar Vercel Dashboard
1. Open: https://vercel.com/dashboard
2. Selecteer project: **seniorease-library**

### Stap 2: Ga naar Environment Variables
1. Klik: **Settings**
2. Klik: **Environment Variables**

### Stap 3: Voeg Secret Key Toe
1. Klik: **Add New**
2. Vul in:
   - **Key:** `STRIPE_SECRET_KEY`
   - **Value:** `sk_live_...`
   - **Environment:** ✅ Production, ✅ Preview, ✅ Development (alle 3!)
3. Klik: **Save**

### Stap 4: Voeg Product ID Toe
1. Klik: **Add New**
2. Vul in:
   - **Key:** `STRIPE_PRODUCT_ID`
   - **Value:** `prod_...`
   - **Environment:** ✅ Production, ✅ Preview, ✅ Development (alle 3!)
3. Klik: **Save**

### Stap 5: Redeploy
1. Ga naar: **Deployments**
2. Klik: **Redeploy** (of 3 puntjes → Redeploy)
3. Wacht 2-3 minuten

---

## ✅ Checklist

- [ ] Secret Key toegevoegd aan Vercel
- [ ] Product ID toegevoegd aan Vercel
- [ ] Alle 3 environments aangevinkt (Production, Preview, Development)
- [ ] Redeploy gedaan
- [ ] API getest

---

## 🧪 Test Na Setup

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
.\test-api-email.ps1 -Email "cmvdeut@gmail.com"
```

**Verwacht:** `{ paid: true }` of `{ paid: false }` (geen error!)

---

**BELANGRIJK: Alleen de Secret Key moet in Vercel!**
