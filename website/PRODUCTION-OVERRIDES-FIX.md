# 🔧 Production Overrides Fix

## ⚠️ Waarschuwing:
"Configuration Settings in the current Production deployment differ from your current Project Settings"

## ✅ Oplossing:

### Stap 1: Bekijk Production Overrides

1. **Klik op "Production Overrides"** (uitklappen)
2. **Noteer de instellingen:**
   - Build Command
   - Output Directory
   - Install Command
   - Root Directory

### Stap 2: Pas Project Settings Aan

1. **Klik op "Project Settings"** (uitklappen)
2. **Zet dezelfde instellingen:**
   - **Root Directory:** `website`
   - **Output Directory:** `.` (punt)
   - **Build Command:** (leeg) of `echo 'Static site'`
   - **Install Command:** (leeg)
   - **Framework Preset:** `Other` of `Static Site`

3. **Klik "Save"**

### Stap 3: Of Gebruik Production Overrides

**Alternatief:** Als de Production Overrides correct zijn, kun je die instellingen kopiëren naar Project Settings zodat ze overeenkomen.

---

## 🎯 Aanbevolen Instellingen:

- **Root Directory:** `website`
- **Output Directory:** `.` (punt)
- **Build Command:** (leeg)
- **Install Command:** (leeg)
- **Framework Preset:** `Other`

---

## 📋 Na Aanpassen:

1. **Redeploy:**
   - Ga naar Deployments
   - Klik op 3 puntjes (⋯) → "Redeploy"
   - OF: Push opnieuw naar GitHub

2. **De waarschuwing zou moeten verdwijnen** als de instellingen overeenkomen.

---

**Klik op "Production Overrides" en "Project Settings" om te zien wat het verschil is, en pas ze aan zodat ze overeenkomen!**
