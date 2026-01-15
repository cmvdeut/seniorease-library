# ✅ Deployment Klaar - Test Instructies

## 🎉 Vercel Deployment is Gereed!

Nu is het tijd om te testen of alles werkt.

---

## 🧪 Stap 1: Test API Direct

Test de API met PowerShell:

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
.\test-api-email.ps1 -Email "cmvdeut@gmail.com"
```

**Verwacht:**
- `{ paid: true }` - Als betaling gevonden wordt ✅
- `{ paid: false }` - Als geen betaling gevonden wordt (check Stripe Dashboard)

---

## 📱 Stap 2: Test in App

### Op je Telefoon:

1. **Open de app** (nieuwste versie met email-based unlock)

2. **Optie A: Via "I've paid" knop**
   - Klik op "I've paid — unlock" (of "Ik heb betaald — ontgrendelen")
   - Voer email in: `cmvdeut@gmail.com`
   - Klik "Check purchase" (of "Betaling controleren")

3. **Optie B: Na 10 boeken scannen**
   - Scan 10 boeken
   - Klik "Unlock full version"
   - Klik "I've paid — unlock"
   - Voer email in
   - Klik "Check purchase"

### Wat er zou moeten gebeuren:

✅ **Als betaling gevonden wordt:**
- App wordt unlocked
- Melding: "Wonderful! Your full version is now unlocked..."
- Je kunt nu onbeperkt boeken toevoegen

❌ **Als betaling niet gevonden wordt:**
- Melding: "We could not find a payment for this email address yet..."
- Check Stripe Dashboard
- Probeer opnieuw (kan 1-2 minuten duren)

❌ **Als er een error is:**
- Melding: "We had trouble checking your purchase..."
- Check internet verbinding
- Check Vercel logs

---

## 🔍 Stap 3: Troubleshooting

### Als API `{ paid: false }` geeft:

1. **Check Stripe Dashboard:**
   - Ga naar: https://dashboard.stripe.com/test/payments
   - Zoek je betaling
   - Check:
     - Status: Moet `Succeeded` zijn
     - Email: Moet exact overeenkomen
     - Price ID: Moet `price_1So2hP3GmccxYlyt6rNoyUxz` zijn

2. **Check Checkout Sessions:**
   - Ga naar: https://dashboard.stripe.com/test/checkout/sessions
   - Zoek session met je email
   - Check:
     - `payment_status`: Moet `paid` zijn
     - `customer_details.email`: Moet exact overeenkomen
     - Line Items: Moet Price ID bevatten

3. **Wacht even:**
   - Stripe kan 1-2 minuten nodig hebben om te indexeren
   - Probeer na 2 minuten opnieuw

### Als App Error geeft:

1. **Check Vercel Logs:**
   - Ga naar: https://vercel.com/dashboard
   - Project → Deployments → Laatste deployment
   - Klik "Functions" tab
   - Check logs voor errors

2. **Check Environment Variables:**
   - Ga naar: Settings → Environment Variables
   - Check of `STRIPE_SECRET_KEY` bestaat
   - Check of alle 3 environments zijn aangevinkt

3. **Check Internet:**
   - App heeft internet nodig voor API call
   - Test website op telefoon: https://www.seniorease.eu

---

## ✅ Checklist

- [ ] API test geeft response (geen error)
- [ ] API test met je email geeft `{ paid: true }` of `{ paid: false }`
- [ ] App kan API bereiken (geen network error)
- [ ] Email verificatie werkt in app
- [ ] App wordt unlocked na succesvolle verificatie

---

## 🎯 Verwacht Resultaat

**Als alles werkt:**
1. ✅ API geeft `{ paid: true }` voor je email
2. ✅ App toont success message
3. ✅ App wordt unlocked
4. ✅ Je kunt onbeperkt boeken toevoegen

**Als er problemen zijn:**
- Check Stripe Dashboard eerst
- Check Vercel logs
- Test API direct met PowerShell script

---

**Test nu de API en de app!** 🚀
