# 🔧 Stripe Test Mode Fix

## ⚠️ Probleem:
Stripe geeft foutmelding: "Je kaart is geweigerd. Je aanvraag vond plaats in de livemodus, maar er is een bekende testkaart gebruikt."

## ✅ Oplossing:

### Stap 1: Check Stripe Dashboard

1. **Log in op Stripe Dashboard:**
   - Ga naar: https://dashboard.stripe.com/
   - Zorg dat je in **Test mode** bent (toggle rechtsboven)

2. **Check je Payment Links:**
   - Ga naar: **Products** → **Payment Links**
   - Zoek je payment link: `9B6fZa8SW31K0BNcge6c002`
   - Check of deze in **Test mode** is gemaakt

### Stap 2: Maak Nieuwe Test Payment Link

Als de link in Live mode is:

1. **Zorg dat je in Test mode bent** (toggle rechtsboven)
2. **Maak nieuwe Payment Link:**
   - Ga naar: **Products** → **Payment Links** → **Create payment link**
   - Configureer je product/prijs
   - **Zorg dat je in Test mode bent!**
   - Kopieer de nieuwe link (zonder "test_" prefix - dat wordt automatisch toegevoegd)

3. **Update de link in de app:**
   - Vervang de payment URL in `MainActivity.kt`
   - Rebuild de app

### Stap 3: Test Kaarten

Gebruik deze testkaarten in **Test mode**:

- **Succesvol:** `4242 4242 4242 4242`
- **Geweigerd:** `4000 0000 0000 0002`
- **3D Secure:** `4000 0025 0000 3155`

**Expiry:** Elke toekomstige datum (bijv. 12/25)
**CVC:** Elke 3 cijfers (bijv. 123)

### Stap 4: Test opnieuw

1. Zorg dat je in **Test mode** bent in Stripe Dashboard
2. Gebruik testkaart: `4242 4242 4242 4242`
3. Test de betaling

---

## 🔍 Check Dit:

- [ ] Stripe Dashboard staat in **Test mode** (niet Live mode)
- [ ] Payment link is gemaakt in **Test mode**
- [ ] Je gebruikt een **testkaart** (niet echte kaart)
- [ ] De app gebruikt de juiste payment link

---

**Belangrijk:** De "test_" prefix in de URL betekent niet automatisch test mode - de link moet in het Stripe Dashboard in test mode zijn gemaakt!
