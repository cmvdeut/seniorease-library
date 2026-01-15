# 📱 Demo APK Builden - Stap voor Stap

## ✅ Code Aangepast

De Android code is aangepast met:
- ✅ Build variant `demo` met applicationId: `com.maureen.biblitoheek.demo`
- ✅ Limietcheck: max 10 items in demo versie
- ✅ Demo banner in TopAppBar: "Demo Versie - X/10 items"
- ✅ FAB (knop) disabled bij limiet bereikt
- ✅ Waarschuwing in AddItemDialog bij limiet

---

## 🚀 Demo APK Builden

### Stap 1: Open Android Studio

1. **Open Android Studio**
2. **Open project:** `D:\MAUREEN\DEV\Biblitoheek`

### Stap 2: Sync Gradle

1. **Klik op "Sync Now"** (als Android Studio dit vraagt)
2. Of: **File → Sync Project with Gradle Files**
3. Wacht tot sync klaar is

### Stap 3: Build Demo APK

1. **Build → Generate Signed Bundle / APK**
2. **Selecteer:** APK (niet AAB)
3. **Kies signing key:**
   - Selecteer je bestaande keystore: `upload-keystore.jks`
   - Password: `Can69893!`
   - Key alias: `upload`
   - Key password: `Can69893!`

4. **BELANGRIJK: Kies de juiste variant!**
   - **Build variant:** `demoRelease` (niet fullRelease!)
   - Dit zorgt ervoor dat de limiet van 10 items actief is

5. **Finish** en wacht tot build klaar is

### Stap 4: Hernoem APK

1. **Zoek het APK bestand:**
   - Locatie: `app/build/outputs/apk/demo/release/app-demo-release.apk`
   - Of: `app/release/app-demo-release.apk`

2. **Hernoem naar:**
   ```
   Seniorease-Bibliotheek-Demo.apk
   ```

### Stap 5: Upload naar Website

1. **Kopieer APK naar:**
   ```
   D:\MAUREEN\DEV\Seniorease\seniorease-project\public\Seniorease-Bibliotheek-Demo.apk
   ```

2. **Commit en push:**
   ```bash
   cd D:\MAUREEN\DEV\Seniorease\seniorease-project
   git add public/Seniorease-Bibliotheek-Demo.apk
   git commit -m "Add: Demo APK met limiet van 10 items"
   git push origin main
   ```

3. **Vercel deployt automatisch**

---

## ✅ Test Checklist

Na upload:

1. **Test QR Code:**
   - [ ] Scan QR code op `https://seniorease.nl`
   - [ ] APK download start automatisch

2. **Test APK Installatie:**
   - [ ] Installeer APK op Android telefoon
   - [ ] App opent correct
   - [ ] Demo banner zichtbaar: "Demo Versie - 0/10 items"

3. **Test Limiet:**
   - [ ] Voeg 10 items toe
   - [ ] Probeer 11e item toe te voegen
   - [ ] Waarschuwing verschijnt: "Demo Limiet Bereikt"
   - [ ] FAB (knop) is disabled

---

## 📋 Build Varianten

- **fullRelease:** Volledige versie (onbeperkt items)
  - ApplicationId: `com.maureen.biblitoheek`
  - Geen limiet

- **demoRelease:** Demo versie (max 10 items)
  - ApplicationId: `com.maureen.biblitoheek.demo`
  - Limiet: 10 items
  - Demo banner zichtbaar

---

## ⚠️ Belangrijk

- **Gebruik ALTIJD `demoRelease` variant** voor demo APK
- **Gebruik ALTIJD `fullRelease` variant** voor betaalde APK
- **Verschillende applicationId** = verschillende apps (kunnen naast elkaar geïnstalleerd worden)

---

**Klaar om te builden!** 🎉










