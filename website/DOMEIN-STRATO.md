# 🌐 Domein Koppelen: seniorease.eu bij Strato.nl

## Stap 1: Domein Toevoegen in Vercel

1. **In Vercel dashboard:**
   - Klik op je project **`seniorease-library`**
   - Ga naar **Settings** → **Domains**
   - Klik **"Add Domain"**
   - Voer in: `seniorease.eu`
   - Klik **"Add"**

2. **Vercel geeft je DNS records:**
   - Noteer de **A record** of **CNAME** die Vercel geeft
   - Meestal krijg je een IP adres (A record) of CNAME waarde

---

## Stap 2: DNS Records Toevoegen bij Strato.nl

### Inloggen bij Strato

1. **Ga naar [strato.nl](https://strato.nl)** en log in
2. **Klik op "Mijn Strato"** (rechtsboven)
3. **Ga naar "Domeinen"** in het menu

### DNS Records Toevoegen

1. **Klik op je domein:** `seniorease.eu`
2. **Klik op "DNS-instellingen"** of "DNS beheer"
3. **Klik op "DNS-records bewerken"** of "Aanpassen"

### Record Toevoegen

**Voor het hoofddomein (seniorease.eu):**

1. **Klik "Record toevoegen"** of "Nieuw record"
2. **Vul in:**
   - **Type:** `A` (als Vercel een IP adres geeft) of `CNAME` (als Vercel een CNAME geeft)
   - **Naam/Hostname:** `@` of leeg laten (voor hoofddomein)
   - **Waarde/Doel:** Het IP adres of CNAME dat Vercel geeft
     - Voor A record: Bijv. `76.76.21.21` (voorbeeld - gebruik wat Vercel geeft)
     - Voor CNAME: Bijv. `cname.vercel-dns.com` (voorbeeld - gebruik wat Vercel geeft)
   - **TTL:** `3600` of "Automatisch"
3. **Klik "Opslaan"** of "Toevoegen"

**Voor www subdomein (optioneel):**

1. **Klik "Record toevoegen"**
2. **Vul in:**
   - **Type:** `CNAME`
   - **Naam/Hostname:** `www`
   - **Waarde/Doel:** `cname.vercel-dns.com` (of wat Vercel aangeeft)
   - **TTL:** `3600`
3. **Klik "Opslaan"**

---

## Stap 3: Wachten op DNS Propagatie

- **Duurt:** 15 minuten tot 2 uur (meestal 30-60 minuten)
- **In Vercel:** Status verandert van "Validating" naar "Valid Configuration"
- **Check status:** Vercel dashboard → Settings → Domains

---

## Stap 4: Verifiëren

1. **In Vercel:** Status moet "Valid Configuration" zijn (groen vinkje)
2. **Test in browser:** `https://seniorease.eu`
3. **Je website zou nu moeten werken!** 🎉

---

## 📋 Strato.nl Specifieke Tips

- **Let op:** Soms moet je eerst bestaande A records verwijderen voordat je nieuwe toevoegt
- **TTL:** Laat op "Automatisch" staan als je niet zeker bent
- **Oude records:** Controleer of er oude A records zijn die verwijderd moeten worden

---

## ❓ Problemen?

**"Invalid Configuration" in Vercel:**
- Controleer of je de juiste waarde hebt ingevoerd (spelling!)
- Wacht langer (DNS kan tot 24 uur duren, maar meestal sneller)
- Controleer of het record type klopt (A vs CNAME)

**Website werkt niet:**
- Controleer of Root Directory op `website` staat in Vercel
- Test eerst op `https://seniorease-library.vercel.app` (werkt die wel?)
- Check Vercel deployment logs

**Kun je DNS niet vinden in Strato:**
- Zoek naar "DNS", "DNS-instellingen", "DNS beheer", of "Nameservers"
- Sommige Strato accounts hebben DNS onder "Geavanceerd" of "Expert modus"

---

## ✅ Na Koppeling

Je website is dan bereikbaar op:
- ✅ `https://seniorease.eu`
- ✅ `https://www.seniorease.eu` (als je www hebt toegevoegd)
- ✅ `https://seniorease-library.vercel.app` (blijft ook werken)

**Alle URLs werken automatisch met HTTPS!** 🔒

---

**Laat weten als je hulp nodig hebt bij het vinden van de DNS instellingen in Strato!**
