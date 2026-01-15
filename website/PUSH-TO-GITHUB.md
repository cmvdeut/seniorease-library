# 🚀 Push naar GitHub - Exacte Commands

## GitHub Repository URL

**Jouw repository URL:**
```
https://github.com/cmvdeut/seniorease-library.git
```

## Stap 1: GitHub Repository Aanmaken

1. **Ga naar [github.com](https://github.com)** en log in
2. **Klik "+" → "New repository"**
3. **Vul in:**
   - **Name:** `seniorease-library`
   - **Description:** "SeniorEase Library - Android app download website"
   - **Public** of **Private** (jouw keuze)
   - ⚠️ **NIET aanvinken:** "Add a README file"
4. **Klik "Create repository"**

## Stap 2: Remote Toevoegen en Pushen

**Voer deze commands uit in PowerShell:**

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library

# Voeg GitHub remote toe
git remote add origin https://github.com/cmvdeut/seniorease-library.git

# Push naar GitHub
git push -u origin master
```

**Als je branch `main` heet in plaats van `master`:**
```powershell
git push -u origin main
```

## Stap 3: Vercel Koppelen

1. **Ga naar [vercel.com](https://vercel.com)**
2. **Klik "Add New..." → "Project"**
3. **Kies "Import Git Repository"**
4. **Autoriseer GitHub** (als nodig)
5. **Selecteer:** `cmvdeut/seniorease-library`
6. **Klik "Import"**

## Stap 4: Project Configuratie

**BELANGRIJK - Pas deze aan:**

- **Framework Preset:** `Other`
- **Root Directory:** `website` ⚠️ (Dit is cruciaal!)
- **Build Command:** (leeg)
- **Output Directory:** `.`
- **Install Command:** (leeg)

**Klik "Deploy"**

## ✅ Klaar!

Je website wordt nu gedeployed vanuit GitHub!

---

## 🔄 Toekomstige Updates

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
git add website/
git commit -m "Update website"
git push
```

Vercel deployt automatisch! 🚀
