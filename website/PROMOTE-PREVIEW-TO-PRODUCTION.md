# 🚀 Promote Preview naar Production - Snelle Fix

## ⚠️ Probleem Gevonden!

**In Vercel Deployments:**
- ✅ Deployment `Ah2eRAi7P` heeft commit `7c1b0a7` (met debug logs)
- ❌ Maar dit is een **Preview** deployment, niet Production!
- ❌ Huidige Production deployment (`HmAYwYr6u`) is een oude redeploy zonder de nieuwste code

---

## ✅ Oplossing: Promote Preview naar Production

### Stap 1: Promote Deployment

**In Vercel Dashboard:**

1. **Ga naar:** Deployments tab
2. **Zoek deployment:** `Ah2eRAi7P`
   - Dit heeft commit: `7c1b0a7 Add session details debug logs...`
   - Status: Preview (niet Production)
3. **Klik op de deployment** `Ah2eRAi7P`
4. **Klik op de knop:** "Promote to Production" (of "Promote")
5. **Bevestig:** "Promote to Production"

**Of:**

1. **Klik op de 3 puntjes** (⋯) naast deployment `Ah2eRAi7P`
2. **Kies:** "Promote to Production"
3. **Bevestig**

### Stap 2: Verifieer

**Na promote:**

1. **Check deployment status:**
   - Deployment `Ah2eRAi7P` zou nu "Production" moeten zijn
   - Moet "Current" tag hebben
   - Status = Ready

2. **Test API:**
   ```powershell
   cd D:\MAUREEN\DEV\SeniorEase-Library\website
   .\test-api-email.ps1 -Email "cmvdeut@gmail.com"
   ```

3. **Check Vercel Logs:**
   - Klik op deployment `Ah2eRAi7P`
   - Functions tab → Zoek `[DEBUG]` logs
   - Je zou nu moeten zien:
     - `[DEBUG] PRICE_ID: ...`
     - `[DEBUG] Normalized email: ...`
     - `[DEBUG] Session from search: ...`

---

## 🔧 Alternatief: Trigger Nieuwe Production Deployment

**Als promote niet werkt, force een nieuwe deployment:**

### Stap 1: Check Production Branch

**Settings → Git:**
- **Production Branch:** Moet `master` zijn
- **Automatic Deployments:** Enabled

### Stap 2: Push naar Master

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
echo "" >> website/README.md
git add website/README.md
git commit -m "Trigger production deployment"
git push origin master
```

**Wacht 2-3 minuten:**
- Nieuwe deployment zou moeten verschijnen
- Moet automatisch naar Production gaan (als Production Branch = `master`)

---

## 📋 Checklist

- [ ] Vercel → Deployments → Zoek `Ah2eRAi7P`
- [ ] Klik op deployment → "Promote to Production"
- [ ] Check: Deployment is nu Production + Current
- [ ] Test API en check voor `[DEBUG]` logs
- [ ] Als promote niet werkt: Check Production Branch = `master`
- [ ] Push naar master om nieuwe deployment te triggeren

---

## 🎯 Waarom Dit Gebeurde

**Mogelijke oorzaken:**

1. **Production Branch was niet ingesteld:**
   - Vercel maakt Preview deployments voor alle branches
   - Zonder Production Branch gaat het niet automatisch naar Production

2. **Handmatig redeploy gebruikt oude versie:**
   - "Redeploy" gebruikt de oude deployment, niet de nieuwste code
   - Daarom heeft `HmAYwYr6u` geen commit `7c1b0a7`

---

## 🚀 Snelle Fix

**1. Promote Preview naar Production:**
   - Deployments → `Ah2eRAi7P` → "Promote to Production"

**2. Check Settings:**
   - Settings → Git → Production Branch = `master`

**Dit zou de nieuwste code met debug logs naar Production moeten brengen!**
