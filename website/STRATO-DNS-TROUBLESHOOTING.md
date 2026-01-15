# 🔍 Strato DNS Troubleshooting - seniorease.eu

## ⚠️ Probleem: 404 Error na DNS configuratie

Als je nog steeds een 404 ziet na het toevoegen van DNS records bij Strato, volg deze stappen:

---

## Stap 1: Check of Vercel Website Werkt

**Test eerst:** `https://seniorease-library.vercel.app`

- ✅ **Werkt dit?** → DNS probleem bij Strato
- ❌ **Werkt dit niet?** → Vercel configuratie probleem (Root Directory!)

---

## Stap 2: Check Vercel Domain Status

1. **Ga naar Vercel Dashboard**
2. **Settings → Domains**
3. **Check status van `seniorease.eu`:**
   - ⏳ "Validating" → Wacht nog (DNS propagatie)
   - ✅ "Valid Configuration" → DNS is correct
   - ❌ "Invalid Configuration" → DNS records kloppen niet

---

## Stap 3: Strato DNS Records Controleren

### Wat moet er in Strato staan?

**Voor het hoofddomein (seniorease.eu):**

1. **Log in bij Strato.nl**
2. **Ga naar:** Mijn Strato → Domeinen → seniorease.eu → DNS-instellingen
3. **Check of je deze records hebt:**

#### Optie A: A Record (als Vercel een IP adres geeft)
```
Type: A
Naam: @ (of leeg)
Waarde: [IP adres van Vercel] (bijv. 76.76.21.21)
TTL: 3600
```

#### Optie B: CNAME Record (als Vercel een CNAME geeft)
```
Type: CNAME
Naam: @ (of leeg)
Waarde: [CNAME van Vercel] (bijv. cname.vercel-dns.com)
TTL: 3600
```

### ⚠️ Belangrijk bij Strato:

- **Verwijder oude A records** die naar andere IP adressen wijzen
- **Gebruik `@` of laat Naam leeg** voor het hoofddomein
- **Niet `seniorease.eu`** in het Naam veld!

---

## Stap 4: Vercel DNS Records Ophalen

1. **In Vercel Dashboard:**
   - Settings → Domains
   - Klik op `seniorease.eu`
   - Je ziet de **exacte DNS records** die je nodig hebt
   - **Noteer deze precies!**

2. **Vercel geeft meestal:**
   - Een **A record** met een IP adres, OF
   - Een **CNAME record** met een waarde

---

## Stap 5: Strato DNS Records Aanpassen

### Als Vercel een A Record vraagt:

1. **In Strato DNS-instellingen:**
   - Zoek naar bestaande **A records** voor `@` of leeg
   - **Verwijder deze** (als ze niet kloppen)
   - **Voeg nieuw A record toe:**
     - Type: `A`
     - Naam: `@` (of leeg laten)
     - Waarde: **[IP adres van Vercel]** (precies zoals Vercel aangeeft)
     - TTL: `3600`
   - **Opslaan**

### Als Vercel een CNAME vraagt:

1. **In Strato DNS-instellingen:**
   - Zoek naar bestaande **A records** voor `@`
   - **Verwijder deze** (CNAME kan niet samen met A record)
   - **Voeg CNAME record toe:**
     - Type: `CNAME`
     - Naam: `@` (of leeg laten)
     - Waarde: **[CNAME waarde van Vercel]** (precies zoals Vercel aangeeft)
     - TTL: `3600`
   - **Opslaan**

---

## Stap 6: Wachten op DNS Propagatie

- **Duurt:** 15 minuten tot 2 uur (meestal 30-60 minuten)
- **Check status in Vercel:** Settings → Domains → seniorease.eu
- **Status moet worden:** "Valid Configuration" (groen vinkje)

---

## Stap 7: Testen

1. **Test direct:** `https://seniorease.eu`
2. **Test ook:** `https://www.seniorease.eu` (als je www hebt toegevoegd)
3. **Check Vercel:** Status moet "Valid Configuration" zijn

---

## ❓ Veelvoorkomende Fouten bij Strato

### Fout 1: Verkeerde Naam waarde
- ❌ **Fout:** Naam = `seniorease.eu`
- ✅ **Goed:** Naam = `@` of leeg

### Fout 2: Oude A records niet verwijderd
- ❌ **Fout:** Meerdere A records voor `@`
- ✅ **Goed:** Alleen de nieuwe A record van Vercel

### Fout 3: CNAME en A record tegelijk
- ❌ **Fout:** Zowel CNAME als A record voor `@`
- ✅ **Goed:** Alleen CNAME OF alleen A record (zoals Vercel aangeeft)

### Fout 4: Verkeerde Waarde
- ❌ **Fout:** IP adres of CNAME niet precies zoals Vercel aangeeft
- ✅ **Goed:** Kopieer exact van Vercel dashboard

### Fout 5: Nameservers niet bij Strato
- ❌ **Fout:** Nameservers wijzen naar andere provider
- ✅ **Goed:** Nameservers moeten bij Strato staan

---

## 🔍 DNS Check Tools

Test of je DNS records correct zijn:

1. **Online DNS checker:**
   - Ga naar: https://dnschecker.org
   - Voer in: `seniorease.eu`
   - Check of het IP adres klopt met wat Vercel geeft

2. **Command line (Windows):**
   ```powershell
   nslookup seniorease.eu
   ```

---

## 📞 Hulp Nodig?

**Als het na 2 uur nog niet werkt:**

1. **Screenshot maken van:**
   - Strato DNS-instellingen (alle records)
   - Vercel Domains pagina (met de records die Vercel vraagt)

2. **Check:**
   - Werkt `https://seniorease-library.vercel.app`? (moet werken!)
   - Wat is de status in Vercel Domains?
   - Welke records staan er precies in Strato?

---

**Laat weten welke records Vercel precies vraagt, dan kan ik je exact helpen!**
