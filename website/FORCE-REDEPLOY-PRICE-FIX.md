# 🔧 Force Redeploy - Price ID Fix

## ⚠️ Probleem
- Nieuwe code met Price ID fix is gepusht
- Maar Vercel deployment heeft nog oude code
- Oude Price ID wordt nog gebruikt

---

## ✅ Oplossing: Force Redeploy

### Stap 1: Check Deployment Status

**In Vercel Dashboard:**

1. **Ga naar:** Deployments tab
2. **Check laatste deployment:**
   - Moet commit `00f9070` hebben (Fix: Update Price ID...)
   - Status moet "Ready" zijn
   - Moet "Production" + "Current" zijn

**Als deployment niet "Current" is:**
- Klik op deployment → "Promote to Production"

### Stap 2: Force Redeploy

**Als deployment wel "Current" is maar nog oude code heeft:**

1. **Vercel Dashboard → Deployments**
2. **Klik op laatste deployment** (commit `00f9070`)
3. **Klik op 3 puntjes** (⋯) → **"Redeploy"**
4. **Zet "Use existing Build Cache" = UIT (OFF)**
5. **Klik "Redeploy"**
6. **Wacht 2-3 minuten**

### Stap 3: Verifieer

**Na redeploy:**

1. **Test API:**
   ```powershell
   cd D:\MAUREEN\DEV\SeniorEase-Library\website
   .\test-api-email.ps1 -Email "cmvdeut@gmail.com"
   ```

2. **Check Vercel logs:**
   - Functions tab → Runtime Logs
   - Zoek naar: `[DEBUG] PRICE_ID: price_1So2gr3GmccxYlytQwl5mitp`
   - Moet de **nieuwe** Price ID zijn!

3. **Verwacht resultaat:**
   - `{ "paid": true }`
   - Log: `[DEBUG] ✅✅✅ MATCH FOUND!`

---

## 🔧 Alternatief: Check Environment Variable

**Als redeploy niet werkt, check Vercel Environment Variables:**

1. **Vercel Dashboard → Settings → Environment Variables**
2. **Check of `STRIPE_PRICE_ID` bestaat:**
   - Als het bestaat en de **oude** Price ID heeft → Update naar `price_1So2gr3GmccxYlytQwl5mitp`
   - Als het niet bestaat → Laat het leeg (code gebruikt fallback)
3. **Redeploy** na wijziging

---

## 📋 Checklist

- [ ] Check deployment commit = `00f9070`
- [ ] Deployment status = Ready + Production + Current
- [ ] Redeploy met cache OFF
- [ ] Wacht 2-3 minuten
- [ ] Test API en check logs
- [ ] Nieuwe Price ID in logs: `price_1So2gr3GmccxYlytQwl5mitp`
- [ ] API geeft `{ paid: true }`

---

## 🚀 Snelle Fix

**1. Vercel → Deployments → Laatste deployment**
**2. Redeploy met cache OFF**
**3. Wacht 2-3 minuten**
**4. Test API opnieuw**

**Dit zou de nieuwe Price ID naar Production moeten brengen!**
