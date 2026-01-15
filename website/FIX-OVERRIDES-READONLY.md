# 🔧 Fix Production Overrides (Read-Only)

## ⚠️ Probleem
- Production Overrides zijn **read-only** (niet aan te passen)
- Ze worden automatisch gegenereerd op basis van de laatste production deployment
- Je kunt ze niet direct bewerken

---

## ✅ Oplossing: Update via Nieuwe Deployment

**Production Overrides worden automatisch bijgewerkt wanneer je een nieuwe deployment maakt met de juiste Project Settings.**

### Stap 1: Check en Fix Project Settings

**In Vercel Dashboard:**

1. **Settings → General**
   - **Root Directory:** `website` ⚠️ (BELANGRIJK!)
   - Klik "Save" als je dit aanpast

2. **Settings → Framework Settings** (of **General**)
   - **Framework Preset:** `Other`
   - **Build Command Override:** UIT (OFF) - gebruik standaard
   - **Output Directory Override:** UIT (OFF) - gebruik `.`
   - **Install Command Override:** UIT (OFF)
   - Klik "Save"

### Stap 2: Check Git Settings

**Settings → Git:**
- **Git Repository:** `cmvdeut/seniorease-library`
- **Production Branch:** `master`
- **Automatic Deployments:** Enabled

### Stap 3: Trigger Nieuwe Deployment

**Optie A: Via Git Push (Aanbevolen)**

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
# Maak kleine wijziging
echo "" >> website/README.md
git add website/README.md
git commit -m "Trigger deployment with correct settings"
git push origin master
```

**Wacht 2-3 minuten** → Check Vercel Dashboard

**Optie B: Via Vercel Dashboard**

1. **Deployments** tab
2. **Klik op 3 puntjes** (⋯) naast laatste deployment
3. **Klik:** "Redeploy"
4. **Wacht 2-3 minuten**

**Optie C: Force Reconnect (Als niets werkt)**

1. **Settings → Git → Disconnect**
2. **Connect Git Repository** → Selecteer `cmvdeut/seniorease-library`
3. **Check instellingen:**
   - Root Directory = `website`
   - Framework Preset = `Other`
   - Build Command = (leeg)
   - Output Directory = `.`
4. **Klik:** "Deploy"

### Stap 4: Verifieer

**Na nieuwe deployment:**

1. **Check Production Overrides:**
   - Settings → General → Production Overrides
   - Moeten nu overeenkomen met Project Settings
   - Waarschuwing zou moeten verdwijnen

2. **Check Deployment:**
   - Deployments → Laatste deployment
   - Source → Commit hash = `7c1b0a7` of nieuwer
   - Status = Ready (Production)

3. **Test API:**
   ```powershell
   cd D:\MAUREEN\DEV\SeniorEase-Library\website
   .\test-api-email.ps1 -Email "cmvdeut@gmail.com"
   ```

4. **Check Vercel Logs:**
   - Functions tab → Zoek `[DEBUG]` logs

---

## 📋 Checklist

- [ ] Settings → General → Root Directory = `website`
- [ ] Settings → Framework → Overrides = OFF (gebruik standaard)
- [ ] Settings → Git → Production Branch = `master`
- [ ] Nieuwe deployment getriggerd (via push of redeploy)
- [ ] Production Overrides zijn automatisch bijgewerkt
- [ ] Waarschuwing is verdwenen
- [ ] Deployment commit hash = `7c1b0a7` of nieuwer
- [ ] Test API en check voor `[DEBUG]` logs

---

## 🚀 Snelle Fix

**1. Check Root Directory:**
   - Settings → General → Root Directory = `website`

**2. Trigger Deployment:**
   ```powershell
   git push origin master
   ```

**3. Wacht 2-3 minuten**

**Production Overrides worden automatisch bijgewerkt met de nieuwe deployment!**

---

## 💡 Waarom Dit Werkt

- Production Overrides zijn een **snapshot** van de laatste production deployment
- Door een nieuwe deployment te maken met correcte Project Settings, worden de Overrides automatisch bijgewerkt
- De waarschuwing verdwijnt wanneer Overrides en Settings overeenkomen

---

**Fix Project Settings → Trigger nieuwe deployment → Overrides worden automatisch bijgewerkt!**
