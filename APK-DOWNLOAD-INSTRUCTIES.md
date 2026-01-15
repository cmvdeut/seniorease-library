# 📱 APK Download via Website - Handleiding

## ✅ APK is Gebouwd!

De demo release APK is succesvol gebouwd en klaar voor distributie.

---

## 📦 Stap 1: Locatie van de APK

De APK staat in:
```
app/build/outputs/apk/demo/release/app-demo-release.apk
```

**Volledige pad:**
```
D:\MAUREEN\DEV\SeniorEase-Library\app\build\outputs\apk\demo\release\app-demo-release.apk
```

**APK Details:**
- **Bestandsnaam:** `app-demo-release.apk`
- **Grootte:** ~30 MB
- **Versie:** 1.0.4 (Demo)
- **Package:** `com.seniorease.library.demo`

---

## 🌐 Stap 2: APK Uploaden naar Website

### Optie A: Via FTP/File Manager

1. **Log in op je webhosting** (bijv. cPanel, Plesk, of FTP)
2. **Upload de APK** naar een publiek toegankelijke map, bijvoorbeeld:
   - `/public_html/downloads/`
   - `/public_html/apps/`
   - Of een andere map die je wilt gebruiken

3. **Zorg dat de APK leesbaar is:**
   - Bestandsrechten: `644` (readable voor iedereen)
   - Map rechten: `755` (executable voor iedereen)

### Optie B: Via GitHub Releases (Gratis)

1. **Maak een GitHub repository** (of gebruik bestaande)
2. **Ga naar Releases → Create a new release**
3. **Upload de APK** als bijlage
4. **Kopieer de directe download link** (bijv: `https://github.com/username/repo/releases/download/v1.0.4/app-demo-release.apk`)

### Optie C: Via Google Drive / Dropbox

1. **Upload APK naar Google Drive of Dropbox**
2. **Maak link publiek** (share → anyone with link)
3. **Gebruik directe download link** (zie onderstaand voor Google Drive)

**Google Drive Directe Download Link:**
```
https://drive.google.com/uc?export=download&id=FILE_ID
```
(Vervang `FILE_ID` met het ID uit je share link)

---

## 🔗 Stap 3: Download Link Maken

### Eenvoudige HTML Download Pagina

Maak een bestand `download.html` op je website:

```html
<!DOCTYPE html>
<html lang="nl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SeniorEase Library - Download</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            text-align: center;
        }
        .download-button {
            display: inline-block;
            background-color: #8B4513;
            color: white;
            padding: 15px 30px;
            text-decoration: none;
            border-radius: 5px;
            font-size: 18px;
            margin: 20px 0;
        }
        .download-button:hover {
            background-color: #654321;
        }
        .qr-code {
            margin: 30px 0;
        }
        .info {
            background-color: #f0f0f0;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
            text-align: left;
        }
    </style>
</head>
<body>
    <h1>📱 SeniorEase Library</h1>
    <h2>Download de App</h2>
    
    <div class="info">
        <h3>Installatie Instructies:</h3>
        <ol>
            <li>Download de APK door op de knop hieronder te klikken</li>
            <li>Open de gedownloade APK op je Android apparaat</li>
            <li>Als je een melding krijgt over "Onbekende bronnen", ga naar:
                <strong>Instellingen → Beveiliging → Onbekende bronnen toestaan</strong>
            </li>
            <li>Installeer de app</li>
        </ol>
    </div>
    
    <a href="https://jouw-website.com/downloads/app-demo-release.apk" 
       class="download-button" 
       download="SeniorEase-Library-Demo.apk">
        📥 Download APK
    </a>
    
    <div class="qr-code">
        <h3>Of scan de QR code:</h3>
        <img src="qr-code.png" alt="QR Code voor download" style="max-width: 300px;">
    </div>
    
    <div class="info">
        <p><strong>Versie:</strong> 1.0.4 (Demo)</p>
        <p><strong>Grootte:</strong> ~XX MB</p>
        <p><strong>Android:</strong> 7.0+ vereist</p>
    </div>
</body>
</html>
```

**Vervang:**
- `https://jouw-website.com/downloads/app-demo-release.apk` met je eigen download link
- `qr-code.png` met de QR code afbeelding (zie volgende stap)

---

## 📱 Stap 4: QR Code Genereren

### Optie A: Online QR Code Generator (Aanbevolen)

1. **Ga naar een QR code generator:**
   - https://www.qr-code-generator.com/
   - https://qr-code-generator.com/
   - https://www.qrcode-monkey.com/

2. **Kies "URL" of "Website"**
3. **Voer je download link in:**
   ```
   https://jouw-website.com/downloads/app-demo-release.apk
   ```
4. **Download de QR code** als PNG of SVG
5. **Upload naar je website** (bijv. `qr-code.png`)

### Optie B: Via Python Script (Lokaal)

Als je Python hebt geïnstalleerd:

```python
import qrcode

# Je download URL
url = "https://jouw-website.com/downloads/app-demo-release.apk"

# Maak QR code
qr = qrcode.QRCode(
    version=1,
    error_correction=qrcode.constants.ERROR_CORRECT_L,
    box_size=10,
    border=4,
)
qr.add_data(url)
qr.make(fit=True)

# Maak afbeelding
img = qrcode.make(url)
img.save("qr-code.png")
```

**Installeer eerst:**
```bash
pip install qrcode[pil]
```

### Optie C: Via Command Line (met qrencode)

Als je `qrencode` hebt geïnstalleerd:

```bash
qrencode -o qr-code.png "https://jouw-website.com/downloads/app-demo-release.apk"
```

---

## 🔒 Stap 5: Beveiliging (Optioneel maar Aanbevolen)

### HTTPS Gebruiken

Zorg dat je website HTTPS gebruikt (SSL certificaat). Dit is belangrijk omdat:
- Android waarschuwt voor downloads via HTTP
- Gebruikers vertrouwen HTTPS meer

### APK Verificatie

Je kunt ook een SHA-256 hash van de APK genereren voor verificatie:

```bash
# Windows PowerShell:
Get-FileHash -Path "app-demo-release.apk" -Algorithm SHA256

# Of via online tool uploaden
```

Toon deze hash op je download pagina zodat gebruikers kunnen verifiëren dat de APK niet is aangepast.

---

## 📋 Checklist

- [ ] APK is gebouwd (`app-demo-release.apk`)
- [ ] APK is geüpload naar website
- [ ] Download link werkt (test in browser)
- [ ] QR code is gegenereerd
- [ ] QR code is geüpload naar website
- [ ] Download pagina is gemaakt
- [ ] Test download op Android apparaat
- [ ] Test QR code scan op Android apparaat

---

## 🧪 Testen

1. **Test de download link** in een browser op je computer
2. **Test op Android:**
   - Scan de QR code met je telefoon camera
   - Of open de download link in Chrome op je telefoon
   - Download en installeer de APK
3. **Verifieer dat de app werkt** na installatie

---

## 💡 Tips

- **Gebruik een duidelijke bestandsnaam:** `SeniorEase-Library-Demo-v1.0.4.apk`
- **Zet versie nummer in bestandsnaam** voor makkelijke updates
- **Maak een changelog** op je download pagina
- **Test regelmatig** of de download link nog werkt

---

**Succes met het hosten van je APK! 🚀**
