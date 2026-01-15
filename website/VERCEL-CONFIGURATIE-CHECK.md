# ✅ Vercel Configuratie Check

## Project ID
`prj_CDUBLeoBl7KwA9a7xitHMjwimjb0`

## 📋 Checklist voor Vercel Settings

### 1. Root Directory
- **Moet zijn:** `website`
- **Niet:** `www.seniorease.eu` of leeg
- **Locatie:** Settings → General → Root Directory

### 2. Framework Preset
- **Moet zijn:** `Other` of `Static Site`
- **Locatie:** Settings → General → Framework Preset

### 3. Build & Output Settings
- **Build Command:** (leeg laten)
- **Output Directory:** `.` (punt)
- **Install Command:** (leeg laten)
- **Locatie:** Settings → General → Build & Development Settings

### 4. Environment Variables
- **Geen nodig** voor deze static site

### 5. Domains
- **Toegevoegd:** `seniorease.eu` (als je dat wilt)
- **Status:** Moet "Valid Configuration" zijn
- **Locatie:** Settings → Domains

---

## 📁 Bestandsstructuur (moet zo zijn)

```
website/
├── index.html          ✅ Hoofdpagina
├── vercel.json         ✅ Vercel configuratie
├── package.json        ✅ Project configuratie
├── downloads/
│   └── app-demo-release.apk  ✅ APK bestand
├── qr-code-apk-download.png  ✅ QR code
└── (andere bestanden)
```

---

## 🔍 Verificatie Commands

### Test lokaal (optioneel):
```bash
cd website
vercel dev
```

### Check deployment:
- Ga naar: `https://seniorease-library.vercel.app`
- Moet de download pagina tonen (niet 404)

---

## ❌ Veelvoorkomende Problemen

### 404 Error
- **Oorzaak:** Root Directory niet op `website`
- **Fix:** Settings → General → Root Directory = `website`

### APK download werkt niet
- **Oorzaak:** Headers niet correct
- **Fix:** `vercel.json` heeft al correcte headers voor `/downloads/*`

### QR code laadt niet
- **Oorzaak:** Bestand niet in `website/` folder
- **Fix:** Zorg dat `qr-code-apk-download.png` in `website/` staat

---

## ✅ Correcte Vercel.json Configuratie

```json
{
  "version": 2,
  "builds": [
    {
      "src": "index.html",
      "use": "@vercel/static"
    }
  ],
  "routes": [
    {
      "src": "/(.*)",
      "dest": "/$1"
    }
  ],
  "headers": [
    {
      "source": "/downloads/(.*)",
      "headers": [
        {
          "key": "Content-Type",
          "value": "application/vnd.android.package-archive"
        },
        {
          "key": "Content-Disposition",
          "value": "attachment"
        }
      ]
    }
  ]
}
```

---

## 🚀 Deployment Status Check

1. **Ga naar Vercel Dashboard**
2. **Klik op project:** `seniorease-library`
3. **Check:**
   - ✅ Latest deployment: "Ready" (groen)
   - ✅ Root Directory: `website`
   - ✅ Build logs: Geen errors
   - ✅ Website laadt: Geen 404

---

**Alles moet nu werken als Root Directory op `website` staat!**
