# 🔑 Stripe Secret Key Ophalen

## 📍 Waar Vind Je Je Stripe Secret Key?

### Stap 1: Ga naar Stripe Dashboard

1. **Open je browser**
2. **Ga naar:** https://dashboard.stripe.com/
3. **Log in** met je Stripe account

### Stap 2: Ga naar API Keys

1. **Klik links in het menu op:** **Developers**
2. **Klik op:** **API keys** (of "API Keys")

### Stap 3: Zorg dat je in Test Mode bent

- **Check rechtsboven** - er moet "Test mode" staan (niet "Live mode")
- **Toggle aan/uit** als je in Live mode bent

### Stap 4: Kopieer de Secret Key

1. **Zoek naar "Secret key"** (niet "Publishable key")
2. **Klik op "Reveal test key"** of "Show test key"
3. **Kopieer de key** - begint met `sk_test_...`
4. **Plak in `.env` bestand**

## 📝 Voorbeeld

Je Stripe Secret Key ziet er zo uit:
```
sk_test_51AbCdEfGhIjKlMnOpQrStUvWxYz...
```
(Begint altijd met `sk_test_` voor test mode of `sk_live_` voor live mode)

## ⚠️ Belangrijk

- **Gebruik TEST key** voor testen (`sk_test_...`)
- **Gebruik LIVE key** alleen voor productie (`sk_live_...`)
- **Deel je key NOOIT** publiekelijk
- **Voeg `.env` toe aan `.gitignore`** (niet committen naar GitHub!)

## 🔗 Directe Link

**Stripe API Keys pagina:**
https://dashboard.stripe.com/test/apikeys

---

**Na het kopiëren: Plak de key in `website/.env` bestand!**
