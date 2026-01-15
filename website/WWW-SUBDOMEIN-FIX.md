# 🔧 www.seniorease.eu Fix

## ⚠️ Probleem

De browser gaat automatisch naar `www.seniorease.eu` maar dit geeft een 404.

## ✅ Oplossing: www Subdomein Toevoegen in Vercel

### Stap 1: www Toevoegen als Domain

1. **In Vercel Dashboard:**
   - Settings → Domains
   - Klik "Add Domain"
   - Voer in: `www.seniorease.eu`
   - Klik "Add"

2. **Vercel geeft je DNS records:**
   - Meestal een **CNAME record** voor `www`
   - Noteer de exacte waarde

### Stap 2: DNS Record Toevoegen bij Strato

1. **In Strato DNS-instellingen:**
   - Klik "Record toevoegen"
   - **Type:** `CNAME`
   - **Naam:** `www`
   - **Waarde:** [CNAME waarde van Vercel] (bijv. `cname.vercel-dns.com`)
   - **TTL:** `3600`
   - **Opslaan**

### Stap 3: Wachten

- **DNS propagatie:** 15-60 minuten
- **Check status in Vercel:** Settings → Domains → www.seniorease.eu
- **Status moet worden:** "Valid Configuration"

---

## 🔄 Alternatief: Redirect van www naar hoofddomein

Als je liever www redirect naar seniorease.eu:

1. **In Vercel:** Voeg `www.seniorease.eu` toe als domain
2. **In vercel.json:** Voeg redirect toe (zie hieronder)

---

## 📋 Vercel.json met Redirect (Optioneel)

Als je www automatisch wilt redirecten naar zonder www:

```json
{
  "headers": [
    {
      "source": "/downloads/(.*)",
      "headers": [
        {
          "key": "Content-Type",
          "value": "application/vnd.android.package-archive"
        },
        {
          "key": "Content-Disposition",
          "value": "attachment"
        }
      ]
    }
  ],
  "redirects": [
    {
      "source": "/(.*)",
      "destination": "https://seniorease.eu/$1",
      "permanent": true,
      "has": [
        {
          "type": "host",
          "value": "www.seniorease.eu"
        }
      ]
    }
  ]
}
```

**Maar dit is niet nodig als je beide domains correct configureert!**

---

## ✅ Aanbevolen: Beide Domains Toevoegen

**Voeg beide toe in Vercel:**
1. `seniorease.eu` (hoofddomein)
2. `www.seniorease.eu` (www subdomein)

**Dan werken beide:**
- ✅ `https://seniorease.eu`
- ✅ `https://www.seniorease.eu`

---

**Voeg `www.seniorease.eu` toe als domain in Vercel en configureer de DNS bij Strato!**
