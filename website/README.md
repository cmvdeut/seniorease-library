# SeniorEase Library - Download Website

Website voor het downloaden van de SeniorEase Library Android app.

## 🚀 Deployment op Vercel

### Stap 1: Project aanmaken in Vercel

1. Ga naar [vercel.com](https://vercel.com) en log in
2. Klik op **"Add New..."** → **"Project"**
3. Importeer deze repository of upload de `website` folder
4. Configureer het project:
   - **Framework Preset:** Other
   - **Root Directory:** `website`
   - **Build Command:** (leeg laten)
   - **Output Directory:** `.` (punt)

### Stap 2: Domein koppelen

1. In Vercel project settings → **Domains**
2. Voeg toe: `seniorease.eu`
3. Volg de DNS instructies om je domein te koppelen

### Stap 3: APK en QR Code uploaden

1. Upload `app-demo-release.apk` naar `/downloads/` folder
2. Upload de QR code afbeelding naar de root als `qr-code-apk-download.png`

## 📁 Bestandsstructuur

```
website/
├── index.html              # Hoofdpagina
├── vercel.json            # Vercel configuratie
├── package.json           # Project configuratie
├── downloads/             # APK bestanden (upload hier)
│   └── app-demo-release.apk
└── qr-code-apk-download.png  # QR code afbeelding
```

## 🔧 Aanpassingen

- **Download link:** Pas aan in `index.html` regel met `href="/downloads/app-demo-release.apk"`
- **QR code:** Vervang `qr-code-apk-download.png` met je eigen QR code
- **Teksten:** Pas aan in `index.html` naar wens

## 📱 QR Code Genereren

Gebruik `generate-qr-code.html` uit de root van het project om een QR code te genereren met de juiste download URL.
