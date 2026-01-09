# 🚀 Vercel Deployment Instructies

## Stap 1: Project Aanmaken in Vercel

1. **Ga naar [vercel.com](https://vercel.com)** en log in
2. **Klik op "Add New..."** → **"Project"**
3. **Kies een van deze opties:**

### Optie A: Via GitHub (Aanbevolen)
- Koppel je GitHub repository
- Selecteer de repository
- **Root Directory:** `website`
- Klik "Deploy"

### Optie B: Via CLI
```bash
cd website
npm install -g vercel
vercel login
vercel
```

### Optie C: Via Drag & Drop
- Ga naar Vercel dashboard
- Sleep de `website` folder naar de upload zone
- Vercel detecteert automatisch de configuratie

## Stap 2: Project Configuratie

In Vercel project settings:

- **Framework Preset:** Other
- **Root Directory:** `website` (of `.` als je alleen de website folder upload)
- **Build Command:** (leeg laten - static site)
- **Output Directory:** `.` (punt)
- **Install Command:** (leeg laten)

## Stap 3: Domein Koppelen (seniorease.eu)

1. In je Vercel project → **Settings** → **Domains**
2. Klik **"Add Domain"**
3. Voer in: `seniorease.eu`
4. Vercel geeft je DNS records:
   - **Type:** A of CNAME
   - **Name:** @ of www
   - **Value:** Vercel IP of CNAME record

5. **Voeg DNS records toe bij je domain provider:**
   - Log in bij je domain provider (waar je seniorease.eu hebt gekocht)
   - Ga naar DNS settings
   - Voeg de records toe die Vercel geeft
   - Wacht 5-60 minuten tot DNS is gepropageerd

## Stap 4: APK Uploaden

### Via Vercel Dashboard:
1. Ga naar je project → **Settings** → **Files**
2. Upload `app-demo-release.apk` naar `/downloads/` folder
   - Of gebruik Vercel CLI: `vercel --prod` en upload handmatig

### Via Git (Aanbevolen):
1. Kopieer de APK naar `website/downloads/`:
   ```bash
   copy "app\build\outputs\apk\demo\release\app-demo-release.apk" "website\downloads\app-demo-release.apk"
   ```
2. Commit en push naar GitHub
3. Vercel deployt automatisch

## Stap 5: QR Code Uploaden

1. **Genereer QR code:**
   - Open `generate-qr-code.html` in browser
   - Voer in: `https://seniorease.eu/downloads/app-demo-release.apk`
   - Download de QR code

2. **Upload naar website:**
   - Kopieer de QR code naar `website/qr-code-apk-download.png`
   - Of upload via Vercel dashboard

## Stap 6: Testen

1. **Test de website:**
   - Ga naar `https://seniorease.eu`
   - Controleer of alles correct wordt weergegeven

2. **Test de download:**
   - Klik op de download knop
   - Controleer of de APK download start

3. **Test QR code:**
   - Scan de QR code met je telefoon
   - Controleer of de download link werkt

## ✅ Klaar!

Je website is nu live op `https://seniorease.eu`!

---

## 🔄 Updates

Bij elke update:
1. Build nieuwe APK: `./gradlew assembleDemoRelease`
2. Kopieer naar `website/downloads/`
3. Commit en push (of upload via Vercel)
4. Vercel deployt automatisch
