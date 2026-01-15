# 🔍 Test Nieuwe Betaling Verificatie

## ⚠️ Probleem
Betaling met `senioreasemail@gmail.com` is gelukt op Stripe, maar verificatie in app werkt niet.

---

## ✅ Oplossingen

### 1. Timing Issue (Meest Waarschijnlijk)
**Stripe heeft tijd nodig om de checkout session te verwerken.**

**Wacht 1-2 minuten** na betaling en probeer opnieuw.

### 2. Check Vercel Logs
1. **Ga naar Vercel Dashboard**
2. **Project → Deployments → Latest**
3. **Klik op "Functions" tab**
4. **Klik op `/api/verify-purchase`**
5. **Check "Logs" tab**

**Zoek naar:**
- `[DEBUG] Normalized email: senioreasemail@gmail.com`
- `[DEBUG] Paid session: id=... email=senioreasemail@gmail.com`
- `[DEBUG] ✅ Email match!`
- `[DEBUG] ✅✅✅ MATCH FOUND!`

### 3. Check Price ID
**De API gebruikt:** `price_1So2gr3GmccxYlytQwl5mitp`

**Check in Stripe Dashboard:**
1. **Ga naar:** Products → Payment Links
2. **Open je Payment Link**
3. **Check welke Price ID gebruikt wordt**
4. **Vergelijk met:** `price_1So2gr3GmccxYlytQwl5mitp`

**Als Price ID niet matcht:**
- Update `STRIPE_PRICE_ID` in Vercel Environment Variables
- Of pas hardcoded Price ID aan in `api/verify-purchase.js`

### 4. Test API Direct
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
$body = @{email="senioreasemail@gmail.com"} | ConvertTo-Json
Invoke-RestMethod -Uri "https://www.seniorease.eu/api/verify-purchase" -Method Post -Body $body -ContentType "application/json"
```

---

## 🔍 Debug Checklist

- [ ] Betaling is minstens 1-2 minuten geleden
- [ ] Email is exact: `senioreasemail@gmail.com` (geen spaties)
- [ ] Price ID in Stripe matcht met API
- [ ] Vercel logs tonen de betaling
- [ ] Checkout session heeft status 'complete'
- [ ] Checkout session heeft payment_status 'paid'

---

## 💡 Meest Waarschijnlijke Oorzaak

**Timing issue** - Stripe heeft tijd nodig om de checkout session te verwerken. Wacht 1-2 minuten en probeer opnieuw.
