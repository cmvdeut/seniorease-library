# 🔍 Betalingsverificatie Probleem

## ⚠️ Probleem
Betaling met `support@seniorease.eu` werkt niet in de app.

---

## ✅ Checklist

### 1. Is de betaling gedaan?
- **Check Stripe Dashboard:** https://dashboard.stripe.com/
- **Ga naar:** Payments
- **Zoek naar:** Betaling met `support@seniorease.eu`
- **Check:** Exact email adres in de betaling

### 2. Timing Issue
**Stripe heeft tijd nodig om te verwerken:**
- Wacht **1-2 minuten** na betaling
- Probeer opnieuw in de app

### 3. Email Adres Match
**Check exact email adres:**
- Geen spaties voor/na
- Exact zoals in Stripe: `support@seniorease.eu`
- Case-insensitive (maar check toch)

### 4. Price ID Match
**De API gebruikt:** `price_1So2gr3GmccxYlytQwl5mitp`

**Check in Stripe:**
1. **Payments → [Je betaling]**
2. **Check:** Welke Price ID is gebruikt?
3. **Vergelijk met:** `price_1So2gr3GmccxYlytQwl5mitp`

### 5. Test API Direct
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
.\test-api-email.ps1 -Email "support@seniorease.eu"
```

### 6. Check Vercel Logs
1. **Vercel Dashboard → Project → Deployments**
2. **Latest deployment → Functions tab**
3. **`/api/verify-purchase` → Logs tab**
4. **Zoek naar:**
   - `[DEBUG] Normalized email: support@seniorease.eu`
   - `[DEBUG] Paid session: id=... email=support@seniorease.eu`
   - `[DEBUG] ✅ Email match!`
   - `[DEBUG] ✅✅✅ MATCH FOUND!`

---

## 🔍 Debug Stappen

### Stap 1: Test API
```powershell
$body = @{email="support@seniorease.eu"} | ConvertTo-Json
Invoke-RestMethod -Uri "https://www.seniorease.eu/api/verify-purchase" -Method Post -Body $body -ContentType "application/json"
```

**Als `{ "paid": false }`:**
- Betaling niet gevonden
- Check Stripe Dashboard
- Check Vercel logs

**Als `{ "paid": true }`:**
- API werkt
- Probleem zit in Android app
- Check Logcat voor errors

### Stap 2: Check Stripe Dashboard
1. **Payments → [Je betaling]**
2. **Check:**
   - Email adres: `support@seniorease.eu`?
   - Status: `Paid`?
   - Price ID: `price_1So2gr3GmccxYlytQwl5mitp`?

### Stap 3: Check Vercel Logs
**Zoek naar DEBUG logs:**
- Email wordt ontvangen?
- Sessions worden gevonden?
- Email match?
- Price ID match?

---

## 💡 Meest Waarschijnlijke Oorzaken

1. **Timing:** Betaling is net gedaan → wacht 1-2 minuten
2. **Email mismatch:** Email in Stripe is anders dan `support@seniorease.eu`
3. **Price ID mismatch:** Betaling gebruikt andere Price ID
4. **Betaling niet gedaan:** Check of betaling echt is gedaan

---

## ✅ Als Alles Faalt

**Check Vercel Logs voor exacte details:**
- Welke email wordt ontvangen?
- Welke sessions worden gevonden?
- Waarom matcht het niet?
