# 📱 Demo APK Builden - Stap voor Stap Handleiding

## ⚠️ BELANGRIJK: Demo Variant Gebruiken!

**Gebruik ALTIJD `demoRelease` variant** - anders overschrijf je de volledige versie!

---

## 🚀 Stap 1: Open Android Studio

1. **Open Android Studio**
2. **Open project:** 
   ```
   D:\MAUREEN\DEV\Biblitoheek
   ```
3. Wacht tot project volledig geladen is

---

## 🔄 Stap 2: Sync Gradle (indien nodig)

1. **Als Android Studio vraagt om sync:**
   - Klik op **"Sync Now"** in de melding
   - Of: **File → Sync Project with Gradle Files**
2. **Wacht tot sync klaar is** (zie statusbalk onderaan)

---

## 📦 Stap 3: Kies Demo Build Variant

**⚠️ KRITIEK: Dit is de belangrijkste stap!**

1. **Kijk linksonder in Android Studio** - zie je "Build Variants" tab?
   - Zo niet: **View → Tool Windows → Build Variants**

2. **In het "Build Variants" venster:**
   - Zoek **"app"** module
   - Klik op de dropdown naast "app"
   - **Kies: `demoRelease`** (NIET `fullRelease`!)
   
3. **Controleer:**
   - ✅ `demoRelease` staat geselecteerd
   - ✅ Je ziet: `demoRelease` (niet `fullRelease` of `debug`)

**Waarom dit belangrijk is:**
- `demoRelease` = Demo versie (max 10 items, applicationId: `com.maureen.biblitoheek.demo`)
- `fullRelease` = Volledige versie (onbeperkt, applicationId: `com.maureen.biblitoheek`)
- **Verschillende applicationId = verschillende apps** (kunnen naast elkaar geïnstalleerd worden)

---

## 🔨 Stap 4: Build Demo APK

### Methode A: Via Build Menu (Aanbevolen)

1. **Klik in menubalk:** **Build → Generate Signed Bundle / APK**

2. **Kies APK:**
   - Selecteer **"APK"** (niet AAB)
   - Klik **Next**

3. **Selecteer Signing Key:**
   - **Key store path:** `D:\MAUREEN\DEV\Biblitoheek\upload-keystore.jks`
   - **Key store password:** `Can69893!`
   - **Key alias:** `upload`
   - **Key password:** `Can69893!`
   - Klik **Next**

4. **⚠️ BELANGRIJK: Kies Demo Variant!**
   - **Build variant:** Kies **`demoRelease`** (NIET `fullRelease`!)
   - **Vink aan:** "Remember passwords"
   - Klik **Finish**

5. **Wacht tot build klaar is:**
   - Zie statusbalk onderaan: "Build: finished"
   - Of zie melding: "APK(s) generated successfully"

### Methode B: Via Gradle (Alternatief)

1. **Kijk rechts in Android Studio** - zie je "Gradle" tab?
   - Zo niet: **View → Tool Windows → Gradle**

2. **In Gradle venster:**
   - Navigeer: **Biblitoheek → app → Tasks → build**
   - Dubbelklik op: **`assembleDemoRelease`**
   - (NIET `assembleFullRelease`!)

3. **Wacht tot build klaar is**

---

## 📍 Stap 5: Vind Demo APK Bestand

**Locatie van demo APK:**
```
D:\MAUREEN\DEV\Biblitoheek\app\build\outputs\apk\demo\release\app-demo-release.apk
```

**Hoe te vinden:**
1. **In Android Studio:**
   - Klik rechts op **"app"** module
   - **Open in Explorer** (of **Reveal in Finder** op Mac)
   - Navigeer naar: `build\outputs\apk\demo\release\`

2. **Of via Windows Explorer:**
   - Ga naar: `D:\MAUREEN\DEV\Biblitoheek\app\build\outputs\apk\demo\release\`
   - Zoek bestand: `app-demo-release.apk`

---

## ✏️ Stap 6: Hernoem APK

**⚠️ BELANGRIJK: Exact deze naam gebruiken!**

1. **Rechtsklik op:** `app-demo-release.apk`
2. **Kies:** "Rename" (of "Hernoemen")
3. **Hernoem naar:**
   ```
   Seniorease-Bibliotheek-Demo.apk
   ```
4. **Controleer:**
   - ✅ Naam is exact: `Seniorease-Bibliotheek-Demo.apk`
   - ✅ Geen extra spaties of tekens
   - ✅ Bestand eindigt op `.apk`

---

## 📤 Stap 7: Kopieer naar Website Project

1. **Kopieer het bestand:**
   - Van: `D:\MAUREEN\DEV\Biblitoheek\app\build\outputs\apk\demo\release\Seniorease-Bibliotheek-Demo.apk`
   - Naar: `D:\MAUREEN\DEV\Seniorease\seniorease-project\public\Seniorease-Bibliotheek-Demo.apk`

2. **Controleer:**
   - ✅ Bestand staat in `public` folder
   - ✅ Naam is exact: `Seniorease-Bibliotheek-Demo.apk`
   - ✅ Bestand is niet leeg (check grootte)

---

## ✅ Stap 8: Verifieer Demo APK

**Controleer voordat je uploadt:**

1. **Bestandsnaam:**
   - ✅ `Seniorease-Bibliotheek-Demo.apk` (niet `Seniorease-Bibliotheek.apk`!)

2. **Bestandsgrootte:**
   - ✅ Moet ongeveer 10-30 MB zijn (niet 0 bytes!)

3. **Build variant:**
   - ✅ APK is gebouwd met `demoRelease` variant
   - ✅ ApplicationId: `com.maureen.biblitoheek.demo` (niet `.biblitoheek`!)

**Hoe te controleren:**
- Open APK met 7-Zip of WinRAR
- Ga naar: `META-INF\MANIFEST.MF`
- Zoek: `Application-Id:` - moet `com.maureen.biblitoheek.demo` zijn

---

## 🚀 Stap 9: Upload naar Website

### Optie A: Via GitHub Desktop

1. **Open GitHub Desktop**
2. **Sleep bestand** naar `public` folder in GitHub Desktop
3. **Commit message:** `Add: Demo APK met limiet van 10 items`
4. **Klik:** "Commit to main"
5. **Klik:** "Push origin"

### Optie B: Via Command Line

```bash
# Ga naar website project
cd D:\MAUREEN\DEV\Seniorease\seniorease-project

