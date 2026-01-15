# 🔍 Check Deployment Version

## ⚠️ Probleem
- Error: `stripe.checkout.sessions.search is not a function`
- Dit betekent dat **oude code** nog draait in production
- De fix met `list()` is wel gepusht, maar staat nog niet op production

---

## ✅ Oplossing: Check en Promote Nieuwste Deployment

### Stap 1: Check Welke Deployment Op Production Staat

**In Vercel Dashboard:**

1. **Ga naar:** Deployments tab
2. **Zoek deployment met "Current" tag** (Production)
3. **Klik op die deployment**
4. **Check:** Source → Commit hash

**Verwacht:**
- Nieuwste commit: `8e0a534` (Fix syntax errors...)
- Of: `94a94a3` (Fix: Replace search() with list()...)

**Als je een oudere commit ziet:**
- Oude code draait nog
- Je moet de nieuwste deployment promoten

### Stap 2: Zoek Nieuwste Deployment

**In Vercel Dashboard:**

1. **Deployments tab**
2. **Zoek deployment met commit:** `8e0a534` of `94a94a3`
3. **Check status:**
   - Als het **Preview** is → Promote naar Production
   - Als het **Production** is → Check of het "Current" is

### Stap 3: Promote Nieuwste Deployment

**Als nieuwste deployment Preview is:**

1. **Klik op deployment** (commit `8e0a534` of `94a94a3`)
2. **Klik:** "Promote to Production"
3. **Bevestig**
4. **Wacht 1-2 minuten**

### Stap 4: Verifieer

**Na promote:**

1. **Test API opnieuw:**
   ```powershell
   cd D:\MAUREEN\DEV\SeniorEase-Library\website
   .\test-api-email.ps1 -Email "cmvdeut@gmail.com"
   ```

2. **Check Vercel logs:**
   - Deployments → Nieuwste deployment → Functions tab
   - Je zou **GEEN** `search is not a function` error moeten zien
   - Je zou `[DEBUG] Checked X total session(s)...` moeten zien

---

## 🔧 Alternatief: Wacht op Automatische Deployment

**Als Production Branch = `master` is ingesteld:**

- Nieuwe push naar `master` zou automatisch naar Production moeten gaan
- Wacht 2-3 minuten na push
- Check of nieuwe deployment automatisch "Production" + "Current" is geworden

**Als dit niet gebeurt:**
- Check Settings → Git → Production Branch = `master`
- Check Settings → Git → Automatic Deployments = Enabled

---

## 📋 Checklist

- [ ] Check welke commit in huidige Production deployment staat
- [ ] Zoek deployment met commit `8e0a534` of `94a94a3`
- [ ] Als Preview → Promote naar Production
- [ ] Test API en check logs voor nieuwe code
- [ ] Geen `search is not a function` error meer

---

## 🚀 Snelle Fix

**1. Check Production deployment commit hash**
**2. Zoek deployment met commit `8e0a534`**
**3. Als Preview → Promote to Production**
**4. Test API opnieuw**

**Dit zou de nieuwste code met `list()` naar Production moeten brengen!**
