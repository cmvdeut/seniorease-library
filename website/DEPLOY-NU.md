# 🚀 Deploy naar Vercel - Stap voor Stap

## ✅ Alles is Klaar voor Deployment

- ✅ API route: `website/api/verify-purchase.js`
- ✅ Stripe dependency: toegevoegd aan `package.json`
- ✅ Node 20 runtime: geconfigureerd in `vercel.json`
- ✅ Build script: werkt correct

## 🔑 Stap 1: Voeg Stripe Key toe aan Vercel

**BELANGRIJK:** Voeg de Stripe Secret Key toe voordat je deployt!

1. **Ga naar:** https://vercel.com/dashboard
2. **Selecteer je project:** `seniorease-library` (of jouw project naam)
3. **Ga naar:** **Settings** → **Environment Variables**
4. **Klik:** **Add New**
5. **Voeg toe:**
   - **Key:** `STRIPE_SECRET_KEY`
   - **Value:** `[Je Stripe Secret Key - haal op uit Stripe Dashboard]`
   - **Environment:** ✅ Production, ✅ Preview, ✅ Development (alle drie!)
6. **Klik:** **Save**

## 🚀 Stap 2: Deploy

### Optie A: Via GitHub (Aanbevolen)

Als je GitHub gebruikt:

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
git add .
git commit -m "Add API route for purchase verification"
git push
```

Vercel deployt automatisch!

### Optie B: Via Vercel Dashboard

1. **Ga naar:** Vercel Dashboard → Project
2. **Klik:** **Deployments**
3. **Klik:** **Redeploy** op de laatste deployment
4. **Of:** Push naar GitHub (als gekoppeld)

## 🧪 Stap 3: Test Na Deployment

Na deployment (2-3 minuten), test de API:

```powershell
$url = "https://www.seniorease.eu/api/verify-purchase"
$body = @{ email = "test@example.com" } | ConvertTo-Json
Invoke-RestMethod -Uri $url -Method POST -ContentType "application/json" -Body $body
```

**Verwacht:** `{ "paid": false }`

## ✅ Checklist

- [ ] Stripe Secret Key toegevoegd aan Vercel Environment Variables
- [ ] Code gepusht naar GitHub (of redeploy in Vercel)
- [ ] Deployment succesvol
- [ ] API getest op `https://www.seniorease.eu/api/verify-purchase`

---

**Voeg eerst de Stripe key toe, dan deployen!**
