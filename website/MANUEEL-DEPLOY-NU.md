# 🚀 Handmatig Deployen - Snelle Fix

## ⚠️ Probleem
- Debug logs zijn niet zichtbaar in Vercel
- Nieuwste code is niet gedeployed
- Vercel CLI geeft root directory error

---

## ✅ Oplossing: Via Vercel Dashboard

### Stap 1: Ga naar Vercel Dashboard

1. **Open:** https://vercel.com/dashboard
2. **Klik op project:** `seniorease-library`
3. **Ga naar:** **Deployments** tab

### Stap 2: Redeploy Laatste Deployment

**Optie A: Redeploy (Snelste)**

1. **Zoek de laatste deployment** (meest recente bovenaan)
2. **Klik op de 3 puntjes** (⋯) rechts naast de deployment
3. **Klik:** "Redeploy"
4. **Wacht 1-2 minuten**

**Optie B: Nieuwe Deployment Triggeren**

1. **Ga naar:** **Settings** → **Git**
2. **Check:** Is project gekoppeld aan `cmvdeut/seniorease-library`?
3. **Als niet gekoppeld:**
   - Klik "Connect Git Repository"
   - Selecteer: `cmvdeut/seniorease-library`
   - **Root Directory:** `website` ⚠️
   - Klik "Deploy"

### Stap 3: Check Root Directory Settings

**BELANGRIJK - Als Root Directory verkeerd is:**

1. **Settings** → **General**
2. **Zoek:** "Root Directory"
3. **Moet zijn:** `website` (niet `.` of leeg)
4. **Klik "Save"**
5. **Redeploy** (zie Stap 2)

---

## ✅ Test Na Deployment

**Na 1-2 minuten:**

1. **Test API:**
   ```powershell
   cd D:\MAUREEN\DEV\SeniorEase-Library\website
   .\test-api-email.ps1 -Email "cmvdeut@gmail.com"
   ```

2. **Check Vercel Logs:**
   - **Deployments** → Laatste deployment → **Functions** tab
   - Zoek naar `[DEBUG]` logs

**Als je `[DEBUG]` logs ziet:**
- ✅ Nieuwste code is gedeployed!

**Als je geen `[DEBUG]` logs ziet:**
- ❌ Oudere versie staat nog live
- Probeer opnieuw redeployen
- Check of commit `7c1b0a7` in deployment staat

---

## 🔧 Alternatief: Force Push (Als niets werkt)

**Maak een kleine wijziging om deployment te triggeren:**

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
echo "" >> website/README.md
git add website/README.md
git commit -m "Trigger deployment"
git push origin master
```

**Wacht 2-3 minuten** → Check Vercel Dashboard

---

## 📋 Checklist

- [ ] Vercel Dashboard → Deployments → Redeploy laatste deployment
- [ ] Wacht 1-2 minuten
- [ ] Test API met test script
- [ ] Check Vercel logs voor `[DEBUG]` logs
- [ ] Als nog steeds geen logs → Check Root Directory = `website`

---

**Redeploy via Vercel Dashboard is de snelste oplossing!**
