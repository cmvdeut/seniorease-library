# 🔧 Gradle Locked Files Fix

## ⚠️ Error: `Unable to delete directory`

Bestanden zijn gelocked door een proces (Android Studio, Gradle daemon, etc.)

---

## ✅ Oplossingen

### Optie 1: Stop Gradle Daemon (Snelste)

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
.\gradlew.bat --stop
.\gradlew.bat assembleDemoRelease
```

### Optie 2: Sluit Android Studio

1. **Sluit Android Studio volledig**
2. **Wacht 5 seconden**
3. **Build via command line:**
   ```powershell
   .\gradlew.bat assembleDemoRelease
   ```

### Optie 3: Skip Clean (Aanbevolen)

**Clean is optioneel** - je kunt direct builden zonder clean:

```powershell
.\gradlew.bat assembleDemoRelease
```

Dit werkt meestal prima en is sneller!

### Optie 4: Force Kill Process (Als niets werkt)

1. **Open Task Manager** (Ctrl+Shift+Esc)
2. **Zoek naar:**
   - `java.exe` (Gradle daemon)
   - `Android Studio`
3. **End Task** voor deze processen
4. **Build opnieuw**

---

## 💡 Tip

**Clean is niet altijd nodig!** 
- Alleen nodig als je build problemen hebt
- Direct builden werkt meestal prima: `.\gradlew.bat assembleDemoRelease`

---

## ✅ Na Fix

APK staat in: `app\build\outputs\apk\demo\release\app-demo-release.apk`
