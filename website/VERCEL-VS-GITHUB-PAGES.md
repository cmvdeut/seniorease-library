# ⚠️ Belangrijk: Vercel vs GitHub Pages

## ❌ We gebruiken NIET GitHub Pages

**GitHub Pages Settings zijn NIET relevant voor onze setup!**

- GitHub → Repository → Settings → Pages
- Dit is voor GitHub Pages (niet wat we gebruiken)
- **Negeer deze settings**

---

## ✅ We gebruiken Vercel

**Vercel heeft zijn eigen dashboard en settings:**

### Waar moet je zijn:

1. **Ga naar:** https://vercel.com/dashboard
2. **Log in** met je account
3. **Klik op project:** `seniorease-library`

### Vercel Settings:

**Settings → Git:**
- **Git Repository:** `cmvdeut/seniorease-library`
- **Production Branch:** `master` (of `main`)
- **Root Directory:** `website` ⚠️ (BELANGRIJK!)
- **Automatic Deployments:** Enabled ✅

**Settings → Environment Variables:**
- `STRIPE_SECRET_KEY`
- `STRIPE_PRODUCT_ID`

**Deployments:**
- Hier zie je alle deployments
- Hier kun je handmatig redeployen

---

## 🔍 Waarom de Verwarring?

**GitHub Pages:**
- Is een andere service van GitHub
- Gebruikt andere settings
- **Niet wat we gebruiken**

**Vercel:**
- Is een aparte service
- Heeft zijn eigen dashboard
- **Dit is wat we gebruiken**

---

## ✅ Wat te Doen

**Als GitHub → Vercel niet werkt:**

1. **Ga naar Vercel Dashboard** (NIET GitHub Pages!)
   - https://vercel.com/dashboard

2. **Check Settings → Git:**
   - Root Directory = `website`?
   - Production Branch = `master`?
   - Automatic Deployments = Enabled?

3. **Handmatig Redeploy:**
   - Deployments → Redeploy

4. **Als niets werkt:**
   - Settings → Git → Disconnect
   - Reconnect repository
   - Check ALLE instellingen

---

**Gebruik Vercel Dashboard, niet GitHub Pages Settings!**
