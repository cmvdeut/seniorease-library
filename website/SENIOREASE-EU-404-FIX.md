# 🔍 seniorease.eu 404 Error - Troubleshooting

## ⚠️ Probleem

Het domein `seniorease.eu` geeft nog steeds een 404 error, ook al is het gekoppeld aan Vercel.

## ✅ Checklist

### 1. Check Vercel Domain Status

1. **Ga naar Vercel Dashboard**
2. **Settings → Domains**
3. **Check status van `seniorease.eu`:**
   - ⏳ "Validating" → DNS propagatie nog bezig
   - ✅ "Valid Configuration" → DNS is correct
   - ❌ "Invalid Configuration" → DNS records kloppen niet

### 2. Check Root Directory (Belangrijk!)

1. **Settings → General**
2. **Check Root Directory:**
   - Moet zijn: `website`
   - **Niet:** `seniorease-library`, `www.seniorease.eu`, of leeg

### 3. Check DNS Records bij Strato

**In Strato DNS-instellingen:**

- Moet er staan:
  - **Type:** `A` of `CNAME` (zoals Vercel aangeeft)
  - **Naam:** `@` (of leeg)
  - **Waarde:** Exact zoals Vercel geeft

- **Verwijder:**
  - Oude A records die niet van Vercel zijn
  - Conflicterende records

### 4. Check of www werkt

- Test: `https://www.seniorease.eu`
- Als dit werkt maar zonder www niet → DNS probleem
- Als beide niet werken → Root Directory probleem

---

## 🔧 Oplossingen

### Oplossing 1: Root Directory Check

**Als Root Directory niet op `website` staat:**

1. Settings → General → Root Directory
2. Zet op: `website`
3. Save
4. Wacht op redeploy (1-2 minuten)

### Oplossing 2: DNS Records Controleren

**Als DNS status "Invalid Configuration" is:**

1. **In Vercel:** Noteer de exacte DNS records die Vercel vraagt
2. **In Strato:** 
   - Verwijder oude records
   - Voeg nieuwe records toe (exact zoals Vercel aangeeft)
3. **Wacht 30-60 minuten** op DNS propagatie

### Oplossing 3: Redeploy

**Soms helpt een nieuwe deployment:**

1. Ga naar Deployments
2. Klik op 3 puntjes (⋯) naast laatste deployment
3. Kies "Redeploy"
4. Wacht 1-2 minuten

---

## 🧪 Testen

1. **Test deployment URL:**
   - `https://seniorease-library-9hzfoov7x-cmvdeut-gmailcoms-projects.vercel.app`
   - Moet werken!

2. **Test domein:**
   - `https://seniorease.eu`
   - `https://www.seniorease.eu`
   - Beide moeten dezelfde pagina tonen

---

## ❓ Wat is de status in Vercel?

**Check dit en laat weten:**

1. **Domain status:** "Validating", "Valid Configuration", of "Invalid Configuration"?
2. **Root Directory:** Staat dit op `website`?
3. **Werkt de deployment URL?** (de lange URL die je eerder gaf)

Dan kan ik je precies helpen met de volgende stap!
