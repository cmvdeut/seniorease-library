# 🔑 Stripe Keys voor Vercel

## ✅ De 3 Keys die je Nodig Hebt

### 1. Publishable Key (Test)
```
pk_test_51SJDlo3GmccxYlyt69mwIhw81gVTjuYVgTAetvrtUGp9Hlww9lLoOBFjRAJVbe8X8q1rskVMUhHVdNcgJSXUUJEg00t1LEHkRO
```
**Gebruik:** Niet nodig voor backend API (alleen voor frontend Stripe.js)
**Status:** ℹ️ Niet nodig voor deze integratie

### 2. Secret Key (Test) - VOOR VERCEL
```
sk_test_YOUR_KEY_HERE
```
**Gebruik:** ✅ **BELANGRIJK** - Voeg toe aan Vercel Environment Variables
**Waar:** Vercel Dashboard → Settings → Environment Variables
**Key naam:** `STRIPE_SECRET_KEY`
**Value:** De secret key hierboven

### 3. Price ID (Test)
```
price_1So2hP3GmccxYlyt6rNoyUxz
```
**Gebruik:** ✅ Gebruikt in API code (staat al in `verify-purchase.js`)
**Waar:** `website/api/verify-purchase.js` regel 4
**Status:** ✅ Al geconfigureerd in code

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
   - **Value:** `sk_test_YOUR_KEY_HERE`
   - **Environment:** ✅ Production, ✅ Preview, ✅ Development (alle 3!)
3. Klik: **Save**

### Stap 4: Redeploy
1. Ga naar: **Deployments**
2. Klik: **Redeploy** (of 3 puntjes → Redeploy)
3. Wacht 2-3 minuten

---

## ✅ Checklist

- [ ] Secret Key toegevoegd aan Vercel
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
