# 📤 Demo APK Uploaden - Stap voor Stap

## 📍 Stap 1: Vind je APK Bestand

**Locatie van je gebouwde APK:**
```
D:\MAUREEN\DEV\Biblitoheek\app\build\outputs\apk\demo\release\app-demo-release.apk
```

**Hoe te vinden:**
1. Open **Windows Verkenner**
2. Navigeer naar: `D:\MAUREEN\DEV\Biblitoheek\app\build\outputs\apk\demo\release\`
3. Zoek bestand: `app-demo-release.apk`

---

## ✏️ Stap 2: Hernoem APK

**⚠️ BELANGRIJK: Exact deze naam gebruiken!**

1. **Rechtsklik** op `app-demo-release.apk`
2. **Kies:** "Hernoemen" (of "Rename")
3. **Type:** `Seniorease-Bibliotheek-Demo.apk`
4. **Druk Enter**

**Controleer:**
- ✅ Naam is exact: `Seniorease-Bibliotheek-Demo.apk`
- ✅ Geen extra spaties
- ✅ Bestand eindigt op `.apk`

---

## 📋 Stap 3: Kopieer naar Website Project

**Van:**
```
D:\MAUREEN\DEV\Biblitoheek\app\build\outputs\apk\demo\release\Seniorease-Bibliotheek-Demo.apk
```

**Naar:**
```
D:\MAUREEN\DEV\Seniorease\seniorease-project\public\Seniorease-Bibliotheek-Demo.apk
```

**Hoe:**
1. **Open Windows Verkenner**
2. **Navigeer naar:** `D:\MAUREEN\DEV\Biblitoheek\app\build\outputs\apk\demo\release\`
3. **Zoek:** `Seniorease-Bibliotheek-Demo.apk`
4. **Rechtsklik** → **Kopieer** (of Ctrl+C)
5. **Navigeer naar:** `D:\MAUREEN\DEV\Seniorease\seniorease-project\public\`
6. **Rechtsklik** → **Plak** (of Ctrl+V)

**Controleer:**
- ✅ Bestand staat in `public` folder
- ✅ Naam is exact: `Seniorease-Bibliotheek-Demo.apk`
- ✅ Bestand is niet leeg (check grootte - moet ~10-30 MB zijn)

---

## 📤 Stap 4: Upload naar GitHub

### Optie A: Via GitHub Desktop (Makkelijkst) ⭐

1. **Open GitHub Desktop**
2. **Zorg dat je in het juiste project bent:**
   - Project: `seniorease-project`
   - Repository: `seniorease-project` (of jouw repo naam)

3. **Kijk links in GitHub Desktop:**
   - Je ziet nu: `Seniorease-Bibliotheek-Demo.apk` in de lijst van gewijzigde bestanden
   - Met een **groene +** ernaast (nieuw bestand)

4. **Onderaan links:**
   - **Summary:** Type: `Add: Demo APK met limiet van 10 items`
   - **Description:** (optioneel) Laat leeg of voeg extra info toe

5. **Klik:** **"Commit to main"** (of "Commit to master")

6. **Klik:** **"Push origin"** (rechtsboven, of Ctrl+P)

7. **Wacht tot push klaar is:**
   - Zie status: "Pushed to origin/main"
   - Vercel deployt automatisch!

### Optie B: Via Command Line

1. **Open PowerShell of Command Prompt**

2. **Navigeer naar website project:**
   ```powershell
   cd D:\MAUREEN\DEV\Seniorease\seniorease-project
   ```

3. **Controleer of bestand er is:**
   ```powershell
   dir public\Seniorease-Bibliotheek-Demo.apk
   ```
   - Moet bestand tonen (niet "bestand niet gevonden")

4. **Voeg toe aan git:**
   ```powershell
   git add public/Seniorease-Bibliotheek-Demo.apk
   ```

5. **Commit:**
   ```powershell
   git commit -m "Add: Demo APK met limiet van 10 items"
   ```

6. **Push naar GitHub:**
   ```powershell
   git push origin main
   ```
   (Of `git push origin master` als je master branch gebruikt)

7. **Wacht tot push klaar is:**
   - Zie: "Everything up-to-date" of "Pushed to origin/main"
   - Vercel deployt automatisch!

---

## ✅ Stap 5: Verifieer Upload

**Na push (wacht 1-2 minuten voor Vercel deploy):**

1. **Test QR Code:**
   - Ga naar: `https://seniorease.nl`
   - Scan QR code met Android telefoon
   - APK download start automatisch

2. **Test Directe Link:**
   - Ga naar: `https://seniorease.nl/api/download-demo-app`
   - APK download start

3. **Test op GitHub:**
   - Ga naar je GitHub repository
   - Check of `public/Seniorease-Bibliotheek-Demo.apk` er staat
   - Bestand moet zichtbaar zijn

---

## ⚠️ Veelgemaakte Fouten

### ❌ Fout 1: Verkeerde Bestandsnaam
**Probleem:** APK heet `Seniorease-Bibliotheek.apk` (zonder "Demo")
**Gevolg:** Website kan demo APK niet vinden
**Oplossing:** Hernoem naar `Seniorease-Bibliotheek-Demo.apk`

### ❌ Fout 2: APK in Verkeerde Folder
**Probleem:** APK staat niet in `public` folder
**Gevolg:** Website kan demo APK niet downloaden
**Oplossing:** Kopieer naar `public\Seniorease-Bibliotheek-Demo.apk`

### ❌ Fout 3: Niet Gecommit
**Probleem:** Bestand staat lokaal maar niet gecommit
**Gevolg:** Bestand staat niet op GitHub, website kan het niet vinden
**Oplossing:** Commit en push naar GitHub

### ❌ Fout 4: Verkeerde Branch
**Probleem:** Je commit naar verkeerde branch
**Gevolg:** Vercel deployt niet automatisch
**Oplossing:** Commit naar `main` of `master` branch

---

## 📋 Checklist

**Voor upload:**
- [ ] APK hernoemd naar: `Seniorease-Bibliotheek-Demo.apk`
- [ ] APK gekopieerd naar: `public\Seniorease-Bibliotheek-Demo.apk`
- [ ] Bestandsnaam is exact correct
- [ ] Bestand is niet leeg

**Na upload:**
- [ ] Bestand staat in GitHub repository
- [ ] Commit message is duidelijk
- [ ] Push is gelukt
- [ ] Vercel deploy is gestart (check Vercel dashboard)

**Na deploy:**
- [ ] QR code werkt op website
- [ ] Directe link werkt: `/api/download-demo-app`
- [ ] APK download start automatisch

---

## 🎯 Snelle Commando's (Copy-Paste)

**Als je command line gebruikt:**

```powershell
# Navigeer naar project
cd D:\MAUREEN\DEV\Seniorease\seniorease-project

# Controleer bestand
dir public\Seniorease-Bibliotheek-Demo.apk

# Voeg toe aan git
git add public/Seniorease-Bibliotheek-Demo.apk

# Commit
git commit -m "Add: Demo APK met limiet van 10 items"

# Push
git push origin main
```

---

## 🎉 Klaar!

Als alles goed is gegaan:
- ✅ Demo APK staat op GitHub
- ✅ Vercel deployt automatisch
- ✅ QR code werkt op website
- ✅ Download werkt voor gebruikers

**Probleem?** Controleer de checklist hierboven!










