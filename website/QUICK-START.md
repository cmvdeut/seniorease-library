# ⚡ Quick Start - Vercel Deployment

## 🚀 Snelle Deployment

### Optie 1: Via Vercel Dashboard (Snelste)

1. **Ga naar [vercel.com](https://vercel.com)** en log in
2. **Klik "Add New..." → "Project"**
3. **Upload de `website` folder:**
   - Sleep de hele `website` folder naar Vercel
   - Of klik "Browse" en selecteer de `website` folder
4. **Project Settings:**
   - Framework: **Other**
   - Root Directory: **website** (of `.` als je alleen de website folder upload)
   - Build Command: (leeg)
   - Output Directory: `.`
5. **Klik "Deploy"**
6. **Domein toevoegen:**
   - Settings → Domains → Add Domain
   - Voer in: `seniorease.eu`
   - Volg DNS instructies

### Optie 2: Via Vercel CLI

```bash
cd website
npm install -g vercel
vercel login
vercel
```

## 📦 Bestanden die je nodig hebt:

✅ `index.html` - Hoofdpagina  
✅ `vercel.json` - Configuratie  
✅ `package.json` - Project info  
✅ `downloads/app-demo-release.apk` - De APK (al gekopieerd!)  
⏳ `qr-code-apk-download.png` - QR code (genereer met `generate-qr.html`)

## 🔲 QR Code Genereren:

1. Open `generate-qr.html` in je browser
2. De URL staat al goed: `https://seniorease.eu/downloads/app-demo-release.apk`
3. Klik "Genereer QR Code"
4. Klik "Download QR Code"
5. Sla op als `qr-code-apk-download.png` in de `website` folder

## ✅ Klaar!

Na deployment is je website live op:
- **Vercel URL:** `https://jouw-project.vercel.app`
- **Custom domain:** `https://seniorease.eu` (na DNS setup)

---

**Tip:** Test eerst op de Vercel URL voordat je het domein koppelt!
