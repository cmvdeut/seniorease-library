# 🔗 Nieuwe Stripe Payment Link Maken

## ⚠️ Probleem
De huidige Payment Link is niet meer actief:
```
https://buy.stripe.com/aFaaEQ9X0dGogALgwu6c003
```

## ✅ Oplossing: Maak Nieuwe Payment Link

### Stap 1: Ga naar Stripe Dashboard

1. **Open:** https://dashboard.stripe.com/payment-links
2. **Log in** met je Stripe account

### Stap 2: Maak Nieuwe Payment Link

**Optie A: Bestaande Link Reactiveren**

1. **Zoek** de link `aFaaEQ9X0dGogALgwu6c003` in de lijst
2. **Klik** op de link
3. **Check status:** Moet **Active** zijn
4. **Als status "Archived" of "Inactive":**
   - Klik op **"..."** (3 puntjes)
   - Kies **"Reactivate"** of **"Activate"**

**Optie B: Nieuwe Link Maken**

1. **Klik:** **"Create payment link"** (of **"New"**)
2. **Selecteer Product:**
   - Selecteer het juiste product
3. **Configureer:**
   - **Price:** Selecteer de juiste prijs
   - **Currency:** Kies de valuta (EUR, USD, etc.)
   - **Name:** Bijv. "SeniorEase Library - Full Version"
4. **Klik:** **"Create payment link"**
5. **Kopieer de link:**
   - Je krijgt een link zoals: `https://buy.stripe.com/XXXXXXXXXXXXX`
   - **Kopieer deze link volledig**

### Stap 3: Geef de Nieuwe Link Door

**Stuur me de nieuwe link, bijvoorbeeld:**
```
https://buy.stripe.com/XXXXXXXXXXXXX
```

### Stap 4: Code Update

Zodra ik de nieuwe link heb, update ik:
- `app/src/main/java/com/seniorease/library/MainActivity.kt` (regel 714)
- Build nieuwe APK
- Commit en push

---

## 🔍 Verificatie

**Check of de link werkt:**
1. **Open de link** in je browser
2. **Moet naar Stripe checkout pagina gaan**
3. **Check of betaling succesvol is**

---

## ⚠️ Belangrijk

- **Gebruik LIVE mode link** voor productie
- **Zorg dat het juiste product is gekoppeld**
- **Check dat de link ACTIVE is** voordat je hem gebruikt

---

**Maak een nieuwe Payment Link en geef me de URL!**

