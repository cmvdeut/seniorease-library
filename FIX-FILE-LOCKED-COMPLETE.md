# 🔧 Fix: File Locked - Complete Solution

## ⚠️ Probleem
Bestanden in `app/build` worden gebruikt door een ander proces:
- `classes.dex`
- `androidx.compose.runtime.lint.RuntimeIssueRegistry-*.jar`

---

## ✅ Oplossing: Sluit Alle Processen

### Stap 1: Sluit Android Studio Volledig

1. **Sluit Android Studio**
2. **Check Task Manager:**
   - Ctrl + Shift + Esc
   - Zoek naar: `Android Studio`
   - Zoek naar: `java.exe` (Gradle daemon)
   - **End Task** voor alle

### Stap 2: Stop Gradle Daemon

**In PowerShell:**
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
.\gradlew.bat --stop
```

**Wacht 30 seconden**

### Stap 3: Sluit File Explorer

**Als je Windows File Explorer hebt open met de `build` folder:**
- Sluit die folder
- Of sluit File Explorer volledig

### Stap 4: Wacht en Build Opnieuw

**Wacht 30 seconden**, dan:

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
.\gradlew.bat assembleDemoRelease
```

---

## 🔧 Alternatief: Build in Android Studio

**Als command line niet werkt:**

1. **Open Android Studio**
2. **Open project:** `D:\MAUREEN\DEV\SeniorEase-Library`
3. **Build → Clean Project**
4. **Wacht tot clean klaar is**
5. **Build → Rebuild Project**
6. **Of:** Build → Generate Signed Bundle / APK → APK → Demo Release

---

## 🔧 Alternatief: Verwijder Build Folder Handmatig

**Als niets werkt:**

1. **Sluit Android Studio**
2. **Stop Gradle:** `.\gradlew.bat --stop`
3. **Wacht 30 seconden**
4. **Verwijder build folder handmatig:**
   ```powershell
   Remove-Item -Recurse -Force "D:\MAUREEN\DEV\SeniorEase-Library\app\build" -ErrorAction SilentlyContinue
   ```
5. **Build opnieuw:**
   ```powershell
   .\gradlew.bat assembleDemoRelease
   ```

---

## 📋 Checklist

- [ ] Android Studio gesloten
- [ ] Gradle daemon gestopt (`.\gradlew.bat --stop`)
- [ ] File Explorer met build folder gesloten
- [ ] Wacht 30 seconden
- [ ] Build opnieuw

---

## 🚀 Snelle Fix

**1. Sluit Android Studio**
**2. Stop Gradle:** `.\gradlew.bat --stop`
**3. Wacht 30 seconden**
**4. Build opnieuw:** `.\gradlew.bat assembleDemoRelease`

**Of build in Android Studio zelf!**
