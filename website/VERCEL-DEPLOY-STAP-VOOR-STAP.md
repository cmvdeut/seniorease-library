# 🚀 Vercel Deployment - Stap voor Stap

## ✅ Wat ik al heb gedaan:

- ✅ Website bestanden aangemaakt (`index.html`, `vercel.json`, etc.)
- ✅ APK gekopieerd naar `downloads/` folder
- ✅ QR code gegenereerd (`qr-code-apk-download.png`)
- ✅ Alles klaar voor upload!

---

## 📤 Stap 1: Upload naar Vercel

### ⚠️ BELANGRIJK: Gebruik "Upload" NIET "Import Git Repository"!

### Optie A: Via Drag & Drop (Makkelijkste)

1. **Ga naar [vercel.com](https://vercel.com)** en log in
2. **Klik op "Add New..."** (rechtsboven)
3. **Zoek naar "Upload" of "Browse"** (NIET "Import Git Repository")
4. **Open Windows Verkenner:**
   - Ga naar: `D:\MAUREEN\DEV\SeniorEase-Library\website`
   - Selecteer ALLE bestanden in de website folder
   - **Sleep deze naar Vercel** (naar het upload gebied)
5. **Project naam:** `seniorease-library` (of wat je wilt)
6. **Klik "Deploy"**

### Optie B: Via Browse Knop

1. In Vercel dashboard, klik op **"Add New..."**
2. Kies **"Browse"** of **"Upload"** (NIET Git Repository!)
3. Navigeer naar: `D:\MAUREEN\DEV\SeniorEase-Library\website`
4. Selecteer de `website` folder
5. Klik "Deploy"

---

## ⚙️ Stap 2: Project Instellingen (Automatisch)

Vercel detecteert automatisch:
- ✅ Framework: Static Site
- ✅ Build Command: (geen nodig)
- ✅ Output Directory: `.`

**Je hoeft niets aan te passen!**

---

## 🌐 Stap 3: Domein Koppelen (seniorease.eu)

1. **In je Vercel project:**
   - Ga naar **Settings** → **Domains**
   - Klik **"Add Domain"**

2. **Voer in:**
   - `seniorease.eu`
   - Klik "Add"

3. **DNS Records toevoegen:**
   - Vercel geeft je DNS records (bijv. CNAME of A record)
   - **Log in bij je domain provider** (waar je seniorease.eu hebt gekocht)
   - Ga naar **DNS Settings**
   - Voeg de records toe die Vercel geeft:
     - **Type:** CNAME (of A)
     - **Name:** @ (of www)
     - **Value:** Vercel geeft je dit
   - Sla op

4. **Wacht op DNS propagatie:**
   - Duurt 5-60 minuten
   - Vercel toont status: "Valid Configuration" wanneer klaar

---

## ✅ Stap 4: Testen

1. **Test de website:**
   - Ga naar `https://seniorease.eu` (of de Vercel URL eerst)
   - Controleer of alles correct wordt weergegeven

2. **Test de download:**
   - Klik op "Download APK"
   - Controleer of de download start

3. **Test QR code:**
   - Scan de QR code met je telefoon
   - Controleer of de download link werkt

---

## 🎉 Klaar!

Je website is nu live op `https://seniorease.eu`!

---

## 📝 Wat staat er op de website:

- ✅ Professionele download pagina
- ✅ Installatie instructies
- ✅ QR code voor mobiel scannen
- ✅ Directe download knop
- ✅ Alle informatie over de app

---

## 🔄 Updates in de toekomst:

1. Build nieuwe APK: `./gradlew assembleDemoRelease`
2. Kopieer naar `website/downloads/`
3. Upload opnieuw naar Vercel (of push naar Git als je dat gebruikt)
4. Vercel deployt automatisch!

---

**Alles staat klaar - je hoeft alleen nog te uploaden naar Vercel! 🚀**
