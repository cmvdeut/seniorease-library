# 🔄 Wanneer Moet je de APK Opnieuw Builden?

## ✅ JA - APK Moet Opnieuw Gebouwd Worden

### Android App Code Wijzigingen:
- ✅ **Kotlin code** (`MainActivity.kt`, `AddItemDialog.kt`, etc.)
- ✅ **String resources** (`strings.xml`, `strings-en.xml`)
- ✅ **Layout/UI** (`*.xml` bestanden)
- ✅ **Build configuratie** (`build.gradle.kts`)
- ✅ **Dependencies** (nieuwe libraries)
- ✅ **Assets** (icons, images, etc.)

**Voorbeelden:**
- Email-based unlock flow toevoegen → ✅ Rebuild
- Tekst aanpassen → ✅ Rebuild
- Nieuwe functie toevoegen → ✅ Rebuild
- Bug fix in app code → ✅ Rebuild

---

## ❌ NEE - Geen APK Rebuild Nodig

### Website Wijzigingen:
- ❌ **HTML/CSS** (`index.html`, styling)
- ❌ **Website content** (tekst op website)
- ❌ **QR code** (nieuwe QR code genereren)

### Backend/API Wijzigingen:
- ❌ **Vercel Serverless Function** (`api/verify-purchase.js`)
- ❌ **API logica** (Stripe verificatie, etc.)
- ❌ **Environment Variables** (Stripe keys in Vercel)

**Voorbeelden:**
- API verbeteren → ❌ Alleen Vercel deployen
- Website tekst aanpassen → ❌ Alleen website deployen
- Stripe key toevoegen → ❌ Alleen Vercel redeployen

---

## 📋 Checklist

### Moet APK Rebuild?
- [ ] Kotlin code gewijzigd? → ✅ Rebuild
- [ ] String resources gewijzigd? → ✅ Rebuild
- [ ] UI/Layout gewijzigd? → ✅ Rebuild
- [ ] Build configuratie gewijzigd? → ✅ Rebuild
- [ ] Nieuwe functie in app? → ✅ Rebuild

### Geen APK Rebuild Nodig:
- [ ] Alleen website gewijzigd? → ❌ Alleen website deployen
- [ ] Alleen API gewijzigd? → ❌ Alleen Vercel deployen
- [ ] Alleen Vercel configuratie? → ❌ Alleen Vercel redeployen

---

## 🎯 Praktische Voorbeelden

### Voorbeeld 1: API Verbeteren
**Wijziging:** `website/api/verify-purchase.js` verbeteren
- ❌ **Geen APK rebuild nodig**
- ✅ **Wel:** Vercel redeployen (automatisch via GitHub push)

### Voorbeeld 2: App Tekst Aanpassen
**Wijziging:** "Unlock full version" tekst aanpassen in `strings.xml`
- ✅ **APK rebuild nodig**
- ✅ **Wel:** Nieuwe APK bouwen en naar website kopiëren

### Voorbeeld 3: Website Content Aanpassen
**Wijziging:** Tekst op `index.html` aanpassen
- ❌ **Geen APK rebuild nodig**
- ✅ **Wel:** Website deployen (automatisch via GitHub push)

### Voorbeeld 4: Bug Fix in App
**Wijziging:** Bug fix in `MainActivity.kt`
- ✅ **APK rebuild nodig**
- ✅ **Wel:** Nieuwe APK bouwen en naar website kopiëren

---

## 🚀 Workflow

### Als je App Code Wijzigt:
1. ✅ Wijzig code in Android Studio
2. ✅ Build nieuwe APK (`demoRelease`)
3. ✅ Kopieer APK naar `website/downloads/`
4. ✅ Commit en push naar GitHub
5. ✅ Vercel deployt automatisch

### Als je Alleen Website/API Wijzigt:
1. ✅ Wijzig website of API code
2. ✅ Commit en push naar GitHub
3. ✅ Vercel deployt automatisch
4. ❌ **Geen APK rebuild nodig!**

---

## 💡 Tip

**Check altijd:** "Heb ik Android app code gewijzigd?"
- **JA** → Rebuild APK
- **NEE** → Alleen deployen

**De laatste keer dat je APK rebuild nodig had:**
- Email-based unlock flow toevoegen → ✅ Rebuild
- Betere error logging toevoegen → ✅ Rebuild

**Sindsdien:**
- API verbeteren → ❌ Alleen Vercel deployen
- Website aanpassen → ❌ Alleen website deployen

---

**Kort antwoord: Alleen als je Android app code wijzigt!** 📱
