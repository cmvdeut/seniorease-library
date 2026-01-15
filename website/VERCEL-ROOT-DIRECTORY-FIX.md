# 🔧 Fix 404 Error - Root Directory Instellen

## ⚠️ Probleem: 404 NOT_FOUND

Dit betekent dat Vercel de `index.html` niet kan vinden omdat de Root Directory niet correct is ingesteld.

## ✅ Oplossing: Root Directory op `website` zetten

### Stap 1: Ga naar Project Settings

1. **In Vercel dashboard:**
   - Klik op je project **`seniorease-library`**
   - Klik op **Settings** (bovenaan, naast "Deployments")
   - In het linker menu, klik op **General**

### Stap 2: Pas Root Directory Aan

1. **Scroll naar beneden** naar de sectie **"Root Directory"**
2. **Klik op "Edit"** (rechts van Root Directory)
3. **Voer in:** `website`
4. **Klik "Save"**

### Stap 3: Redeploy

Na het opslaan gebeurt er automatisch een redeploy. Of:

1. Ga naar **Deployments** tab
2. Klik op de **3 puntjes** (⋯) naast je laatste deployment
3. Kies **"Redeploy"**

---

## ✅ Alternatief: Via Project Settings Direct

1. **Settings** → **General**
2. Zoek **"Root Directory"** (onder "Build & Development Settings")
3. Klik **"Edit"**
4. Zet op: `website`
5. **Save**

Vercel redeployt automatisch!

---

## 🧪 Testen

Na de redeploy (1-2 minuten):
- Ga naar: `https://seniorease-library.vercel.app`
- Je zou nu de download pagina moeten zien! 🎉

---

## 📋 Checklist

- [ ] Root Directory staat op `website`
- [ ] Redeploy is voltooid
- [ ] Website laadt zonder 404 error
- [ ] Download knop werkt
- [ ] QR code wordt getoond

---

**Dit is de meest voorkomende fout - de Root Directory moet op `website` staan!**
