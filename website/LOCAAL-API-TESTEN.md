# 🧪 API Lokaal Testen

## 📋 Vereisten

- Node.js 16+ geïnstalleerd
- Vercel CLI (optioneel, voor lokaal testen)
- Stripe Secret Key

## 🚀 Optie 1: Lokaal Testen met Vercel CLI

### Stap 1: Installeer Vercel CLI (als je die nog niet hebt)

```powershell
npm install -g vercel
```

### Stap 2: Login op Vercel

```powershell
vercel login
```

### Stap 3: Maak `.env` bestand

Maak een `.env` bestand in de `website` directory:

```powershell
cd website
```

Maak `.env` bestand aan met:
```
STRIPE_SECRET_KEY=sk_test_jouw_stripe_key_hier
```

### Stap 4: Start Lokale Server

```powershell
vercel dev
```

Vercel start nu een lokale server (meestal op `http://localhost:3000`)

### Stap 5: Test de API

Open een **nieuwe** PowerShell terminal en test:

```powershell
curl -X POST http://localhost:3000/api/verify-purchase `
  -H "Content-Type: application/json" `
  -d '{\"email\":\"test@example.com\"}'
```

**Of gebruik PowerShell's `Invoke-RestMethod`:**

```powershell
$body = @{
    email = "test@example.com"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:3000/api/verify-purchase" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

---

## 🚀 Optie 2: Test Na Deployment (Sneller)

### Stap 1: Deploy naar Vercel

Push naar GitHub of deploy via Vercel Dashboard.

### Stap 2: Test de Live API

```powershell
$body = @{
    email = "test@example.com"
} | ConvertTo-Json

Invoke-RestMethod -Uri "https://www.seniorease.eu/api/verify-purchase" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

**Verwacht response:**
```json
{
  "paid": false
}
```

---

## 🧪 Test Scenarios

### Test 1: Geldig Email (geen betaling)
```powershell
$body = @{ email = "test@example.com" } | ConvertTo-Json
Invoke-RestMethod -Uri "https://www.seniorease.eu/api/verify-purchase" -Method POST -ContentType "application/json" -Body $body
```
**Verwacht:** `{ "paid": false }`

### Test 2: Ongeldig Email
```powershell
$body = @{ email = "invalid-email" } | ConvertTo-Json
Invoke-RestMethod -Uri "https://www.seniorease.eu/api/verify-purchase" -Method POST -ContentType "application/json" -Body $body
```
**Verwacht:** `{ "paid": false }` (400 status)

### Test 3: Met Echte Betaling
1. Betaal via Stripe met test kaart
2. Gebruik hetzelfde email adres:
```powershell
$body = @{ email = "jouw-email@example.com" } | ConvertTo-Json
Invoke-RestMethod -Uri "https://www.seniorease.eu/api/verify-purchase" -Method POST -ContentType "application/json" -Body $body
```
**Verwacht:** `{ "paid": true }` (als betaling is voltooid)

---

## 🔍 Troubleshooting

### "Cannot find module 'stripe'"
- Check of `package.json` Stripe dependency bevat
- Run `npm install` in `website` directory

### "STRIPE_SECRET_KEY is not configured"
- Check of `.env` bestand bestaat (lokaal)
- Check of environment variable is toegevoegd in Vercel (productie)

### API geeft altijd `{ paid: false }`
- Check of Stripe key correct is (test mode key voor testen)
- Check of betaling is voltooid in Stripe Dashboard
- Check of email exact matcht (case-insensitive)

---

## 💡 Tips

- **Voor lokaal testen:** Gebruik `vercel dev` - dit simuleert de Vercel omgeving
- **Voor snelle test:** Deploy direct en test live API
- **Test altijd eerst** met een email zonder betaling → zou `{ paid: false }` moeten geven
- **Test daarna** met een email met betaling → zou `{ paid: true }` moeten geven

---

**Kies een optie en test de API!**
