# 🔧 GitHub → Vercel Niet Deployed - Fix

## ⚠️ Probleem
GitHub pushes worden niet automatisch gedeployed naar Vercel.

---

## ✅ Stap 1: Check Vercel Project Settings

1. **Ga naar [vercel.com](https://vercel.com)** en log in
2. **Klik op je project** (`seniorease-library`)
3. **Ga naar Settings → Git**

**Check deze instellingen:**

### A. Git Repository
- ✅ Moet gekoppeld zijn aan: `cmvdeut/seniorease-library`
- ❌ Als dit leeg is of verkeerd → **Klik "Connect Git Repository"** en koppel opnieuw

### B. Production Branch
- ✅ Moet staan op: `master` (of `main` als je die gebruikt)
- ❌ Als dit leeg is → **Zet op `master` en klik "Save"**

### C. Automatic Deployments
- ✅ Moet **aan** staan (enabled)
- ❌ Als dit uit staat → **Zet aan en klik "Save"**

---

## ✅ Stap 2: Check GitHub Webhook

1. **Ga naar GitHub:** [github.com/cmvdeut/seniorease-library/settings/hooks](https://github.com/cmvdeut/seniorease-library/settings/hooks)
2. **Check of er een Vercel webhook is:**
   - Moet URL bevatten: `vercel.com` of `vercel.app`
   - Status moet **Active** zijn (groen vinkje)
   - **Recent deliveries** moeten successvol zijn

**Als er geen webhook is:**
- Vercel project is niet correct gekoppeld
- Ga terug naar Stap 1 en koppel opnieuw

**Als webhook bestaat maar faalt:**
- Check de "Recent deliveries" → Klik op een delivery → Check "Response"
- Mogelijk probleem met Vercel token → Reconnect in Vercel

---

## ✅ Stap 3: Reconnect Vercel aan GitHub

### Optie A: Via Vercel Dashboard (Aanbevolen)

1. **Vercel Dashboard → Project → Settings → Git**
2. **Klik "Disconnect"** (onderaan)
3. **Klik "Connect Git Repository"**
4. **Selecteer:** `cmvdeut/seniorease-library`
5. **Klik "Import"**
6. **Check instellingen:**
   - **Root Directory:** `website` ⚠️
   - **Framework Preset:** `Other`
   - **Build Command:** (leeg)
   - **Output Directory:** `.`
7. **Klik "Deploy"**

### Optie B: Via Vercel CLI

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
vercel link
# Volg de prompts:
# - Selecteer bestaand project: seniorease-library
# - Root directory: website
```

---

## ✅ Stap 4: Test Deployment

**Push een kleine test wijziging:**

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
echo "# Test" >> website/test-deploy.md
git add website/test-deploy.md
git commit -m "Test deployment"
git push origin master
```

**Check Vercel Dashboard:**
- Ga naar **Deployments** tab
- Binnen 1-2 minuten moet er een nieuwe deployment verschijnen
- Status moet **Ready** worden (groen)

**Als er geen deployment komt:**
- Check Vercel logs voor errors
- Check GitHub webhook deliveries
- Probeer handmatig deployen (zie Stap 5)

---

## ✅ Stap 5: Handmatig Deployen (Tijdelijke Fix)

**Als automatisch deployen niet werkt, deploy handmatig:**

### Via Vercel Dashboard:
1. **Vercel Dashboard → Project → Deployments**
2. **Klik "Redeploy"** op de laatste deployment
3. Of klik **"Deploy"** → **"Import Git Repository"** → Selecteer repo

### Via Vercel CLI:
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
vercel --prod
```

---

## ✅ Stap 6: Check Vercel Logs

**Als deployment faalt:**

1. **Vercel Dashboard → Project → Deployments**
2. **Klik op de gefaalde deployment**
3. **Check "Build Logs"** voor errors:
   - ❌ "Repository not found" → GitHub koppeling is verkeerd
   - ❌ "Root directory not found" → Root Directory moet `website` zijn
   - ❌ "Build failed" → Check build logs voor specifieke errors

---

## 📋 Checklist

- [ ] Vercel project is gekoppeld aan GitHub repo
- [ ] Production Branch = `master`
- [ ] Automatic Deployments = **Enabled**
- [ ] GitHub webhook bestaat en is **Active**
- [ ] Root Directory = `website`
- [ ] Test push triggert deployment binnen 2 minuten

---

## 🔍 Veelvoorkomende Problemen

### "Repository not found"
- **Oorzaak:** Vercel heeft geen toegang tot GitHub repo
- **Fix:** Reconnect GitHub in Vercel Settings → Git

### "No deployments triggered"
- **Oorzaak:** Webhook is niet actief of faalt
- **Fix:** Check GitHub webhook status, reconnect indien nodig

### "Deployment goes to Preview instead of Production"
- **Oorzaak:** Production Branch is niet ingesteld
- **Fix:** Settings → Git → Production Branch = `master`

### "Build fails"
- **Oorzaak:** Root Directory of build config is verkeerd
- **Fix:** Check Root Directory = `website`, Build Command = (leeg)

---

## 🚀 Snelle Fix (Als Alles Faalt)

**Herstel de koppeling volledig:**

1. **Vercel → Project → Settings → Git → Disconnect**
2. **Vercel → Add New Project → Import Git Repository**
3. **Selecteer:** `cmvdeut/seniorease-library`
4. **Root Directory:** `website`
5. **Deploy**

**Dit maakt een nieuwe koppeling en zou moeten werken!**

---

**Test na fix:**
```powershell
git push origin master
# Check Vercel Dashboard binnen 2 minuten
```
