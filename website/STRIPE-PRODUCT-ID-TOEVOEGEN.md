# 🔑 Stripe Product ID Toevoegen aan Vercel

## ✅ Product ID

```
prod_TlZqTwEVuXnHg0
```

**Gebruik:** ✅ **BELANGRIJK** - Voeg toe aan Vercel Environment Variables  
**Waar:** Vercel Dashboard → Settings → Environment Variables  
**Key naam:** `STRIPE_PRODUCT_ID`  
**Value:** `prod_TlZqTwEVuXnHg0`

---

## 📋 Stap-voor-Stap Instructies

### Stap 1: Ga naar Vercel Dashboard

1. **Open:** https://vercel.com/dashboard
2. **Log in** met je account
3. **Klik op project:** `seniorease-library` (of jouw project naam)

### Stap 2: Ga naar Environment Variables

1. **Klik:** **Settings** (bovenaan in het menu)
2. **Klik:** **Environment Variables** (in het linker menu)

### Stap 3: Voeg Product ID Toe

1. **Klik:** **Add New** (of **Add**)
2. **Vul in:**
   - **Key:** `STRIPE_PRODUCT_ID`
   - **Value:** `prod_TlZqTwEVuXnHg0`
   - **Environment:** 
     - ✅ **Production**
     - ✅ **Preview** 
     - ✅ **Development**
     - (Alle 3 aanvinken!)
3. **Klik:** **Save**

### Stap 4: Redeploy

**BELANGRIJK:** Na het toevoegen van de environment variable moet je redeployen!

1. **Ga naar:** **Deployments** (in het linker menu)
2. **Klik op de 3 puntjes** (⋯) naast de laatste deployment
3. **Klik:** **Redeploy**
4. **Wacht 2-3 minuten** tot deployment klaar is

---

## ✅ Checklist

- [ ] Product ID toegevoegd aan Vercel Environment Variables
- [ ] Key naam: `STRIPE_PRODUCT_ID`
- [ ] Value: `prod_TlZqTwEVuXnHg0`
- [ ] Alle 3 environments aangevinkt (Production, Preview, Development)
- [ ] Redeploy gedaan
- [ ] API getest

---

## 🔍 Verificatie

**Check of het werkt:**

1. **Test de API:**
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
.\test-api-email.ps1 -Email "jouw-test-email@example.com"
```

2. **Check Vercel Logs:**
   - Ga naar Vercel Dashboard → Deployments
   - Klik op de laatste deployment
   - Klik op **Functions** tab
   - Klik op `/api/verify-purchase`
   - Check de logs voor errors

3. **Als je een error ziet:**
   - `Missing STRIPE_PRODUCT_ID environment variable` → Product ID niet toegevoegd
   - `No matching session found` → Product ID klopt niet of betaling niet gevonden

---

## 📝 Belangrijk

- **Product ID is anders dan Price ID!**
  - Product ID: `prod_TlZqTwEVuXnHg0` (dit is wat je nu toevoegt)
  - Price ID: Wordt automatisch gevonden via de Product ID

- **De API gebruikt nu Product ID matching:**
  - De API zoekt naar checkout sessions met het juiste Product ID
  - Dit is betrouwbaarder dan Price ID matching

---

**Voeg de Product ID toe en redeploy!**
