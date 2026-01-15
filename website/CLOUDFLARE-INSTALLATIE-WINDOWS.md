# 🚀 Cloudflare Tunnel Installatie voor Windows

## Snelle Installatie

### Optie 1: Direct Downloaden (Aanbevolen)

1. **Download cloudflared:**
   - Ga naar: https://github.com/cloudflare/cloudflared/releases/latest
   - Download: `cloudflared-windows-amd64.exe`
   - Hernoem het bestand naar: `cloudflared.exe`

2. **Plaats het bestand:**
   - Maak een map aan: `C:\cloudflared\`
   - Plaats `cloudflared.exe` in deze map

3. **Voeg toe aan PATH (optioneel maar handig):**
   - Druk op `Win + R`
   - Typ: `sysdm.cpl` en druk Enter
   - Ga naar tab "Geavanceerd"
   - Klik op "Omgevingsvariabelen"
   - Onder "Systeemvariabelen" → zoek "Path" → klik "Bewerken"
   - Klik "Nieuw" → voeg toe: `C:\cloudflared`
   - Klik "OK" op alle vensters
   - **Herstart PowerShell/CMD** om PATH te verversen

### Optie 2: Via Chocolatey (als je dat hebt)

```powershell
choco install cloudflared
```

## ✅ Test Installatie

Open een **nieuwe** PowerShell/CMD en typ:

```powershell
cloudflared --version
```

Als je een versienummer ziet, is het geïnstalleerd! ✅

## 🚀 Gebruik

### Stap 1: Start lokale server (in eerste terminal)

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
python -m http.server 8000
```

### Stap 2: Start Cloudflare Tunnel (in tweede terminal)

```powershell
cloudflared tunnel --url http://localhost:8000
```

### Stap 3: Gebruik de URL

Cloudflare geeft je een URL zoals:
```
https://random-words-1234.trycloudflare.com
```

**Open deze URL op je Android telefoon in de browser!**

## 📱 Testen

1. Noteer de URL die cloudflared geeft
2. Open op je telefoon in de browser
3. Test de website - download knop, QR code, payment link, etc.

## ⚠️ Belangrijk

- De URL is **tijdelijk** - alleen zolang cloudflared draait
- Elke keer dat je cloudflared start, krijg je een **nieuwe URL**
- Perfect voor **lokaal testen**, niet voor productie

---

**Na installatie: Start cloudflared en deel de URL met je telefoon!**
