# 🌐 Domein Koppelen - seniorease.eu

## ✅ Website Werkt!

Je deployment URL werkt:
`https://seniorease-library-9hzfoov7x-cmvdeut-gmailcoms-projects.vercel.app`

Nu kunnen we het domein `seniorease.eu` koppelen!

---

## Stap 1: Domein Toevoegen in Vercel

1. **In Vercel Dashboard:**
   - Klik op je project `seniorease-library`
   - Ga naar **Settings** → **Domains**
   - Klik **"Add Domain"**
   - Voer in: `seniorease.eu`
   - Klik **"Add"**

2. **Vercel geeft je DNS records:**
   - Noteer de **exacte** records die Vercel geeft
   - Meestal een **A record** met een IP adres, OF
   - Een **CNAME record** met een waarde

---

## Stap 2: DNS Records Toevoegen bij Strato.nl

### Inloggen bij Strato:

1. **Ga naar [strato.nl](https://strato.nl)** en log in
2. **Klik op "Mijn Strato"** (rechtsboven)
3. **Ga naar "Domeinen"** in het menu
4. **Klik op je domein:** `seniorease.eu`
5. **Klik op "DNS-instellingen"** of "DNS beheer"
6. **Klik op "DNS-records bewerken"** of "Aanpassen"

### Record Toevoegen:

**BELANGRIJK:** Gebruik de **exacte** waarden die Vercel geeft!

1. **Klik "Record toevoegen"** of "Nieuw record"

2. **Als Vercel een A Record vraagt:**
   - **Type:** `A`
   - **Naam/Hostname:** `@` (of leeg laten)
   - **Waarde/Doel:** **[IP adres van Vercel]** (precies zoals Vercel aangeeft)
   - **TTL:** `3600` of "Automatisch"
   - **Opslaan**

3. **Als Vercel een CNAME vraagt:**
   - **Verwijder eerst** eventuele bestaande A records voor `@`
   - **Type:** `CNAME`
   - **Naam/Hostname:** `@` (of leeg laten)
   - **Waarde/Doel:** **[CNAME waarde van Vercel]** (precies zoals Vercel aangeeft)
   - **TTL:** `3600` of "Automatisch"
   - **Opslaan**

### Voor www subdomein (optioneel):

Als je ook `www.seniorease.eu` wilt:
- **Type:** `CNAME`
- **Naam:** `www`
- **Waarde:** `cname.vercel-dns.com` (of wat Vercel aangeeft)
- **TTL:** `3600`
- **Opslaan**

---

## Stap 3: Wachten op DNS Propagatie

- **Duurt:** 15 minuten tot 2 uur (meestal 30-60 minuten)
- **Check status in Vercel:** Settings → Domains → seniorease.eu
- **Status moet worden:** "Valid Configuration" (groen vinkje)

---

## Stap 4: Testen

1. **In Vercel:** Status moet "Valid Configuration" zijn
2. **Test in browser:** `https://seniorease.eu`
3. **Je website zou nu moeten werken!** 🎉

---

## ❓ Problemen?

**"Invalid Configuration" in Vercel:**
- Controleer of je de juiste waarde hebt ingevoerd (spelling!)
- Wacht langer (DNS propagatie kan lang duren)
- Controleer of het record type klopt (A vs CNAME)

**Website werkt niet:**
- Test eerst: `https://seniorease-library-9hzfoov7x-cmvdeut-gmailcoms-projects.vercel.app` (werkt die wel?)
- Check Vercel deployment logs
- Controleer of Root Directory op `website` staat

---

## ✅ Na Koppeling

Je website is dan bereikbaar op:
- ✅ `https://seniorease.eu`
- ✅ `https://www.seniorease.eu` (als je www hebt toegevoegd)
- ✅ `https://seniorease-library.vercel.app` (werkt mogelijk na update)
- ✅ `https://seniorease-library-9hzfoov7x-cmvdeut-gmailcoms-projects.vercel.app` (deployment URL)

**Alle URLs werken automatisch met HTTPS!** 🔒

---

**Laat weten welke DNS records Vercel precies geeft, dan kan ik je exact helpen met Strato!**
