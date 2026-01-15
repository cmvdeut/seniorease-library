# 🚀 Vercel Deployment - Handmatig Triggeren

## ⚠️ Probleem
- Git push triggert Vercel niet automatisch
- Geen nieuwe deployment verschijnt

## ✅ Oplossing: Handmatig Deployen

### Stap 1: Check Vercel Dashboard

1. **Ga naar:** https://vercel.com/dashboard
2. **Klik op project:** `seniorease-library`
3. **Ga naar:** **Deployments** tab

### Stap 2: Handmatig Redeploy

**Optie A: Redeploy Laatste Deployment**

1. **Zoek de laatste deployment** (meest recente bovenaan)
2. **Klik op de 3 puntjes** (⋯) naast die deployment
3. **Kies:** "Redeploy"
4. **Wacht 2-3 minuten**

**Optie B: Check Git Settings**

1. **Ga naar:** Settings → Git
2. **Check:**
   - **Git Repository:** `cmvdeut/seniorease-library`
   - **Production Branch:** `master`
   - **Root Directory:** `website` ⚠️ (BELANGRIJK!)
   - **Automatic Deployments:** Enabled ✅

3. **Als Root Directory NIET `website` is:**
   - Klik "Edit"
   - Verander naar: `website`
   - Klik "Save"
   - Vercel redeployt automatisch

### Stap 3: Force Reconnect (Als niets werkt)

1. **Settings → Git → Disconnect**
2. **Klik:** "Connect Git Repository"
3. **Selecteer:** `cmvdeut/seniorease-library`
4. **Check instellingen:**
   - **Root Directory:** `website` ⚠️
   - **Framework Preset:** `Other`
   - **Build Command:** (leeg)
   - **Output Directory:** `.`
   - **Production Branch:** `master`
5. **Klik:** "Deploy"

### Stap 4: Verifieer Deployment

**Na deployment:**
- Check **Deployments** tab
- Nieuwe deployment zou moeten verschijnen
- Status moet "Ready" zijn (groen)
- Check commit hash in deployment details

---

## 🔍 Troubleshooting

### "No deployments found"
- Check of je in het juiste project bent
- Check of Git repository correct is gekoppeld

### "Deployment failed"
- Check **Logs** tab in deployment
- Check of `website` folder bestaat in repository
- Check of `website/api/verify-purchase.js` bestaat

### "Old code still deployed"
- Wacht 2-3 minuten (cache)
- Test in **incognito mode**
- Hard refresh: Ctrl + F5

---

**Handmatig redeployen in Vercel Dashboard!**
