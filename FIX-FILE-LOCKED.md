# 🔧 Fix: File Locked by Another Process

## ⚠️ Probleem
```
Het proces heeft geen toegang tot het bestand omdat het door een ander proces wordt gebruikt
```

Dit gebeurt wanneer:
- Android Studio is open en gebruikt bestanden
- Gradle daemon draait nog
- Vorige build is nog niet klaar

---

## ✅ Oplossing 1: Stop Gradle Daemon

**In PowerShell:**

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
.\gradlew.bat --stop
```

**Wacht 10 seconden**, dan build opnieuw:

```powershell
.\gradlew.bat assembleDemoRelease
```

---

## ✅ Oplossing 2: Sluit Android Studio

**Als Oplossing 1 niet werkt:**

1. **Sluit Android Studio volledig**
2. **Wacht 30 seconden**
3. **Build via command line:**
   ```powershell
   cd D:\MAUREEN\DEV\SeniorEase-Library
   .\gradlew.bat assembleDemoRelease
   ```

---

## ✅ Oplossing 3: Clean Build

**Als bestanden nog steeds gelocked zijn:**

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
.\gradlew.bat clean
.\gradlew.bat assembleDemoRelease
```

---

## ✅ Oplossing 4: Check Running Processes

**Als niets werkt:**

1. **Open Task Manager** (Ctrl + Shift + Esc)
2. **Zoek naar:**
   - `java.exe` (Gradle daemon)
   - `Android Studio`
   - `gradle`
3. **End Task** voor deze processen
4. **Build opnieuw**

---

## 🚀 Snelle Fix

**1. Stop Gradle:**
   ```powershell
   .\gradlew.bat --stop
   ```

**2. Wacht 10 seconden**

**3. Build opnieuw:**
   ```powershell
   .\gradlew.bat assembleDemoRelease
   ```

---

**Gradle daemon is gestopt. Wacht 10 seconden en build opnieuw!**
