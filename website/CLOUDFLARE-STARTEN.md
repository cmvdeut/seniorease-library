# 🚀 Cloudflare Tunnel Starten

## ✅ Cloudflared Geïnstalleerd

Cloudflared staat in: `C:\cloudflared\cloudflared.exe`

## 🚀 Starten

### Optie 1: Direct met volledig pad

Open PowerShell en typ:

```powershell
C:\cloudflared\cloudflared.exe tunnel --url http://localhost:8000
```

### Optie 2: Voeg toe aan PATH (permanent)

1. **Druk op `Win + R`**
2. **Typ:** `sysdm.cpl` en druk Enter
3. **Ga naar tab "Geavanceerd"**
4. **Klik op "Omgevingsvariabelen"**
5. **Onder "Systeemvariabelen"** → zoek "Path" → klik "Bewerken"
6. **Klik "Nieuw"** → voeg toe: `C:\cloudflared`
7. **Klik "OK"** op alle vensters
8. **Herstart PowerShell** om PATH te verversen

Na toevoegen aan PATH kun je gewoon typen:
```powershell
cloudflared tunnel --url http://localhost:8000
```

## 📱 Gebruik

Na starten krijg je een URL zoals:
```
https://random-words-1234.trycloudflare.com
```

**Open deze URL op je Android telefoon!**

## ⚠️ Belangrijk

- De URL is **tijdelijk** - alleen zolang cloudflared draait
- Elke keer dat je start, krijg je een **nieuwe URL**
- Perfect voor **lokaal testen**

---

**Start cloudflared en deel de URL die je krijgt!**
