# 🔗 Nieuwe Stripe Payment Link Maken

## ⚠️ Huidige Link Werkt Niet

De link `https://buy.stripe.com/aFaaEQ9X0dGogALgwu6c003` werkt niet meer.

## ✅ Stap-voor-Stap: Nieuwe Link Maken

### Stap 1: Ga naar Stripe Dashboard

1. **Open:** https://dashboard.stripe.com/
2. **Log in** met je Stripe account
3. **Zorg dat je in Live mode bent** (toggle rechtsboven moet "Live mode" zeggen)

### Stap 2: Maak Nieuwe Payment Link

1. **Klik links in menu op:** **Products**
2. **Klik op:** **Payment Links** (of "Payment links")
3. **Klik op groene knop:** **Create payment link** (of "+ New")

### Stap 3: Configureer Product en Prijs

**Optie A: Als je al een product hebt:**
- Selecteer je bestaande product
- Kies de prijs

**Optie B: Maak nieuw product aan:**
1. Klik op **"Create new product"** (of "+ New product")
2. **Product name:** Bijv. "SeniorEase Library Full Version"
3. **Description:** (optioneel) "Unlock unlimited books"
4. **Pricing:**
   - Kies **One-time payment**
   - Voer prijs in (bijv. €9.99)
   - Kies currency (EUR)
5. Klik **Save product**

### Stap 4: Maak de Payment Link

1. **Selecteer het product** dat je net hebt gemaakt
2. **Zorg dat je in Live mode bent!** (check rechtsboven)
3. Klik **Create link** (of "Continue")

### Stap 5: Kopieer de Link

Je krijgt nu een link zoals:
```
https://buy.stripe.com/XXXXXXXXXXXXX
```

**Kopieer deze volledige URL!**

### Stap 6: Update App

1. **Open:** `app/src/main/java/com/seniorease/library/MainActivity.kt`
2. **Zoek regel ~682:**
```kotlin
val paymentUrl = "https://buy.stripe.com/aFaaEQ9X0dGogALgwu6c003"
```

3. **Vervang met jouw nieuwe link:**
```kotlin
val paymentUrl = "https://buy.stripe.com/JOUW_NIEUWE_CODE_HIER"
```

### Stap 7: Rebuild App

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
.\gradlew assembleDemoRelease
```

### Stap 8: Test de Nieuwe Link

1. Installeer nieuwe APK op telefoon
2. Test de payment link in de app
3. Doe een echte betaling (live mode)

---

## 🔍 Belangrijke Checks

- ✅ **Live mode** moet AAN staan in Stripe Dashboard
- ✅ Link moet beginnen met `https://buy.stripe.com/`
- ✅ Product en prijs moeten zijn ingesteld
- ✅ Link moet worden geüpdatet in `MainActivity.kt`
- ✅ App moet opnieuw worden gebuild

---

## 💡 Tips

- **Bewaar de link** ergens veilig (bijv. in een notitie)
- **Gebruik echte betaling** om te testen in live mode

---

**Heb je de nieuwe link? Stuur hem door en ik update de app voor je!**

