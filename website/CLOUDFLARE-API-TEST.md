# 🌐 API Testen via Cloudflare Tunnel

## ✅ Setup

1. **Lokale server draait** op `http://localhost:8000`
2. **Cloudflare Tunnel gestart** - geeft je een publieke URL

## 🔗 Cloudflare URL

Na het starten van Cloudflare Tunnel krijg je een URL zoals:
```
https://random-words-1234.trycloudflare.com
```

## 🧪 API Testen

### Test 1: Via PowerShell

```powershell
# Vervang CLOUDFLARE_URL met de URL die je krijgt
$cloudflareUrl = "https://jouw-cloudflare-url.trycloudflare.com"

$body = @{
    email = "test@example.com"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$cloudflareUrl/api/verify-purchase" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

### Test 2: Via Browser (OPTIONS check)

Open in browser:
```
https://jouw-cloudflare-url.trycloudflare.com/api/verify-purchase
```

### Test 3: Via cURL

```bash
curl -X POST https://jouw-cloudflare-url.trycloudflare.com/api/verify-purchase \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com"}'
```

## ⚠️ Belangrijk

- **Cloudflare URL is tijdelijk** - alleen zolang tunnel draait
- **API route werkt** via Cloudflare Tunnel
- **Stripe key** staat in `.env` bestand

## 🔍 Verwacht Resultaat

**Met test email (geen betaling):**
```json
{
  "paid": false
}
```

**Met email waar je betaald hebt:**
```json
{
  "paid": true
}
```

---

**Noteer de Cloudflare URL en test de API!**
