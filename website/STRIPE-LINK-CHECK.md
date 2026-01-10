# 🔗 Stripe Payment Link Check

## ✅ Test Link (huidig in app)
```
https://buy.stripe.com/test_9B6fZa8SW31K0BNcge6c002
```

## 📋 Checklist

### 1. Is de link actief?
- ✅ Test de link in je browser
- ✅ Moet naar Stripe checkout pagina gaan
- ❌ Als "link is no longer available" → nieuwe link maken

### 2. Is de link in test mode?
- ✅ Link begint met `/test_` → test mode
- ❌ Link zonder `/test_` → live mode (gebruik test cards niet!)

### 3. Stripe Dashboard Check
1. Ga naar: https://dashboard.stripe.com/test/payment-links
2. Check of de link bestaat
3. Check of de link **Active** is
4. Check of de **Price ID** klopt: `price_1So2hP3GmccxYlyt6rNoyUxz`

## 🔧 Als link niet werkt

### Optie 1: Nieuwe Test Link Maken
1. Ga naar: https://dashboard.stripe.com/test/payment-links
2. Klik **Create payment link**
3. Selecteer product met Price ID: `price_1So2hP3GmccxYlyt6rNoyUxz`
4. Kopieer de nieuwe link
5. Update in app: `MainActivity.kt` regel 682

### Optie 2: Check Bestaande Link
1. Ga naar: https://dashboard.stripe.com/test/payment-links
2. Zoek de link: `9B6fZa8SW31K0BNcge6c002`
3. Check status: **Active** of **Inactive**
4. Als **Inactive**: klik **Reactivate**

## 🧪 Test de Link
1. Open link in browser
2. Vul test card in: `4242 4242 4242 4242`
3. Expiry: willekeurige toekomstige datum
4. CVC: willekeurige 3 cijfers
5. Email: jouw test email
6. Klik **Pay**
7. Check of betaling succesvol is

## ✅ Na Betaling
1. Gebruik hetzelfde email in de app
2. Klik "I've paid — unlock"
3. Voer email in
4. Klik "Check purchase"
5. App zou moeten unlocken
