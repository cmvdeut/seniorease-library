# 🌐 Cloudflare Tunnel voor Telefoon Testen

## Wat is Cloudflare Tunnel?

Cloudflare Tunnel (cloudflared) maakt je lokale server beschikbaar via een publieke URL, zodat je de website op je telefoon kunt testen zonder te deployen.

## 📥 Installatie

### Windows:

1. **Download cloudflared:**
   - Ga naar: https://github.com/cloudflare/cloudflared/releases
   - Download: `cloudflared-windows-amd64.exe`
   - Hernoem naar: `cloudflared.exe`
   - Plaats in een map (bijv. `C:\cloudflared\`)

2. **Voeg toe aan PATH (optioneel):**
   - Zoek "Environment Variables" in Windows
   - Voeg de map toe aan PATH

### Of via Chocolatey (als je dat hebt):
```powershell
choco install cloudflared
```

## 🚀 Gebruik

### Stap 1: Start je lokale server

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
python -m http.server 8000
```

### Stap 2: Start Cloudflare Tunnel (in nieuwe terminal)

```powershell
cloudflared tunnel --url http://localhost:8000
```

### Stap 3: Gebruik de URL

Cloudflare geeft je een URL zoals:
```
https://random-words-1234.trycloudflare.com
```

**Open deze URL op je Android telefoon!**

## 📱 Testen op Telefoon

1. **Noteer de URL** die cloudflared geeft
2. **Open op je telefoon** in de browser
3. **Test de website** - download knop, QR code, etc.

## ⚠️ Belangrijk

- De URL is **tijdelijk** - alleen zolang cloudflared draait
- Elke keer dat je cloudflared start, krijg je een **nieuwe URL**
- Perfect voor **testen**, niet voor productie

## 🔄 Alternatief: ngrok

Als cloudflared niet werkt, kun je ook ngrok gebruiken:

```powershell
ngrok http 8000
```

---

**Start cloudflared en deel de URL met je telefoon!**
