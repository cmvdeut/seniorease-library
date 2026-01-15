# 🔑 Stripe Secret Key Ophalen

## 📍 Waar Vind Je Je Stripe Secret Key?

### Stap 1: Ga naar Stripe Dashboard

1. **Open je browser**
2. **Ga naar:** https://dashboard.stripe.com/
3. **Log in** met je Stripe account

### Stap 2: Ga naar API Keys

1. **Klik links in het menu op:** **Developers**
2. **Klik op:** **API keys** (of "API Keys")

### Stap 3: Zorg dat je in Live Mode bent

- **Check rechtsboven** - er moet "Live mode" staan (niet "Test mode")
- **Toggle aan/uit** als je in Test mode bent

### Stap 4: Kopieer de Secret Key

1. **Zoek naar "Secret key"** (niet "Publishable key")
2. **Klik op "Reveal live key"** of "Show live key"
3. **Kopieer de key** - begint met `sk_live_...`
4. **Plak in Vercel Environment Variables**

## 📝 Voorbeeld

Je Stripe Secret Key ziet er zo uit:
```
sk_live_...
```
(Begint altijd met `sk_live_` voor live mode)

## ⚠️ Belangrijk

- **Gebruik LIVE key** voor productie (`sk_live_...`)
- **Deel je key NOOIT** publiekelijk
- **Voeg `.env` toe aan `.gitignore`** (niet committen naar GitHub!)

## 🔗 Directe Link

**Stripe API Keys pagina:**
https://dashboard.stripe.com/apikeys

---

**Na het kopiëren: Plak de key in Vercel Environment Variables!**
