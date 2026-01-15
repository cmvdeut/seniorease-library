# 🔍 Unlock Verificatie Probleem - Diagnose

## 📋 Situatie

- ✅ Betaling ging goed in test fase
- ❌ App geeft error: "Er ging iets mis bij het controleren"
- ✅ API werkt (test geeft `{ paid: false }` terug)

## 🔍 Mogelijke Oorzaken

### 1. Betaling Nog Niet Zichtbaar in Stripe
- **Probleem:** Stripe heeft betaling nog niet verwerkt
- **Oplossing:** Wacht 1-2 minuten en probeer opnieuw
- **Check:** Ga naar Stripe Dashboard → Payments → Check of betaling er is

### 2. Email Adres Komt Niet Overeen
- **Probleem:** Email in betaling is anders dan ingevoerd email
- **Oplossing:** Check welk email je hebt gebruikt bij Stripe checkout
- **Check:** Stripe Dashboard → Checkout Sessions → Check `customer_details.email`

### 3. STRIPE_SECRET_KEY Niet in Vercel
- **Probleem:** API kan niet met Stripe communiceren
- **Oplossing:** Voeg `STRIPE_SECRET_KEY` toe aan Vercel Environment Variables
- **Check:** Vercel Dashboard → Settings → Environment Variables

### 4. Network Error op Telefoon
- **Probleem:** Telefoon kan API niet bereiken
- **Oplossing:** Check internet verbinding op telefoon
- **Check:** Probeer website te openen op telefoon

## 🧪 Test Stappen

### Stap 1: Test API Direct
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
$url = "https://www.seniorease.eu/api/verify-purchase"
$body = @{ email = "cmvdeut@gmail.com" } | ConvertTo-Json
Invoke-RestMethod -Uri $url -Method POST -ContentType "application/json" -Body $body
```

**Verwacht:** `{ paid: true }` of `{ paid: false }`

### Stap 2: Check Stripe Dashboard
1. Ga naar: https://dashboard.stripe.com/test/payments
2. Zoek betaling
2. Check email adres: `customer_details.email`
3. Check status: Moet `Succeeded` zijn

### Stap 3: Check Vercel Logs
1. Ga naar: https://vercel.com/dashboard
2. Project → Deployments → Laatste deployment
3. Klik op "Functions" tab
4. Check logs voor errors

### Stap 4: Check App Logs (Logcat)
1. Open Android Studio
2. Verbind telefoon
3. Open Logcat
4. Filter op: `UnlockVerify`
5. Probeer unlock opnieuw
6. Check error messages

## ✅ Verbeteringen Toegevoegd

- ✅ Betere error logging in app
- ✅ Specifieke error handling voor network errors
- ✅ JSON parsing error handling
- ✅ Response code logging

**Na nieuwe APK build:** Check Logcat voor details!

## 🔧 Oplossingen

### Als API `{ paid: false }` geeft:
1. **Check Stripe Dashboard:**
   - Is betaling succesvol?
   - Klopt email adres?
   - Is Price ID correct?

2. **Check Vercel:**
   - Is `STRIPE_SECRET_KEY` toegevoegd?
   - Is deployment succesvol?

3. **Wacht even:**
   - Stripe kan 1-2 minuten nodig hebben om betaling te verwerken

### Als API error geeft:
1. **Check Vercel Logs:**
   - Zijn er errors in function logs?
   - Is `STRIPE_SECRET_KEY` geconfigureerd?

2. **Test API direct:**
   - Gebruik `test-api-online.ps1`
   - Check response

## 📱 Nieuwe APK Bouwen

Na code wijzigingen:
1. Build nieuwe APK in Android Studio
2. Installeer op telefoon
3. Test opnieuw
4. Check Logcat voor details

---

**Check eerst Stripe Dashboard en Vercel Environment Variables!**
