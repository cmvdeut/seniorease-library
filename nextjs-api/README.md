# SeniorEase Next.js API

Next.js API route voor het verifiëren van Stripe betalingen.

## 📋 Vereisten

- Node.js 16+
- Stripe account met API keys

## 🚀 Installatie

1. **Installeer dependencies:**
```bash
cd nextjs-api
npm install
```

2. **Configureer environment variabelen:**
```bash
cp .env.example .env
```

3. **Voeg je Stripe Secret Key toe aan `.env`:**
```
STRIPE_SECRET_KEY=sk_test_your_key_here
```

## 🔑 Stripe API Key Ophalen

1. Ga naar: https://dashboard.stripe.com/apikeys
2. Zorg dat je in **Test mode** bent (toggle rechtsboven)
3. Kopieer de **Secret key** (begint met `sk_test_`)
4. Plak in `.env` bestand

## ▶️ Starten

**Development:**
```bash
npm run dev
```

**Production:**
```bash
npm run build
npm start
```

API route is beschikbaar op: `http://localhost:3000/api/verify-purchase`

## 📡 API Endpoint

### POST /api/verify-purchase

Verifieert of een gebruiker heeft betaald.

**Request:**
```json
{
  "email": "user@example.com"
}
```

**Response (paid):**
```json
{
  "paid": true
}
```

**Response (not paid):**
```json
{
  "paid": false
}
```

**Response (error):**
```json
{
  "paid": false,
  "error": "Error message"
}
```

## 🔒 Beveiliging

- **Rate limiting:** 10 requests per 15 minuten per IP (in-memory)
- **Email validatie:** Format check
- **Geen Stripe data:** Alleen `{ paid: boolean }` wordt geretourneerd
- **Error handling:** Graceful error handling

## 🧪 Testen

```bash
curl -X POST http://localhost:3000/api/verify-purchase \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com"}'
```

## 📝 Notities

- **Test Price ID:** `price_1So2hP3GmccxYlyt6rNoyUxz` (hardcoded)
- **Zoekt in:** Laatste 100 Checkout Sessions
- **Matcht op:** Email (case-insensitive), payment_status === "paid", en Price ID
- **Rate limiting:** In-memory map (reset na 15 minuten)

## 🚀 Deployment

### Vercel (Aanbevolen)

1. Push naar GitHub
2. Import project in Vercel
3. Voeg `STRIPE_SECRET_KEY` toe aan Environment Variables
4. Deploy

### Andere Platforms

- **Netlify:** Gebruik Next.js plugin
- **Railway:** Auto-detect Next.js
- **Self-hosted:** `npm run build && npm start`

---

**Na installatie: Test de API route!**
