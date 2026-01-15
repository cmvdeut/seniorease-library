# ✅ Demo APK Verificatie - Hoe Controleren?

## 🔍 Methode 1: Via Android Studio Build Output (Aanbevolen)

**Tijdens het builden:**

1. **Kijk in de "Build" output** (onderaan in Android Studio)
2. **Zoek naar regel:**
   ```
   > Task :app:packageDemoRelease
   ```
   - ✅ Als je `packageDemoRelease` ziet = Demo variant!
   - ❌ Als je `packageFullRelease` ziet = Verkeerde variant!

3. **Of zoek naar:**
   ```
   > Task :app:assembleDemoRelease
   ```
   - ✅ `assembleDemoRelease` = Demo variant!
   - ❌ `assembleFullRelease` = Verkeerde variant!

---

## 🔍 Methode 2: Via APK Bestandsnaam

**Controleer de bestandsnaam:**

✅ **Goed (Demo):**
```
app-demo-release.apk
```

❌ **Fout (Volledige versie):**
```
app-full-release.apk
```

**Locatie:**
```
D:\MAUREEN\DEV\Biblitoheek\app\build\outputs\apk\demo\release\app-demo-release.apk
```

**Als het in deze folder staat:** `apk\demo\release\` = Demo variant! ✅

---

## 🔍 Methode 3: Via Build Variants Venster

**In Android Studio:**

1. **Open Build Variants venster** (linksonder)
2. **Kijk bij "app" module:**
   - ✅ Moet zeggen: `demoRelease`
   - ❌ NIET: `fullRelease` of `debug`

3. **Als `demoRelease` geselecteerd is** = Je bouwt de demo variant! ✅

---

## 🔍 Methode 4: Via AndroidManifest.xml in APK

**Met 7-Zip of WinRAR:**

1. **Open APK** met 7-Zip of WinRAR
2. **Ga naar:** `AndroidManifest.xml` (in root, niet in META-INF)
3. **Open met tekst editor** (kan binary zijn, gebruik Android Asset Packaging Tool)
4. **Zoek naar:** `package="com.maureen.biblitoheek"`
   - ✅ `package="com.maureen.biblitoheek.demo"` = Demo variant!
   - ❌ `package="com.maureen.biblitoheek"` = Volledige versie!

**Eenvoudiger alternatief:**
- Gebruik **aapt** tool (Android Asset Packaging Tool)
- Of installeer APK en check in app settings

---

## 🔍 Methode 5: Via App Installatie (Beste Test!)

**Installeer APK op Android telefoon:**

1. **Installeer de APK** op je Android telefoon
2. **Open de app**
3. **Kijk in de TopAppBar:**
   - ✅ **Demo variant:** Zie je "Demo Versie - 0/10 items" banner
   - ❌ **Volledige versie:** Geen demo banner

4. **Test limiet:**
   - ✅ **Demo variant:** Kan max 10 items toevoegen
   - ❌ **Volledige versie:** Onbeperkt items

5. **Check app naam in settings:**
   - Ga naar: **Instellingen → Apps**
   - Zoek: "Biblitoheek"
   - ✅ **Demo variant:** App heet "Biblitoheek" (of met demo suffix)
   - ✅ **Verschillende app ID:** Kan naast volledige versie staan

---

## 🔍 Methode 6: Via APK Analyzer in Android Studio

**In Android Studio:**

1. **Build → Analyze APK...**
2. **Selecteer je APK:** `app-demo-release.apk`
3. **Kijk in "AndroidManifest.xml" tab:**
   - Zoek: `package` attribute
   - ✅ `com.maureen.biblitoheek.demo` = Demo variant!
   - ❌ `com.maureen.biblitoheek` = Volledige versie!

---

## ✅ Snelle Checklist

**Voor je build:**
- [ ] Build Variants venster open
- [ ] `demoRelease` geselecteerd (niet `fullRelease`!)
- [ ] Build output toont `assembleDemoRelease` of `packageDemoRelease`

**Na build:**
- [ ] APK staat in folder: `apk\demo\release\`
- [ ] APK heet: `app-demo-release.apk`
- [ ] Bestandsnaam bevat "demo"

**Na installatie (optioneel):**
- [ ] Demo banner zichtbaar: "Demo Versie - X/10 items"
- [ ] Limiet werkt: max 10 items
- [ ] App kan naast volledige versie geïnstalleerd worden

---

## ⚠️ Belangrijk

**Als je twijfelt:**

1. **Controleer Build Variants venster** - moet `demoRelease` zijn
2. **Check APK bestandsnaam** - moet `app-demo-release.apk` zijn
3. **Check folder locatie** - moet in `apk\demo\release\` staan

**Als ALLE drie kloppen:** Je hebt de demo variant! ✅

---

## 🎯 Meest Betrouwbare Methode

**Gewoon vertrouwen op:**
1. ✅ Build Variants venster toont `demoRelease`
2. ✅ APK staat in `apk\demo\release\` folder
3. ✅ APK heet `app-demo-release.apk`

**Als deze 3 punten kloppen, dan is het 100% zeker de demo variant!**










