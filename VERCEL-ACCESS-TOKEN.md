# 🔑 Vercel Access Token voor VS Code Extension

## ⚠️ Error
"Please set your Vercel Access Token in the extension settings `vercelVSCode.accessToken`."

---

## ✅ Dit is Optioneel!

**De VS Code extension is NIET nodig voor deployment!**

Deployment werkt al via:
- ✅ **GitHub push** → Vercel deployt automatisch
- ✅ **Vercel Dashboard** → Manual deploy

---

## 🔧 Als je de Extension Wilt Gebruiken

### Stap 1: Vercel Access Token Aanmaken

1. **Ga naar:** https://vercel.com/account/tokens
2. **Klik:** "Create Token"
3. **Geef naam:** "VS Code Extension"
4. **Kopieer de token** (je ziet hem maar 1x!)

### Stap 2: Token Toevoegen aan VS Code

1. **Open VS Code Settings:**
   - `Ctrl + ,` (of `Cmd + ,` op Mac)
   - Of: File → Preferences → Settings

2. **Zoek naar:** `vercelVSCode.accessToken`

3. **Plak de token** in het veld

4. **Of via settings.json:**
   ```json
   {
     "vercelVSCode.accessToken": "jouw-token-hier"
   }
   ```

---

## 💡 Aanbeveling

**Je hoeft dit NIET te doen!**

Deployment werkt al perfect via:
- Git push → Vercel deployt automatisch
- Vercel Dashboard voor manual deploys

De extension is alleen handig als je:
- Direct vanuit VS Code wilt deployen
- Preview deployments wilt bekijken
- Logs wilt bekijken in VS Code

---

## ✅ Alternatief: Extension Uitschakelen

Als je de extension niet gebruikt:
1. **Uninstall:** Extensions → Vercel → Uninstall
2. **Of:** Ignoreer de error (hij doet niets)

---

**Tip:** Deployment werkt al via GitHub push, dus deze error kan je negeren!
