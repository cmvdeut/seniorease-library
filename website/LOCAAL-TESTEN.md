# 🧪 Lokaal Testen met Cloudflare Tunnel

## ✅ Status

De Stripe test link staat al correct in de app:
- Link: `https://buy.stripe.com/test_9B6fZa8SW31K0BNcge6c002`

## 🚀 Lokaal Testen

### Stap 1: Start Lokale Server

De server draait al op poort 8000:
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
python -m http.server 8000
```

### Stap 2: Start Cloudflare Tunnel

Open een **nieuwe** PowerShell terminal en typ:

```powershell
cloudflared tunnel --url http://localhost:8000
```

**Je krijgt een URL zoals:**
```
https://random-words-1234.trycloudflare.com
```

### Stap 3: Test op Telefoon

1. **Noteer de Cloudflare URL** (bijv. `https://random-words-1234.trycloudflare.com`)
2. **Open op je Android telefoon** in de browser
3. **Test de website:**
   - Download knop werkt
   - QR code is zichtbaar
   - Payment link opent correct

### Stap 4: Test Payment Link

1. **Klik op "Open Payment Link in browser"** in de app
2. **Stripe payment pagina opent**
3. **Gebruik testkaart:**
   - Kaartnummer: `4242 4242 4242 4242`
   - Expiry: `12/25` (of elke toekomstige datum)
   - CVC: `123` (of elke 3 cijfers)
4. **Test de betaling**

## ⚠️ Belangrijk

- **Zorg dat Stripe Dashboard in Test mode staat** (niet Live mode)
- **Gebruik alleen testkaarten** (niet echte kaarten)
- **Cloudflare URL is tijdelijk** - alleen zolang cloudflared draait

## 🔍 Troubleshooting

**Als payment link niet werkt:**
1. Check Stripe Dashboard → Test mode toggle
2. Check of payment link in Test mode is gemaakt
3. Gebruik testkaart: `4242 4242 4242 4242`

**Als Cloudflare niet werkt:**
- Check of cloudflared geïnstalleerd is: `cloudflared --version`
- Check of lokale server draait: `netstat -ano | findstr :8000`

---

**Start cloudflared en test de payment link op je telefoon!**