# Controleer of bestand er is
dir public\Seniorease-Bibliotheek-Demo.apk

# Voeg toe aan git
git add public/Seniorease-Bibliotheek-Demo.apk

# Commit
git commit -m "Add: Demo APK met limiet van 10 items"

# Push
git push origin main
```

---

## 🎯 Stap 10: Test Demo APK

**Na upload (Vercel deployt automatisch):**

1. **Test QR Code:**
   - [ ] Ga naar: `https://seniorease.nl`
   - [ ] Scan QR code met Android telefoon
   - [ ] APK download start automatisch

2. **Test Directe Link:**
   - [ ] Ga naar: `https://seniorease.nl/api/download-demo-app`
   - [ ] APK download start

3. **Test Installatie:**
   - [ ] Installeer APK op Android telefoon
   - [ ] App opent correct
   - [ ] Demo banner zichtbaar: "Demo Versie - 0/10 items"

4. **Test Limiet:**
   - [ ] Voeg 10 items toe
   - [ ] Probeer 11e item toe te voegen
   - [ ] Waarschuwing verschijnt: "Demo Limiet Bereikt"
   - [ ] FAB (knop) is disabled

---

## ⚠️ Veelgemaakte Fouten

### ❌ Fout 1: Verkeerde Build Variant
**Probleem:** Je bouwt `fullRelease` in plaats van `demoRelease`
**Gevolg:** Volledige versie wordt overschreven!
**Oplossing:** Controleer Build Variants venster - moet `demoRelease` zijn!

### ❌ Fout 2: Verkeerde Bestandsnaam
**Probleem:** APK heet `Seniorease-Bibliotheek.apk` (zonder "Demo")
**Gevolg:** Website kan demo APK niet vinden
**Oplossing:** Hernoem naar `Seniorease-Bibliotheek-Demo.apk`

### ❌ Fout 3: APK in Verkeerde Folder
**Probleem:** APK staat niet in `public` folder
**Gevolg:** Website kan demo APK niet downloaden
**Oplossing:** Kopieer naar `public\Seniorease-Bibliotheek-Demo.apk`

---

## 📋 Build Varianten Overzicht

| Variant | ApplicationId | Limiet | Gebruik |
|---------|--------------|--------|---------|
| `fullRelease` | `com.maureen.biblitoheek` | Onbeperkt | Betaalde versie |
| `demoRelease` | `com.maureen.biblitoheek.demo` | 10 items | Demo versie |

**Belangrijk:**
- ✅ Verschillende applicationId = verschillende apps
- ✅ Beide kunnen naast elkaar geïnstalleerd worden
- ✅ Demo overschrijft NIET de volledige versie

---

## ✅ Checklist voor Build

Voor je build:
- [ ] Build Variants venster open
- [ ] `demoRelease` geselecteerd (niet `fullRelease`!)
- [ ] Gradle sync klaar

Na build:
- [ ] APK gevonden in `app\build\outputs\apk\demo\release\`
- [ ] APK hernoemd naar `Seniorease-Bibliotheek-Demo.apk`
- [ ] APK gekopieerd naar `public` folder
- [ ] Bestandsnaam is exact correct
- [ ] Bestand is niet leeg

Voor upload:
- [ ] Git commit gemaakt
- [ ] Git push gedaan
- [ ] Vercel deploy gestart

---

## 🎉 Klaar!

Als alles goed is gegaan:
- ✅ Demo APK staat op website
- ✅ QR code werkt
- ✅ Download werkt
- ✅ Volledige versie is NIET overschreven

**Probleem?** Controleer de checklist hierboven!










