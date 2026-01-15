# 🔧 Vercel Dev Troubleshooting

## ❌ Probleem: 502 Bad Gateway

De Cloudflare Tunnel werkt, maar `vercel dev` reageert niet.

## ✅ Oplossing

### Stap 1: Stop alle processen

Stop alle `vercel dev` en `cloudflared` processen.

### Stap 2: Start Vercel Dev opnieuw

Open een **nieuwe** PowerShell terminal:

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
```

**Zet de Stripe key in environment variable:**
```powershell
$env:STRIPE_SECRET_KEY="sk_test_YOUR_KEY_HERE"
```

**Start vercel dev:**
```powershell
vercel dev
```

**Wacht tot je ziet:**
```
Ready! Available at http://localhost:3000
```

### Stap 3: Start Cloudflare Tunnel (in nieuwe terminal)

```powershell
C:\cloudflared\cloudflared.exe tunnel --url http://localhost:3000
```

**Noteer de URL** die je krijgt (bijv. `https://abc-123.trycloudflare.com`)

### Stap 4: Test de API

```powershell
$url = "https://jouw-cloudflare-url.trycloudflare.com/api/verify-purchase"
$body = @{ email = "test@example.com" } | ConvertTo-Json
Invoke-RestMethod -Uri $url -Method POST -ContentType "application/json" -Body $body
```

## 🔍 Alternatief: Direct Deployen

Als lokaal testen niet werkt, deploy direct naar Vercel:

1. **Push naar GitHub**
2. **Vercel deployt automatisch**
3. **Test live API:** `https://www.seniorease.eu/api/verify-purchase`

---

**Start vercel dev opnieuw en wacht tot "Ready!" verschijnt!**
