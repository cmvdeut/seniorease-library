# 🔑 Stripe Secret Key Toevoegen aan Vercel - Stap voor Stap

## ⚠️ BELANGRIJK: Dit moet je zelf doen in Vercel Dashboard

Ik kan de secret key niet automatisch toevoegen - dit moet via de Vercel website.

## 📋 Stap-voor-Stap Instructies

### Stap 1: Ga naar Vercel Dashboard
1. Open je browser
2. Ga naar: **https://vercel.com/dashboard**
3. Log in met je account

### Stap 2: Selecteer je Project
1. Klik op je project: **seniorease-library** (of de naam die je hebt gebruikt)
2. Je ziet nu de project overview

### Stap 3: Ga naar Settings
1. Klik op **Settings** (bovenaan in het menu, naast "Deployments", "Analytics", etc.)
2. In het linker menu zie je verschillende opties
3. Klik op **Environment Variables** (in het linker menu onder "General")

### Stap 4: Voeg de Key Toe
1. Klik op de knop **Add New** (of **Add** - rechtsboven)
2. Er opent een formulier met 3 velden:
   
   **Key:**
   ```
   STRIPE_SECRET_KEY
   ```
   (Type dit exact, zonder spaties)
   
   **Value:**
   ```
   [Je Stripe Secret Key - haal op uit Stripe Dashboard]
   ```
   (Kopieer je secret key uit Stripe Dashboard → Developers → API keys)
   
   **Environment:**
   - ✅ **Production** (vink aan)
   - ✅ **Preview** (vink aan)
   - ✅ **Development** (vink aan)
   
   **BELANGRIJK:** Vink alle 3 aan!

3. Klik op **Save**

### Stap 5: Verifieer
Na het opslaan zie je de key in de lijst:
- **Key:** `STRIPE_SECRET_KEY`
- **Environments:** Production, Preview, Development
- **Value:** (verborgen, maar aanwezig)

### Stap 6: Redeploy
**BELANGRIJK:** Na het toevoegen van een environment variable moet je redeployen!

1. Ga naar **Deployments** (in het linker menu)
2. Klik op de **3 puntjes** (⋯) naast de laatste deployment
3. Klik op **Redeploy**
4. Wacht 2-3 minuten tot deployment klaar is

**OF:**

1. Ga naar **Deployments**
2. Klik op **Redeploy** (grote knop rechtsboven)
3. Selecteer de laatste commit
4. Klik **Redeploy**
5. Wacht 2-3 minuten

## ✅ Testen Na Deployment

Na deployment (2-3 minuten), test de API:

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library\website
.\test-api-online.ps1
```

**Verwacht:** `{ "paid": false }` (omdat test@example.com geen betaling heeft)

Als je dit ziet, werkt alles! ✅

## 🐛 Troubleshooting

### "Invalid API Key" Error
- ✅ Check of de key correct is gekopieerd (zonder spaties aan begin/einde)
- ✅ Check of alle 3 environments zijn aangevinkt
- ✅ Check of je **Redeploy** hebt gedaan na het toevoegen

### API geeft nog steeds error
- ✅ Wacht 2-3 minuten na redeploy
- ✅ Check Vercel deployment logs voor errors
- ✅ Test opnieuw met `test-api-online.ps1`

### Environment Variable niet zichtbaar
- ✅ Refresh de pagina
- ✅ Check of je in de juiste project bent
- ✅ Check of je de juiste permissions hebt

## 📸 Visuele Hulp

Als je hulp nodig hebt:
1. Screenshot van Vercel Dashboard → Settings → Environment Variables
2. Screenshot van het formulier na "Add New"
3. Screenshot van de deployment logs

---

**Na het toevoegen en redeployen zou alles moeten werken!** 🚀
