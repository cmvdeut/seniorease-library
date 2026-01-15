# 🔧 API Setup voor Vercel

## ✅ API Route Toegevoegd

De API route `/api/verify-purchase` is toegevoegd als Vercel serverless function.

**Locatie:** `website/api/verify-purchase.js`

## 🔑 Environment Variable Toevoegen

1. **Ga naar Vercel Dashboard:**
   - https://vercel.com/dashboard
   - Selecteer je project: `seniorease-library` (of jouw project naam)

2. **Ga naar Settings → Environment Variables**

3. **Voeg toe:**
   - **Name:** `STRIPE_SECRET_KEY`
   - **Value:** `sk_test_jouw_stripe_key_hier` (of live key voor productie)
   - **Environment:** Production, Preview, Development (alle drie aanvinken)

4. **Klik "Save"**

## 🚀 Deploy

Na het toevoegen van de environment variable:

1. **Push naar GitHub** (als je GitHub gebruikt)
2. **Of redeploy in Vercel Dashboard:**
   - Ga naar Deployments
   - Klik op de laatste deployment
   - Klik "Redeploy"

## 🧪 Testen

Na deployment, test de API:

```bash
curl -X POST https://www.seniorease.eu/api/verify-purchase \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com"}'
```

**Response:**
```json
{
  "paid": true
}
```

## 📝 Notities

- **Price ID:** `price_1So2hP3GmccxYlyt6rNoyUxz` (hardcoded, test mode)
- **Rate limiting:** 20 requests per minuut per IP
- **CORS:** Enabled voor alle origins (website + app)
- **Error handling:** Geen details gelekt, altijd `{ paid: false }` bij errors

## ⚠️ Belangrijk

- **Zorg dat `STRIPE_SECRET_KEY` is toegevoegd** aan Vercel Environment Variables
- **Test mode key** voor testen: `sk_test_...`
- **Live mode key** voor productie: `sk_live_...`

---

**Na setup: Test de API route!**
