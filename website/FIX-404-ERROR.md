# 🔧 Fix 404 Error in Vercel

## Probleem

Je ziet een 404 error omdat Vercel de bestanden niet kan vinden. Dit komt omdat de **Root Directory** niet correct is ingesteld.

## ✅ Oplossing: Root Directory Aanpassen

### Stap 1: Ga naar Project Settings

1. In Vercel dashboard → **Klik op je project** `seniorease-library`
2. Ga naar **Settings** (bovenaan)
3. Klik op **General** (in het linker menu)

### Stap 2: Pas Root Directory Aan

1. Scroll naar beneden naar **"Root Directory"**
2. Klik op **"Edit"**
3. Voer in: `website`
4. Klik **"Save"**

### Stap 3: Redeploy

1. Ga terug naar **Deployments** tab
2. Klik op de **3 puntjes** (⋯) naast je laatste deployment
3. Kies **"Redeploy"**
4. Of: **Push opnieuw naar GitHub** (Vercel deployt automatisch)

---

## ✅ Alternatief: Via Project Settings

1. **Settings** → **General**
2. Zoek **"Root Directory"**
3. Zet op: `website`
4. **Save**
5. Vercel redeployt automatisch

---

## 🧪 Testen

Na de redeploy:
- Ga naar: `https://seniorease-library.vercel.app`
- Je zou nu de download pagina moeten zien! 🎉

---

**Dit is de meest voorkomende fout - de Root Directory moet op `website` staan!**
