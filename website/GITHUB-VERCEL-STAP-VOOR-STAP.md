# 🚀 GitHub + Vercel Setup - Stap voor Stap

## ✅ Stap 1: GitHub Repository Aanmaken

1. **Ga naar [github.com](https://github.com)** en log in
2. **Klik op het "+" icoon (rechtsboven) → "New repository"**
3. **Vul in:**
   - **Repository name:** `seniorease-library` (of een andere naam)
   - **Description:** "SeniorEase Library - Android app download website"
   - **Visibility:** 
     - ✅ **Public** (aanbevolen - gratis)
     - Of Private (als je het privé wilt houden)
   - ⚠️ **NIET aanvinken:** "Add a README file" (we hebben al bestanden)
   - ⚠️ **NIET aanvinken:** "Add .gitignore" (we hebben er al een)
4. **Klik "Create repository"**

## ✅ Stap 2: GitHub URL Kopiëren

Na het aanmaken zie je een pagina met instructies. **Kopieer de HTTPS URL**, bijvoorbeeld:
```
https://github.com/JOUW-GEBRUIKERSNAAM/seniorease-library.git
```

## ✅ Stap 3: Remote Toevoegen en Pushen

**Open PowerShell of Command Prompt** en voer uit:

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library

# Voeg GitHub remote toe (vervang met jouw URL!)
git remote add origin https://github.com/JOUW-GEBRUIKERSNAAM/seniorease-library.git

# Push naar GitHub
git push -u origin master
```

**Let op:** 
- Als je branch `main` heet in plaats van `master`, gebruik dan `main`
- Vervang `JOUW-GEBRUIKERSNAAM` met je GitHub gebruikersnaam

## ✅ Stap 4: Vercel Koppelen aan GitHub

1. **Ga naar [vercel.com](https://vercel.com)** en log in
2. **Klik "Add New..." → "Project"**
3. **Kies "Import Git Repository"**
4. **Autoriseer GitHub** (als je dat nog niet hebt gedaan)
5. **Selecteer je repository:** `seniorease-library`
6. **Klik "Import"**

## ✅ Stap 5: Project Configuratie in Vercel

**BELANGRIJK:** Pas deze instellingen aan:

- **Framework Preset:** `Other`
- **Root Directory:** `website` ⚠️ (Dit is belangrijk!)
- **Build Command:** (leeg laten)
- **Output Directory:** `.` (punt)
- **Install Command:** (leeg laten)

**Klik "Deploy"**

## ✅ Stap 6: Domein Toevoegen

1. In je Vercel project → **Settings** → **Domains**
2. Klik **"Add Domain"**
3. Voer in: `seniorease.eu`
4. Volg de DNS instructies die Vercel geeft

---

## 🎉 Klaar!

Je website is nu live op `https://seniorease.eu`!

---

## 🔄 Toekomstige Updates

Bij elke wijziging:

```bash
cd D:\MAUREEN\DEV\SeniorEase-Library
git add website/
git commit -m "Update website"
git push
```

Vercel deployt automatisch! 🚀

---

## ❓ Problemen?

**"Repository not found":**
- Controleer of je GitHub repository bestaat
- Controleer of de remote URL correct is: `git remote -v`

**"Permission denied":**
- Zorg dat je GitHub account is gekoppeld aan Vercel
- Controleer GitHub permissions in Vercel settings

**Website werkt niet:**
- Controleer of "Root Directory" op `website` staat
- Check Vercel deployment logs
