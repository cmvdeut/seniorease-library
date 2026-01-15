# 🔄 Vercel Project Opnieuw Opzetten - Stap voor Stap

## Stap 1: Project Verwijderen in Vercel

1. **Ga naar Vercel Dashboard**
2. **Klik op je project:** `seniorease-library`
3. **Ga naar:** Settings (bovenaan)
4. **Scroll helemaal naar beneden**
5. **Klik op:** "Delete Project" (rood)
6. **Bevestig verwijdering:**
   - Typ de project naam: `seniorease-library`
   - Klik "Delete"

---

## Stap 2: Nieuw Project Aanmaken

1. **In Vercel Dashboard:**
   - Klik "Add New..." (rechtsboven)
   - Kies "Project"

2. **Import Git Repository:**
   - Klik "Import Git Repository"
   - Selecteer: `cmvdeut/seniorease-library`
   - Klik "Import"

---

## Stap 3: Project Instellingen (BELANGRIJK!)

### Configure Project:

1. **Framework Preset:**
   - Kies: `Other` of `Static Site`

2. **Root Directory:**
   - ⚠️ **BELANGRIJK:** Klik op "Edit" naast Root Directory
   - Voer in: `website`
   - Klik "Continue"

3. **Build & Output Settings:**
   - **Build Command:** (leeg laten)
   - **Output Directory:** `.` (punt)
   - **Install Command:** (leeg laten)

4. **Environment Variables:**
   - Geen nodig voor deze static site
   - Klik "Deploy"

---

## Stap 4: Wachten op Deployment

- **Duurt:** 1-2 minuten
- **Status:** Moet "Ready" worden (groen)

---

## Stap 5: Testen

1. **Test de website:**
   - Ga naar: `https://seniorease-library.vercel.app`
   - Je zou nu de download pagina moeten zien! 🎉

2. **Als het werkt:**
   - ✅ Root Directory was correct ingesteld
   - ✅ Website laadt zonder 404

---

## Stap 6: Domein Koppelen (Later)

**Pas als de website werkt op het Vercel domein:**

1. **Settings → Domains**
2. **Klik "Add Domain"**
3. **Voer in:** `seniorease.eu`
4. **Klik "Add"**
5. **Voeg DNS records toe bij Strato.nl** (zoals eerder beschreven)

---

## ✅ Checklist

- [ ] Oud project verwijderd
- [ ] Nieuw project aangemaakt
- [ ] Root Directory = `website` (vanaf het begin!)
- [ ] Deployment succesvol
- [ ] Website werkt op Vercel domein
- [ ] (Later) Domein `seniorease.eu` gekoppeld

---

## ⚠️ Belangrijkste Tip

**Zet de Root Directory ALTIJD op `website` tijdens het aanmaken van het project!**

Dit voorkomt de 404 error vanaf het begin.

---

**Na het opnieuw aanmaken zou alles direct moeten werken!** 🚀
