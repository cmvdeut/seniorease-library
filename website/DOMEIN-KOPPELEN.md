# 🌐 Domein Koppelen: seniorease.eu

## Stap 1: Domein Toevoegen in Vercel

1. **In Vercel dashboard:**
   - Klik op je project **`seniorease-library`**
   - Ga naar **Settings** (bovenaan)
   - Klik op **Domains** (in het linker menu)

2. **Klik "Add Domain"**

3. **Voer in:**
   - `seniorease.eu`
   - Klik **"Add"**

4. **Vercel geeft je DNS records:**
   - Je ziet een lijst met DNS records die je moet toevoegen
   - Meestal is dit een **CNAME** of **A** record

---

## Stap 2: DNS Records Toevoegen bij je Domain Provider

### Waar heb je seniorease.eu gekocht?

**Veelvoorkomende providers:**
- TransIP
- Hostnet
- Mijndomein.nl
- Namecheap
- GoDaddy
- Cloudflare

### Stappen bij je Domain Provider:

1. **Log in** bij je domain provider
2. **Ga naar DNS Settings** of **DNS Beheer**
3. **Zoek naar "DNS Records"** of "DNS Zones"
4. **Voeg de records toe** die Vercel geeft:

**Meestal krijg je dit van Vercel:**

**Voor het hoofddomein (seniorease.eu):**
- **Type:** `A` of `CNAME`
- **Name:** `@` of leeg (of `seniorease.eu`)
- **Value:** Het IP adres of CNAME dat Vercel geeft
- **TTL:** 3600 (of automatisch)

**Voor www subdomein (optioneel):**
- **Type:** `CNAME`
- **Name:** `www`
- **Value:** `cname.vercel-dns.com` (of wat Vercel aangeeft)
- **TTL:** 3600

5. **Sla op**

---

## Stap 3: Wachten op DNS Propagatie

- **Duurt:** 5 minuten tot 24 uur (meestal 15-60 minuten)
- **Vercel toont status:**
  - ⏳ "Validating" (bezig met controleren)
  - ✅ "Valid Configuration" (klaar!)
  - ❌ "Invalid Configuration" (controleer DNS records)

---

## Stap 4: Verifiëren

1. **In Vercel:** Status moet "Valid Configuration" zijn
2. **Test in browser:** `https://seniorease.eu`
3. **Je website zou nu moeten werken!** 🎉

---

## ❓ Problemen?

**"Invalid Configuration":**
- Controleer of DNS records correct zijn toegevoegd
- Wacht langer (DNS propagatie kan lang duren)
- Controleer spelling van records

**"Domain not found":**
- Controleer of je de juiste domain provider hebt
- Controleer of je toegang hebt tot DNS settings

**Website werkt niet:**
- Controleer of Root Directory op `website` staat
- Check Vercel deployment logs

---

## ✅ Na Koppeling

Je website is dan bereikbaar op:
- ✅ `https://seniorease.eu`
- ✅ `https://www.seniorease.eu` (als je www hebt toegevoegd)
- ✅ `https://seniorease-library.vercel.app` (blijft ook werken)

**Alle URLs werken automatisch met HTTPS!** 🔒
