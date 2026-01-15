# SeniorEase API Server

API server voor het verifiëren van Stripe betalingen.

## 📋 Vereisten

- Node.js 16+ 
- Stripe account met API keys

## 🚀 Installatie

1. **Installeer dependencies:**
```bash
cd api
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

```bash
npm start
```

Server draait op: `http://localhost:3000`

## 📡 API Endpoint

### POST /api/verify-purchase

Verifieert of een gebruiker heeft betaald.

**Request:**
```json
{
  "email": "user@example.com"
}
```

**Response (success):**
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

- Rate limiting: 10 requests per 15 minuten per IP
- Geen Stripe data wordt geretourneerd
- Email validatie
- Error handling

## 🧪 Testen

```bash
curl -X POST http://localhost:3000/api/verify-purchase \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com"}'
```

## 📝 Notities

- Test Price ID: `price_1So2hP3GmccxYlyt6rNoyUxz` (hardcoded)
- Zoekt in laatste 100 Payment Intents en Checkout Sessions
- Matcht op email en price ID
- Alleen "paid" of "succeeded" status wordt geaccepteerd
