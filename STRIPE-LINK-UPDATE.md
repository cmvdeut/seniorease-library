# 🔗 Stripe Payment Link Updaten

## ⚠️ Probleem
De Stripe payment link werkt niet meer ("link is no longer available").

## ✅ Oplossing: Nieuwe Link Maken

### Stap 1: Maak Nieuwe Payment Link in Stripe

1. **Log in op Stripe Dashboard:**
   - Ga naar: https://dashboard.stripe.com/
  - **Zorg dat je in Live mode bent** (toggle rechtsboven)

2. **Maak nieuwe Payment Link:**
   - Ga naar: **Products** → **Payment Links**
   - Klik op: **Create payment link**
   
3. **Configureer de link:**
   - **Product:** Kies of maak een product aan (bijv. "SeniorEase Library Full Version")
   - **Price:** Maak een prijs aan (bijv. €9.99 of jouw prijs)
  - **Zorg dat je in Live mode bent!**
   - Klik **Create link**

4. **Kopieer de link:**
   - Je krijgt een link zoals: `https://buy.stripe.com/XXXXXXXXXXXXX`
   - **Kopieer de volledige URL**

### Stap 2: Update Link in App

1. **Open `MainActivity.kt`:**
   - Bestand: `app/src/main/java/com/seniorease/library/MainActivity.kt`
   - Zoek naar regel ~682

2. **Vervang de oude link:**
```kotlin
// OUDE LINK (vervang deze):
val paymentUrl = "https://buy.stripe.com/aFaaEQ9X0dGogALgwu6c003"

// NIEUWE LINK (plak jouw nieuwe link hier):
val paymentUrl = "https://buy.stripe.com/JOUW_NIEUWE_LINK_HIER"
```

### Stap 3: Rebuild App

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
.\gradlew assembleDemoRelease
```

### Stap 4: Installeer Nieuwe APK

- APK staat in: `app\build\outputs\apk\demo\release\app-demo-release.apk`
- Installeer op je telefoon

---

## 🔍 Check Dit

- [ ] Stripe Dashboard staat in **Live mode**
- [ ] Nieuwe payment link is gemaakt in **Live mode**
- [ ] Link is geüpdatet in `MainActivity.kt`
- [ ] App is opnieuw gebuild
- [ ] Nieuwe APK is geïnstalleerd

---

## 💡 Tips

- **Doe een echte betaling** om de link te testen
- **Bewaar de link** ergens veilig voor later gebruik

---

**Na het updaten: Test de payment link in de app!**

