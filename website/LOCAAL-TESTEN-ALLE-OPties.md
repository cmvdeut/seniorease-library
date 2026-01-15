# 🧪 Website Lokaal Testen - Alle Opties

## ⚠️ Probleem
Localhost werkt niet - server start niet of is niet bereikbaar.

---

## ✅ Optie 1: Python HTTP Server (Eenvoudigste)

**Check of Python geïnstalleerd is:**
```powershell
python --version
```

**Als Python werkt:**
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
python -m http.server 8000
```

**Open in browser:**
- `http://localhost:8000`

**Als port 8000 bezet is:**
```powershell
python -m http.server 8001
```

---

## ✅ Optie 2: Vercel Dev (Aanbevolen)

**Als Vercel CLI geïnstalleerd is:**
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
vercel dev
```

**Open in browser:**
- `http://localhost:3000`

**Voordeel:** Simuleert Vercel productie omgeving

---

## ✅ Optie 3: Node.js http-server

**Installeer (eenmalig):**
```powershell
npm install -g http-server
```

**Start server:**
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
http-server -p 8000
```

**Of via npx (zonder installatie):**
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
npx -y http-server -p 8000
```

---

## ✅ Optie 4: PowerShell SimpleHTTPServer

**Als Python niet werkt, gebruik PowerShell script:**
```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
.\START-LOKALE-SERVER.ps1
```

Dit script probeert automatisch:
1. Python
2. Python3
3. Node.js http-server
4. Vercel Dev

---

## 🔧 Troubleshooting

### "Port already in use"
**Oplossing:**
- Gebruik andere port: `python -m http.server 8001`
- Of stop proces op port 8000:
  ```powershell
  netstat -ano | findstr :8000
  taskkill /PID [PID-nummer] /F
  ```

### "Python not found"
**Oplossing:**
- Installeer Python: https://www.python.org/downloads/
- Of gebruik Vercel Dev: `vercel dev`

### "Cannot connect to localhost"
**Check:**
- Is server gestart? (zie output)
- Welke port gebruikt server?
- Firewall blokkeert niet?

### "404 Not Found"
**Check:**
- Ben je in de `website` folder?
- Bestaat `index.html`?
- Check URL: `http://localhost:8000/index.html`

---

## 📱 Test op Android Telefoon

**Na server start:**

1. **Vind PC IP adres:**
   ```powershell
   ipconfig
   # Zoek "IPv4 Address" (bijv. 192.168.1.100)
   ```

2. **Zorg dat telefoon opzelfde WiFi zit**

3. **Open op telefoon:**
   - `http://192.168.1.100:8000` (vervang met jouw IP)

4. **Test:**
   - Download button werkt
   - QR code is zichtbaar
   - Android-only tekst is zichtbaar

---

## 🚀 Snelle Fix

**Probeer deze volgorde:**

1. **Python:**
   ```powershell
   cd website
   python -m http.server 8000
   ```

2. **Als dat niet werkt, Vercel Dev:**
   ```powershell
   cd website
   vercel dev
   ```

3. **Als dat niet werkt, PowerShell script:**
   ```powershell
   cd website
   .\START-LOKALE-SERVER.ps1
   ```

---

**Welke foutmelding krijg je precies?** Dan kan ik specifieker helpen!
