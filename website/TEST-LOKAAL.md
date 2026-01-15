# 🧪 Website Lokaal Testen

## ✅ Wat is Aangepast

1. **Download button:** Nu met volledige URL `https://www.seniorease.eu/downloads/app-demo-release.apk`
2. **Android-only vermelding:** Toegevoegd op meerdere plekken
3. **QR code tekst:** Aangepast naar "Scan this QR code with your Android phone"
4. **QR code URL:** Moet worden gegenereerd met `https://www.seniorease.eu/downloads/app-demo-release.apk`

---

## 🧪 Lokaal Testen

### Stap 1: Start Lokale Server

**Optie A: Via Python (Eenvoudigste)**

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
python -m http.server 8000
```

**Optie B: Via Vercel Dev**

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
vercel dev
```

### Stap 2: Open in Browser

**Op PC:**
- Open: `http://localhost:8000` (of `http://localhost:3000` voor Vercel)

**Op Android Telefoon:**
- Zorg dat telefoon en PC op hetzelfde WiFi netwerk zitten
- Vind je PC IP adres:
  ```powershell
  ipconfig
  # Zoek naar "IPv4 Address" (bijv. 192.168.1.100)
  ```
- Open op telefoon: `http://192.168.1.100:8000` (vervang met jouw IP)

---

## ✅ Test Checklist

### Op PC:
- [ ] Website laadt correct
- [ ] Download button is zichtbaar
- [ ] Android-only vermelding is zichtbaar
- [ ] QR code is zichtbaar
- [ ] Download button werkt (download start)

### Op Android Telefoon:
- [ ] Website laadt correct
- [ ] Download button is zichtbaar en klikbaar
- [ ] Download button start download direct (niet alleen opent link)
- [ ] QR code is scannable
- [ ] QR code leidt naar download
- [ ] Android-only vermelding is zichtbaar

---

## 🔧 QR Code Genereren

**Als QR code nog niet correct is:**

1. **Open:** `website/generate-qr.html` in browser
2. **Check URL:** Moet zijn `https://www.seniorease.eu/downloads/app-demo-release.apk`
3. **Klik:** "Genereer QR Code"
4. **Klik:** "Download QR Code"
5. **Vervang:** `website/qr-code-apk-download.png` met nieuwe QR code

---

## 📋 Wat te Testen

### Download Button op Mobiel:
- **Verwacht:** Direct download start (niet alleen link openen)
- **Als het niet werkt:** Check of `download` attribute werkt op mobiel browsers

### QR Code:
- **Scan met telefoon camera**
- **Verwacht:** Opent download link direct
- **Als het niet werkt:** Check QR code URL

### Android-Only Vermelding:
- **Moet zichtbaar zijn op:**
  - Onder download button
  - Bij QR code
  - In footer

---

## 🚀 Na Lokaal Testen

**Als alles werkt:**
1. Commit changes
2. Push naar GitHub
3. Vercel deployt automatisch

**Als er problemen zijn:**
- Noteer wat niet werkt
- Fix en test opnieuw

---

**Start lokale server en test op PC en Android telefoon!**
