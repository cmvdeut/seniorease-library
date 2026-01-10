# 🔑 Stripe Secret Key Toevoegen aan Vercel

## ⚠️ BELANGRIJK: Dit moet je zelf doen in Vercel Dashboard

Ik kan de secret key niet automatisch toevoegen - dit moet via de Vercel website.

## 📋 Stap-voor-Stap Instructies

### Stap 1: Ga naar Vercel Dashboard
1. Open: https://vercel.com/dashboard
2. Log in met je account

### Stap 2: Selecteer je Project
1. Klik op je project: **seniorease-library** (of de naam die je hebt gebruikt)

### Stap 3: Ga naar Settings
1. Klik op **Settings** (bovenaan in het menu)
2. Klik op **Environment Variables** (in het linker menu)

### Stap 4: Voeg de Key Toe
1. Klik op **Add New** (of **Add**)
2. Vul in:
   - **Key:** `STRIPE_SECRET_KEY`
   - **Value:** `[Je Stripe Secret Key - haal op uit Stripe Dashboard]`
   - **Environment:** 
     - ✅ **Production**
     - ✅ **Preview** 
     - ✅ **Development**
3. Klik op **Save**

### Stap 5: Redeploy
1. Ga naar **Deployments** (in het linker menu)
2. Klik op de **3 puntjes** (⋯) naast de laatste deployment
3. Klik op **Redeploy**
4. Wacht 2-3 minuten tot deployment klaar is

## ✅ Testen

Na deployment, test de API:

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
.\test-api.ps1
```

Of test handmatig:
```powershell
$url = "https://www.seniorease.eu/api/verify-purchase"
$body = @{ email = "test@example.com" } | ConvertTo-Json
Invoke-RestMethod -Uri $url -Method POST -ContentType "application/json" -Body $body
```

**Verwacht:** `{ "paid": false }` (omdat test@example.com geen betaling heeft)

## 🐛 Troubleshooting

### "Invalid API Key" Error
- ✅ Check of de key correct is gekopieerd (zonder spaties)
- ✅ Check of alle 3 environments zijn aangevinkt
- ✅ Check of je **Redeploy** hebt gedaan na het toevoegen

### "Function not found" Error
- ✅ Check of de deployment succesvol was
- ✅ Check of `website/api/verify-purchase.js` bestaat in GitHub

### API geeft altijd `{ paid: false }`
- ✅ Dit is normaal als er geen betaling is voor dat email adres
- ✅ Test met een email waarmee je daadwerkelijk hebt betaald via Stripe
