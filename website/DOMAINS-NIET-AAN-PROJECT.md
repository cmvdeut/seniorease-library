# 🔧 Domains Niet Aan Project Gekoppeld - Oplossing

## ⚠️ Probleem:
- `seniorease.eu` en `www.seniorease.eu` zijn "Valid Configuration"
- Maar ze zijn niet zichtbaar bij het project `seniorease-library`
- Of ze wijzen niet naar de juiste deployment

## ✅ Oplossing: Domains Toewijzen aan Project

### Stap 1: Check Welk Project

1. **In Vercel Dashboard:**
   - Klik op je project `seniorease-library`
   - Ga naar **Settings → Domains**
   - Check of `seniorease.eu` en `www.seniorease.eu` hier staan

### Stap 2: Als Domains Niet bij Project Staan

**Optie A: Domains Toevoegen aan Project**

1. **In je project `seniorease-library`:**
   - Settings → Domains
   - Klik "Add Domain"
   - Voer in: `seniorease.eu`
   - Klik "Add"
   - Herhaal voor `www.seniorease.eu`

**Optie B: Domains Verplaatsen**

1. **In Team Settings (als je daar bent):**
   - Ga naar de domain die je ziet
   - Klik "Edit"
   - Wijzig het project naar `seniorease-library`
   - OF verwijder en voeg opnieuw toe bij het project

### Stap 3: Check Domain Assignment

1. **Klik op `www.seniorease.eu`** (of `seniorease.eu`)
2. **Klik "Edit"**
3. **Check:**
   - **Production:** Moet wijzen naar je laatste deployment
   - **Project:** Moet `seniorease-library` zijn
   - **Branch:** Moet `master` zijn (of de branch die je gebruikt)

### Stap 4: Verwijder Redirect (Als Nodig)

Ik zie dat `seniorease.eu` een 307 redirect heeft naar `www.seniorease.eu`. Dit kan problemen veroorzaken.

**Als je beide domains wilt laten werken:**

1. **Klik op `seniorease.eu`**
2. **Klik "Edit"**
3. **Verwijder de redirect** (als die er is)
4. **Zet beide domains op "Production"**

---

## 🔍 Check Dit:

1. **In je project `seniorease-library` → Settings → Domains:**
   - Zie je `seniorease.eu` en `www.seniorease.eu` staan?
   - Zo niet, voeg ze toe!

2. **Als je ze wel ziet:**
   - Klik op een domain → "Edit"
   - Check of "Production" wijst naar je laatste deployment
   - Check of het project `seniorease-library` is

---

## 📋 Stap-voor-Stap:

1. **Ga naar project `seniorease-library`**
2. **Settings → Domains**
3. **Als domains niet staan:**
   - Klik "Add Domain"
   - Voeg `seniorease.eu` toe
   - Voeg `www.seniorease.eu` toe
4. **Als domains wel staan:**
   - Klik op een domain → "Edit"
   - Check of "Production" correct is ingesteld
   - Save

---

**Voeg de domains toe aan het project `seniorease-library` als ze daar niet staan!**
