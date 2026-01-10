# ✅ Stripe Configuratie Verificatie

## 🔑 Keys

### Secret Key (voor Vercel Environment Variables)
```
[Je Stripe Secret Key - haal op uit Stripe Dashboard]
```
✅ **Status:** Correct - deze wordt gebruikt in de API

### Publishable Key (niet nodig voor backend)
```
[Je Stripe Publishable Key - niet nodig voor deze integratie]
```
ℹ️ **Notitie:** Deze is alleen nodig voor frontend Stripe.js integratie (niet gebruikt in deze app)

## 🔗 Payment Link

### Link in App
```
https://buy.stripe.com/test_9B6fZa8SW31K0BNcge6c002
```
✅ **Status:** Correct - deze staat in `MainActivity.kt` regel 682

## 💰 Price ID

### Price ID in API
```
price_1So2hP3GmccxYlyt6rNoyUxz
```
✅ **Status:** Correct - deze staat in `website/api/verify-purchase.js` regel 4

## ✅ Checklist

- [x] Secret Key: Correct
- [x] Payment Link: Correct in app
- [x] Price ID: Correct in API
- [ ] **Secret Key toegevoegd aan Vercel?** (moet je zelf checken)
- [ ] **Payment Link actief in Stripe Dashboard?** (moet je zelf checken)

## 🔍 Wat te Controleren

### 1. Vercel Environment Variable
1. Ga naar: https://vercel.com/dashboard
2. Project → Settings → Environment Variables
3. Check of `STRIPE_SECRET_KEY` bestaat met de waarde hierboven
4. Check of alle 3 environments zijn aangevinkt (Production, Preview, Development)

### 2. Stripe Payment Link Status
1. Ga naar: https://dashboard.stripe.com/test/payment-links
2. Zoek link: `9B6fZa8SW31K0BNcge6c002`
3. Check status: Moet **Active** zijn
4. Check Price ID: Moet `price_1So2hP3GmccxYlyt6rNoyUxz` zijn

### 3. Test de Payment Link
1. Open in browser: https://buy.stripe.com/test_9B6fZa8SW31K0BNcge6c002
2. Moet naar Stripe checkout pagina gaan
3. Test met test card: `4242 4242 4242 4242`
4. Gebruik test email (bijv. `test@example.com`)
5. Check of betaling succesvol is

### 4. Test de API
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
.\test-api-online.ps1
```

## 🐛 Als Payment Link "Ongeldig" is

Als de link niet werkt:
1. Check Stripe Dashboard → Payment Links
2. Als link **Inactive** is: klik **Reactivate**
3. Als link niet bestaat: maak nieuwe link met Price ID `price_1So2hP3GmccxYlyt6rNoyUxz`
4. Update de nieuwe link in `MainActivity.kt` regel 682

## ✅ Conclusie

**Alles klopt in de code!** 

De configuratie is correct:
- ✅ Secret Key: Correct
- ✅ Payment Link: Correct in app
- ✅ Price ID: Correct in API

**Volgende stap:** Check of de Secret Key in Vercel staat en of de Payment Link actief is in Stripe Dashboard.
