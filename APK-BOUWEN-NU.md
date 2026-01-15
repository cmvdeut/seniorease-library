# 📱 APK Bouwen in Android Studio - Snelle Handleiding

## ⚠️ BELANGRIJK: Demo Variant!

**Gebruik ALTIJD `demoRelease` variant** - anders overschrijf je de volledige versie!

---

## 🚀 Stap-voor-Stap

### Stap 1: Open Android Studio
1. **Open Android Studio**
2. **Open project:** `D:\MAUREEN\DEV\SeniorEase-Library`
3. Wacht tot project volledig geladen is (sync kan even duren)

### Stap 2: Kies Demo Build Variant (KRITIEK!)
1. **Kijk linksonder in Android Studio** - zie je "Build Variants" tab?
   - Zo niet: **View → Tool Windows → Build Variants**

2. **In het "Build Variants" venster:**
   - Zoek **"app"** module
   - Klik op de dropdown naast "app"
   - **Kies: `demoRelease`** (NIET `fullRelease`!)

3. **Controleer:**
   - ✅ `demoRelease` staat geselecteerd
   - ✅ Je ziet: `demoRelease` (niet `fullRelease` of `debug`)

### Stap 3: Build de APK
1. **Klik in de menubalk:** **Build**
2. **Kies:** **Build Bundle(s) / APK(s)**
3. **Kies:** **Build APK(s)**
4. **Wacht tot build klaar is** (zie statusbalk onderaan)

### Stap 4: Vind de APK
Na het bouwen:
1. **Klik op:** **"locate"** in de melding die verschijnt
2. **Of ga naar:**
   ```
   app\build\outputs\apk\demo\release\app-demo-release.apk
   ```

### Stap 5: Kopieer naar Website
1. **Kopieer de APK** naar:
   ```
   website\downloads\app-demo-release.apk
   ```
2. **Vervang** de oude APK als die er al is

### Stap 6: Update QR Code (optioneel)
Als de download URL is veranderd:
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
python generate-qr-code.py
```

### Stap 7: Deploy Website
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
git add website/downloads/app-demo-release.apk
git commit -m "Update APK with latest changes"
git push
```

---

## ✅ Checklist

- [ ] Android Studio geopend
- [ ] Project geladen: `D:\MAUREEN\DEV\SeniorEase-Library`
- [ ] Build Variant: `demoRelease` (niet `fullRelease`!)
- [ ] APK gebouwd: `app-demo-release.apk`
- [ ] APK gekopieerd naar `website/downloads/`
- [ ] Website gedeployed

---

## 🐛 Troubleshooting

### "Build failed" Error
- ✅ Check of Build Variant op `demoRelease` staat
- ✅ Sync Gradle: **File → Sync Project with Gradle Files**
- ✅ Clean project: **Build → Clean Project**, dan opnieuw builden

### APK niet gevonden
- ✅ Check: `app\build\outputs\apk\demo\release\`
- ✅ Check of build succesvol was (geen errors in Build tab)

### Verkeerde variant gebouwd
- ✅ Check Build Variants tab (linksonder)
- ✅ Zorg dat `demoRelease` geselecteerd is
- ✅ Rebuild: **Build → Rebuild Project**

---

**Na het bouwen: De nieuwe APK bevat alle laatste wijzigingen!** 🚀
