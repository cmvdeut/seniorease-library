# 🔧 Automatische Production Deployment Instellen

## ⚠️ Probleem:
- Deployments worden wel gebouwd, maar gaan naar "Preview" in plaats van "Production"
- Je moet handmatig "Promote to Production" klikken
- Dit is vervelend bij elke push

## ✅ Oplossing: Production Branch Instellen

### Stap 1: Ga naar Vercel Project Settings

1. **In Vercel Dashboard:**
   - Klik op je project `seniorease-library`
   - Ga naar **Settings** → **Git**

### Stap 2: Stel Production Branch In

1. **Zoek "Production Branch":**
   - Dit staat onder "Git Repository"
   - Standaard kan dit leeg zijn of op een andere branch staan

2. **Zet Production Branch op:**
   - **Branch:** `master` (of `main` als je die gebruikt)
   - **Klik "Save"**

### Stap 3: Verifieer

**Na het instellen:**
- Elke push naar `master` branch gaat automatisch naar **Production**
- Je hoeft niet meer handmatig "Promote to Production" te klikken
- Je domein `seniorease.eu` wordt automatisch bijgewerkt

---

## 🔍 Alternatief: Via Vercel CLI

Als je via CLI wilt instellen:

```bash
vercel project set-production-branch master
```

---

## 📋 Checklist

- [ ] Settings → Git → Production Branch = `master`
- [ ] Test: Push naar master → Moet automatisch naar Production gaan
- [ ] Domein werkt automatisch zonder "Promote to Production"

---

## 💡 Waarom Dit Gebeurt

Vercel maakt standaard **preview deployments** voor elke push. Dit is handig voor:
- Pull requests
- Feature branches
- Testen voordat je naar production gaat

Maar voor je `master` branch wil je dat het **direct naar production** gaat, omdat dat je "live" branch is.

---

**Zet Production Branch op `master` in Settings → Git!**
