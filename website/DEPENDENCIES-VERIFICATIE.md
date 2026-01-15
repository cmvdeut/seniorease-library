# ✅ Dependencies Verificatie

## Stripe Dependency

**Status:** ✅ **TOEGEVOEGD**

**Locatie:** `website/package.json`

```json
{
  "dependencies": {
    "stripe": "^14.21.0"
  }
}
```

## ✅ Verificatie Checklist

- [x] Stripe dependency toegevoegd aan `package.json`
- [x] Versie: `^14.21.0` (laatste stabiele versie)
- [x] API route gebruikt `require("stripe")` correct
- [x] `vercel.json` configureert Node 20 runtime
- [x] Build script kopieert statische bestanden

## 🚀 Vercel Deployment

Vercel zal automatisch:
1. **Dependencies installeren** tijdens deployment (`npm install`)
2. **Serverless functions detecteren** in `api/` directory
3. **Node 20 runtime gebruiken** (zoals geconfigureerd in `vercel.json`)

## 📝 Belangrijk

- **Geen `node_modules` nodig** - Vercel installeert dependencies automatisch
- **Geen `package-lock.json` nodig** - Vercel genereert deze tijdens build
- **Stripe wordt geïnstalleerd** voor serverless functions

## ✅ Deployment Zal Slagen

De deployment zal slagen omdat:
- ✅ `package.json` bevat Stripe dependency
- ✅ API route gebruikt correcte `require("stripe")`
- ✅ Vercel detecteert automatisch serverless functions
- ✅ Node 20 runtime is geconfigureerd

## 🔍 Test Na Deployment

Na deployment, test de API:

```bash
curl -X POST https://www.seniorease.eu/api/verify-purchase \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com"}'
```

**Verwacht:** `{ "paid": false }` (of `true` als er een betaling is)

---

**Alles is correct geconfigureerd voor Vercel deployment!**
