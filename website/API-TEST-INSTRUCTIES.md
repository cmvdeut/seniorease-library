# 🧪 API Test Instructies

## ✅ API Status

De API werkt correct! Test met `test@example.com` geeft `{ paid: false }` terug, wat normaal is.

## 🔍 Test met Je Eigen Email

Om te testen of je betaling wordt gevonden:

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
$url = "https://www.seniorease.eu/api/verify-purchase"
$body = @{ email = "cmvdeut@gmail.com" } | ConvertTo-Json
Invoke-RestMethod -Uri $url -Method POST -ContentType "application/json" -Body $body
```

**Verwacht:**
- `{ paid: true }` - Als betaling gevonden wordt
- `{ paid: false }` - Als geen betaling gevonden wordt

## 🔍 Check Stripe Dashboard

Als `{ paid: false }` wordt teruggegeven:

1. **Ga naar:** https://dashboard.stripe.com/test/payments
2. **Zoek je betaling:**
   - Check status: Moet `Succeeded` zijn
   - Check email: `customer_details.email` moet exact overeenkomen
   - Check Price ID: Moet `price_1So2hP3GmccxYlyt6rNoyUxz` zijn

3. **Check Checkout Sessions:**
   - Ga naar: https://dashboard.stripe.com/test/checkout/sessions
   - Zoek session met je email
   - Check `payment_status`: Moet `paid` zijn
   - Check `customer_details.email`: Moet exact overeenkomen (case-insensitive)

## ⚠️ Veelvoorkomende Problemen

### Email Adres Komt Niet Overeen
- **Probleem:** Email in Stripe is anders dan ingevoerd
- **Oplossing:** Check exact email adres in Stripe Dashboard
- **Tip:** Email wordt case-insensitive vergeleken, maar moet exact overeenkomen

### Betaling Nog Niet Verwerkt
- **Probleem:** Stripe heeft betaling nog niet verwerkt
- **Oplossing:** Wacht 1-2 minuten en probeer opnieuw
- **Check:** Stripe Dashboard → Payments → Status

### Price ID Klopt Niet
- **Probleem:** Betaling is voor andere Price ID
- **Oplossing:** Check Price ID in Stripe Dashboard
- **Verwacht:** `price_1So2hP3GmccxYlyt6rNoyUxz`

### STRIPE_SECRET_KEY Niet in Vercel
- **Probleem:** API kan niet met Stripe communiceren
- **Oplossing:** Voeg `STRIPE_SECRET_KEY` toe aan Vercel Environment Variables
- **Check:** Vercel Dashboard → Settings → Environment Variables

## ✅ Nieuwe API Functionaliteit

De API gebruikt nu:
- ✅ Stripe Checkout Session **search** (efficiënter)
- ✅ Zoekt specifiek op: `payment_status:'paid' AND customer_details.email:'<email>'`
- ✅ Checkt line items voor exacte Price ID
- ✅ Rate limiting (20 requests/min per IP)
- ✅ Veilige error handling (geen Stripe objecten lekken)

## 🧪 Test Checklist

- [ ] API geeft response (niet error)
- [ ] Test email geeft `{ paid: false }` (normaal)
- [ ] Je eigen email geeft `{ paid: true }` of `{ paid: false }`
- [ ] Stripe Dashboard toont betaling
- [ ] Email adres komt overeen
- [ ] Price ID klopt

---

**Als betaling niet wordt gevonden, check eerst Stripe Dashboard!**
