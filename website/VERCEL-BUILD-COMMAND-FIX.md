# 🔧 Vercel Build Command Fix

## ✅ Huidige Instellingen (Goed):
- Root Directory: `website` ✅
- Output Directory: `.` ✅ (override is aan)
- Framework Preset: `Other` ✅

## ❌ Probleem:
- Build Command: `npm run vercel-build` or `npm run build`
- Dit probeert een build te draaien die niet nodig is voor een static site

## ✅ Oplossing:

### Stap 1: Pas Build Command Aan

1. **In Project Settings:**
   - Zoek "Build Command"
   - **Klik op "Override" toggle** (zet deze AAN - blauw)
   - **Verwijder de tekst** of zet op: `echo 'Static site - no build needed'`
   - Laat het veld leeg OF vul in: `echo 'Static site'`

2. **Klik "Save"** (onderaan de sectie)

### Stap 2: Optioneel - Install Command

Voor een static site zonder dependencies:
- **Install Command:** Laat de override UIT (grijs)
- Dit is OK, Vercel zal het overslaan als er geen package.json dependencies zijn

---

## 📋 Aanbevolen Instellingen:

- **Root Directory:** `website` ✅
- **Output Directory:** `.` ✅ (override AAN)
- **Build Command:** (leeg) of `echo 'Static site'` (override AAN)
- **Install Command:** (override UIT - standaard is OK)
- **Framework Preset:** `Other` ✅

---

## 🧪 Na Aanpassen:

1. **Klik "Save"** (onderaan Project Settings sectie)
2. **Redeploy:**
   - Ga naar Deployments
   - Klik op 3 puntjes (⋯) → "Redeploy"
   - OF: Push opnieuw naar GitHub

3. **Test:**
   - `https://seniorease-library-9hzfoov7x-cmvdeut-gmailcoms-projects.vercel.app`
   - Moet nu werken!

---

**Zet de Build Command override AAN en maak het veld leeg of zet `echo 'Static site'`!**
