# 🔧 Force Deployment - Nieuwste Commit Niet in Vercel

## ⚠️ Probleem
- Nieuwste commit (`7c1b0a7`) staat niet in Vercel deployments
- GitHub triggert Vercel niet automatisch
- Handmatig redeployen gebruikt oude code

---

## ✅ Oplossing 1: Reconnect Vercel aan GitHub

### Stap 1: Disconnect in Vercel

1. **Ga naar:** https://vercel.com/dashboard
2. **Klik op project:** `seniorease-library`
3. **Ga naar:** **Settings** → **Git**
4. **Klik:** "Disconnect" (onderaan)
5. **Bevestig:** "Disconnect Repository"

### Stap 2: Reconnect GitHub

1. **Klik:** "Connect Git Repository"
2. **Selecteer:** `cmvdeut/seniorease-library`
3. **Klik:** "Import"
4. **BELANGRIJK - Check instellingen:**
   - **Root Directory:** `website` ⚠️
   - **Framework Preset:** `Other`
   - **Build Command:** (leeg)
   - **Output Directory:** `.`
   - **Production Branch:** `master` ⚠️
5. **Klik:** "Deploy"

### Stap 3: Verifieer

**Na deployment:**
- Check deployment details → Source → Commit hash
- Moet `7c1b0a7` of nieuwer zijn
- Check Functions logs voor `[DEBUG]` logs

---

## ✅ Oplossing 2: Trigger Nieuwe Deployment via Git Push

**Als reconnect niet werkt, force een nieuwe deployment:**

### Stap 1: Maak Kleine Wijziging

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
# Voeg een lege regel toe aan een bestand
echo "" >> website/README.md
```

### Stap 2: Commit en Push

```powershell
git add website/README.md
git commit -m "Trigger Vercel deployment - force update"
git push origin master
```

### Stap 3: Check Vercel

**Wacht 2-3 minuten:**
- Ga naar Vercel Dashboard → Deployments
- Nieuwe deployment zou moeten verschijnen
- Check commit hash in deployment details

---

## ✅ Oplossing 3: Check GitHub Webhook

**Als niets werkt, check GitHub webhook:**

1. **Ga naar:** https://github.com/cmvdeut/seniorease-library/settings/hooks
2. **Zoek webhook met URL:** `vercel.com` of `vercel.app`
3. **Check status:**
   - Moet **Active** zijn (groen vinkje)
   - **Recent deliveries** moeten successvol zijn

**Als webhook niet bestaat of faalt:**
- Vercel is niet correct gekoppeld
- Gebruik Oplossing 1 (Reconnect)

**Als webhook bestaat maar faalt:**
- Klik op webhook → Check "Recent deliveries"
- Klik op laatste delivery → Check "Response"
- Mogelijk probleem met Vercel token → Reconnect in Vercel

---

## ✅ Oplossing 4: Check Vercel Project Settings

**Check deze instellingen in Vercel:**

1. **Settings** → **Git**
   - **Git Repository:** Moet `cmvdeut/seniorease-library` zijn
   - **Production Branch:** Moet `master` zijn
   - **Automatic Deployments:** Moet **Enabled** zijn

2. **Settings** → **General**
   - **Root Directory:** Moet `website` zijn (niet `.` of leeg)

**Als iets verkeerd is:**
- Pas aan en klik "Save"
- Redeploy handmatig

---

## 🧪 Test Na Fix

**Na reconnect of nieuwe deployment:**

1. **Test API:**
   ```powershell
   cd D:\MAUREEN\DEV\SeniorEase-Library\website
   .\test-api-email.ps1 -Email "cmvdeut@gmail.com"
   ```

2. **Check Vercel Logs:**
   - **Deployments** → Laatste deployment → **Functions** tab
   - Zoek naar `[DEBUG]` logs:
     - `[DEBUG] PRICE_ID: ...`
     - `[DEBUG] Normalized email: ...`
     - `[DEBUG] Session from search: ...`

**Als je `[DEBUG]` logs ziet:**
- ✅ Nieuwste code is gedeployed!

**Als je geen `[DEBUG]` logs ziet:**
- ❌ Oudere versie staat nog live
- Check commit hash in deployment
- Probeer opnieuw reconnect

---

## 📋 Checklist

- [ ] Vercel → Settings → Git → Disconnect
- [ ] Vercel → Connect Git Repository → Selecteer `cmvdeut/seniorease-library`
- [ ] Check: Root Directory = `website`
- [ ] Check: Production Branch = `master`
- [ ] Deploy
- [ ] Check deployment commit hash = `7c1b0a7` of nieuwer
- [ ] Test API en check voor `[DEBUG]` logs

---

## 🚀 Snelle Fix (Aanbevolen)

**Reconnect Vercel aan GitHub:**
1. Settings → Git → Disconnect
2. Connect Git Repository → Selecteer repo
3. Root Directory = `website`
4. Production Branch = `master`
5. Deploy

**Dit zou automatisch de nieuwste code moeten deployen!**
