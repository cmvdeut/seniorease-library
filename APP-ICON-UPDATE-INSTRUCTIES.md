# 🎨 App Icoon Update - SeniorEase Logo

## ✅ Wat is al gedaan:

1. ✅ **Vector Drawables aangepast:**
   - `ic_launcher_background.xml` → SeniorEase cream achtergrond (#F5EEE6)
   - `ic_launcher_foreground.xml` → SeniorEase hart logo in bruin (#8B5E3C)
   - `colors.xml` → SeniorEase kleuren toegevoegd

2. ✅ **Adaptive Icon configuratie** werkt nu met het nieuwe logo

---

## ⚠️ OPTIONEEL: PNG Iconen Vervangen (voor beste kwaliteit)

Voor oudere Android versies of als fallback worden PNG iconen gebruikt. Deze staan in:
- `app/src/main/res/mipmap-hdpi/`
- `app/src/main/res/mipmap-mdpi/`
- `app/src/main/res/mipmap-xhdpi/`
- `app/src/main/res/mipmap-xxhdpi/`
- `app/src/main/res/mipmap-xxxhdpi/`

### Methode 1: Android Studio Image Asset Studio (Aanbevolen)

1. Open Android Studio
2. Ga naar: **File → New → Image Asset**
3. **Icon Type:** Launcher Icons (Adaptive and Legacy)
4. **Foreground Layer:**
   - **Asset Type:** Image
   - **Path:** `D:\MAUREEN\DEV\Seniorease\seniorease-project\public\heart-logo.png`
   - **Scaling:** 100% (of pas aan voor beste weergave)
5. **Background Layer:**
   - **Asset Type:** Color
   - **Color:** `#F5EEE6` (SeniorEase cream)
6. Klik **Next** → **Finish**
7. Android Studio genereert automatisch alle iconen in de juiste formaten!

### Methode 2: Online Tool

1. Ga naar: https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html
2. Upload: `heart-logo.png`
3. Stel achtergrondkleur in: `#F5EEE6`
4. Download het gegenereerde ZIP bestand
5. Pak uit en kopieer de mipmap mappen naar `app/src/main/res/`

### Methode 3: Handmatig (niet aanbevolen)

Als je handmatig wilt:
1. Gebruik een image editor (Photoshop, GIMP, etc.)
2. Maak iconen in deze formaten:
   - **hdpi:** 72x72 px
   - **mdpi:** 48x48 px
   - **xhdpi:** 96x96 px
   - **xxhdpi:** 144x144 px
   - **xxxhdpi:** 192x192 px
3. Plaats het logo op een cream achtergrond (#F5EEE6)
4. Sla op als `ic_launcher.png` en `ic_launcher_round.png` in elke mipmap map

---

## 🎨 Logo Specificaties

- **Logo bestand:** `D:\MAUREEN\DEV\Seniorease\seniorease-project\public\heart-logo.png`
- **Achtergrond kleur:** `#F5EEE6` (SeniorEase cream)
- **Logo kleur:** `#8B5E3C` (SeniorEase bruin)
- **Vorm:** Omgekeerd hart met gezicht

---

## ✅ Testen

Na het updaten:
1. Build de app in Android Studio
2. Installeer op een emulator of fysiek apparaat
3. Controleer of het icoon correct wordt weergegeven
4. Test op verschillende Android versies (indien mogelijk)

---

## 📝 Notities

- De vector drawables werken al, dus de app heeft nu al het nieuwe logo!
- PNG iconen zijn alleen nodig voor oudere Android versies of als fallback
- Android Studio Image Asset Studio is de makkelijkste methode

---

**Status:** Vector iconen zijn klaar! ✅  
**Optioneel:** PNG iconen kunnen worden toegevoegd voor beste compatibiliteit.










