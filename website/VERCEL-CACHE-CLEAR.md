# 🔧 Vercel Cache Clear - Force Redeploy

## ⚠️ Probleem
- Code gebruikt `list()` maar logs tonen nog steeds `search is not a function` error
- Vercel heeft mogelijk oude code gecached
- Deployment heeft oude versie geladen

---

## ✅ Oplossing: Force Redeploy

### Stap 1: Via Git Push (Gedaan)

**Kleine wijziging triggeren:**
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
echo "" >> website/README.md
git add website/README.md
git commit -m "Force redeploy - clear cache"
git push origin master
```

**Wacht 2-3 minuten** → Nieuwe deployment verschijnt

### Stap 2: Via Vercel Dashboard

**Als Git push niet werkt:**

1. **Vercel Dashboard → Deployments**
2. **Klik op deployment `8e0a534`**
3. **Klik op 3 puntjes** (⋯) → **"Redeploy"**
4. **Selecteer:** "Use existing Build Cache" = **UIT** (OFF)
5. **Klik:** "Redeploy"
6. **Wacht 2-3 minuten**

### Stap 3: Verifieer

**Na nieuwe deployment:**

1. **Check deployment:**
   - Nieuwe deployment zou moeten verschijnen
   - Status = Ready (Production)
   - Commit = nieuwste

2. **Test API:**
   ```powershell
   cd D:\MAUREEN\DEV\SeniorEase-Library\website
   .\test-api-email.ps1 -Email "cmvdeut@gmail.com"
   ```

3. **Check Vercel logs:**
   - Functions tab → Nieuwste logs
   - **GEEN** `search is not a function` error
   - **WEL** `[DEBUG] Checked X total session(s)...`

---

## 🔍 Alternatief: Check Code in Deployment

**Als het nog steeds niet werkt:**

1. **Vercel Dashboard → Deployments → Laatste deployment**
2. **Klik op deployment → Source tab**
3. **Check:** `api/verify-purchase.js`
4. **Zoek naar:** `stripe.checkout.sessions.list`
5. **Als je `search` ziet:** Code is niet correct gedeployed

**Fix:**
- Disconnect en reconnect Vercel aan GitHub
- Of upload handmatig via Vercel CLI

---

## 📋 Checklist

- [ ] Force redeploy getriggerd (via Git push)
- [ ] Wacht 2-3 minuten
- [ ] Check nieuwe deployment status
- [ ] Test API en check logs
- [ ] Geen `search is not a function` error
- [ ] Zie `[DEBUG] Checked X total session(s)...` logs

---

## 🚀 Snelle Fix

**1. Force redeploy via Git push (al gedaan)**
**2. Wacht 2-3 minuten**
**3. Test API opnieuw**
**4. Check nieuwste logs**

**Als het nog steeds niet werkt:**
- Redeploy via Vercel Dashboard met cache OFF
- Of disconnect/reconnect Vercel aan GitHub

---

**Force redeploy is getriggerd - wacht 2-3 minuten en test opnieuw!**
