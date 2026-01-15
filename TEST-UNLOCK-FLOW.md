# 🧪 Testen van de "I've paid — unlock" Flow

## 📋 Overzicht

Deze gids helpt je om de unlock flow te testen:
1. API server starten (lokaal)
2. App builden en installeren
3. Testen van de unlock flow

---

## 🚀 Stap 1: API Server Starten

### Optie A: Lokaal (localhost)

1. **Open PowerShell in de `api` folder:**
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\api
```

2. **Installeer dependencies (eerste keer):**
```powershell
npm install
```

3. **Maak `.env` bestand aan:**
```powershell
# Maak een nieuw bestand .env
STRIPE_SECRET_KEY=sk_test_jouw_stripe_key_hier
PORT=3000
```

4. **Start de server:**
```powershell
npm start
```

Server draait nu op: `http://localhost:3000`

### Optie B: Met Cloudflare Tunnel (voor telefoon testen)

Als je op je telefoon wilt testen:

1. **Start de API server** (zie Optie A)

2. **Open nieuwe PowerShell terminal:**
```powershell
C:\cloudflared\cloudflared.exe tunnel --url http://localhost:3000
```

3. **Noteer de URL** die je krijgt (bijv. `https://random-words-1234.trycloudflare.com`)

4. **Pas API URL aan in de app** (zie Stap 2)

---

## 📱 Stap 2: App Builden en Installeren

### 1. Pas API URL aan (als je Cloudflare gebruikt)

Open `app/src/main/java/com/seniorease/library/MainActivity.kt` en zoek naar regel ~780:

```kotlin
// Vervang deze regel:
val apiUrl = "https://seniorease.eu/api/verify-purchase"

// Met jouw Cloudflare URL (als je lokaal test):
val apiUrl = "https://jouw-cloudflare-url.trycloudflare.com/api/verify-purchase"
```

**Of laat staan als je productie API gebruikt!**

### 2. Build de app:

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
.\gradlew assembleDemoRelease
```

### 3. Installeer op telefoon:

APK staat in: `app\build\outputs\apk\demo\release\app-demo-release.apk`

---

## ✅ Stap 3: Testen

### Test Scenario 1: Betaling Verifiëren

1. **Open de app** op je telefoon
2. **Voeg 10 boeken toe** (demo limiet)
3. **Bij 10e boek** → unlock dialog verschijnt
4. **Klik "Ik heb betaald — ontgrendelen"**
5. **Voer test email in** (bijv. `test@example.com`)
6. **Klik "Betaling controleren"**

### Test Scenario 2: Met Echte Stripe Betaling

1. **Betaal via Stripe** met test kaart:
   - Kaart: `4242 4242 4242 4242`
   - Expiry: `12/25`
   - CVC: `123`
   - **Gebruik een echt email adres!**

2. **In de app:**
   - Klik "Ik heb betaald — ontgrendelen"
   - Voer hetzelfde email adres in
   - Klik "Betaling controleren"
   - ✅ App wordt unlocked!

---

## 🔍 Troubleshooting

### API Server start niet?

**Check:**
- Is Node.js geïnstalleerd? `node --version`
- Zijn dependencies geïnstalleerd? `npm install`
- Staat Stripe key in `.env`? Check `.env` bestand

### App kan API niet bereiken?

**Check:**
- Draait de API server? (zie terminal)
- Is de API URL correct in `MainActivity.kt`?
- Heeft de telefoon internet? (of gebruik Cloudflare Tunnel)

### "Betaling niet gevonden" error?

**Check:**
- Is de email exact hetzelfde als bij Stripe betaling?
- Is de betaling voltooid in Stripe Dashboard?
- Check Stripe Dashboard → Payments → zoek op email

### API geeft error?

**Check API server terminal:**
- Zie je error messages?
- Is Stripe key correct?
- Check `.env` bestand

---

## 📝 Test Checklist

- [ ] API server draait (`npm start`)
- [ ] Stripe key staat in `.env`
- [ ] App is gebuild (`assembleDemoRelease`)
- [ ] App is geïnstalleerd op telefoon
- [ ] API URL is correct in `MainActivity.kt`
- [ ] Test email is ingevoerd
- [ ] Betaling is gevonden (of niet, beide scenarios testen)

---

## 🎯 Snelle Test (Zonder Echte Betaling)

Voor snelle test zonder Stripe:

1. **Start API server**
2. **Test met willekeurig email** → zou `{ paid: false }` moeten geven
3. **Check of error message rustig is** (senior-vriendelijk)

---

## 💡 Tips

- **Voor lokaal testen:** Gebruik Cloudflare Tunnel om API beschikbaar te maken op telefoon
- **Voor productie:** Zet API op `https://seniorease.eu/api/verify-purchase`
- **Test altijd met echte Stripe betaling** voordat je live gaat!

---

**Vragen? Check de API README in `api/README.md`**
