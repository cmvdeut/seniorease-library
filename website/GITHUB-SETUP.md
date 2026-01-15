# 🔗 GitHub Setup voor Vercel

## Stap 1: GitHub Repository Aanmaken

1. **Ga naar [github.com](https://github.com)** en log in
2. **Klik op "+" (rechtsboven) → "New repository"**
3. **Repository instellingen:**
   - **Name:** `seniorease-library` (of wat je wilt)
   - **Description:** "SeniorEase Library - Android app download website"
   - **Visibility:** Public (of Private, zoals je wilt)
   - **NIET** "Initialize with README" aanvinken (we hebben al bestanden)
4. **Klik "Create repository"**

## Stap 2: GitHub Remote Toevoegen

Na het aanmaken van de repository geeft GitHub je een URL, bijvoorbeeld:
- `https://github.com/JOUW-GEBRUIKERSNAAM/seniorease-library.git`
- Of: `git@github.com:JOUW-GEBRUIKERSNAAM/seniorease-library.git`

**Voer dit commando uit (vervang met jouw URL):**

```bash
cd D:\MAUREEN\DEV\SeniorEase-Library
git remote add origin https://github.com/JOUW-GEBRUIKERSNAAM/seniorease-library.git
```

## Stap 3: Bestanden Committen en Pushen

```bash
# Voeg alle website bestanden toe
git add website/

# Commit
git commit -m "Add website for APK download"

# Push naar GitHub
git push -u origin master
```

(Als je branch `main` heet in plaats van `master`, gebruik dan `main`)

## Stap 4: Vercel Koppelen aan GitHub

1. **Ga naar [vercel.com](https://vercel.com)**
2. **Klik "Add New..." → "Project"**
3. **Kies "Import Git Repository"**
4. **Selecteer je GitHub repository:** `seniorease-library`
5. **Project Settings:**
   - **Root Directory:** `website` (belangrijk!)
   - **Framework Preset:** Other
   - **Build Command:** (leeg)
   - **Output Directory:** `.`
6. **Klik "Deploy"**

## ✅ Klaar!

Je website wordt automatisch gedeployed vanuit GitHub!

---

## 🔄 Updates in de Toekomst

1. Maak wijzigingen in de `website` folder
2. Commit en push:
   ```bash
   git add website/
   git commit -m "Update website"
   git push
   ```
3. Vercel deployt automatisch! 🚀
