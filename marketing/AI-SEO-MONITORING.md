# SEO & AI-zichtbaarheid — SeniorEase Library

Laatst bijgewerkt: **9 juli 2026**

---

## Stap 1 — Google Search Console (nu doen)

### A. Property toevoegen

1. Ga naar [Google Search Console](https://search.google.com/search-console/welcome)
2. Kies **URL-prefix**: `https://seniorease.eu/`
3. Kies verificatiemethode **HTML-tag**
4. Kopieer alleen de **content-waarde** uit de meta-tag (het lange token tussen aanhalingstekens)

### B. Verificatiecode in project zetten

Voeg toe aan `website/.env`:

```
GOOGLE_SITE_VERIFICATION=jouw_token_hier
```

Daarna build + deploy:

```powershell
cd website
node build.js
vercel --prod
```

De build injecteert de meta-tag automatisch in alle HTML-pagina's.

**Alternatief (zonder .env):** plaats het HTML-bestand dat Google geeft (bijv. `google123abc.html`) in de map `website/` — de build kopieert het naar `public/`.

### C. Sitemap indienen

Na verificatie in Search Console:

1. Menu **Sitemaps** (links)
2. **Gebruik de volledige www-URL** (niet alleen `sitemap.xml`):
   ```
   https://www.seniorease.eu/sitemap.xml
   ```
3. Versturen

> **Let op:** `https://seniorease.eu/sitemap.xml` (zonder www) redirect naar www en geeft soms "Kan niet worden gelezen" in Search Console. Gebruik altijd de **www**-URL hierboven.

### D. Basiscontroles (eenmalig)

| Check | Waar |
|-------|------|
| Indexering homepage | URL-inspectie → `https://seniorease.eu/` → Indexering aanvragen |
| Indexering NL | URL-inspectie → `https://seniorease.eu/nl/` |
| robots.txt | `https://seniorease.eu/robots.txt` |
| sitemap | `https://seniorease.eu/sitemap.xml` |

### E. Huidige status (9 juli 2026)

- `site:seniorease.eu` → **homepage wordt al geïndexeerd** door Google
- Search Console property nog te verifiëren voor crawl-statistieken en sitemap-monitoring

---

## Stap 2 — Maandelijkse AI-check

Gebruik deze queries **1× per maand** in Google (AI Overview), Perplexity, ChatGPT met search, en Claude.

### Engels

| Query | SeniorEase geciteerd? (jul 2026) | Wie wél? |
|-------|----------------------------------|----------|
| Android app to track book collection without account | Nee | Books Tracker, Booky, LibriShelf, Openreads |
| barcode scanner app for books and vinyl offline | Nee | Booky, Books Tracker |
| private book catalog app Android no cloud | Nee | Booky, LibriShelf, Booke |
| never buy duplicate books app | Nee | Booky, BookItUp, bibliofy, My Library |
| SeniorEase Library app review | Ja (eigen site) | — |

### Nederlands

| Query | SeniorEase geciteerd? (jul 2026) | Wie wél? |
|-------|----------------------------------|----------|
| app om boekencollectie bij te houden zonder account Android | Nee | Books Tracker, Booky, Booke, Offline Books Database |
| barcode scanner app boeken offline | Nee | Booky, Books Tracker |
| app platen vinyl collectie bijhouden | Nog testen | — |
| dubbele boeken kopen voorkomen app | Nog testen | — |

### Actie na check

- [ ] Prijs en limieten kloppen met site + Play Store?
- [ ] `Last updated` in `llms.txt` bijgewerkt?
- [ ] Nieuwe FAQ's toegevoegd aan homepage + JSON-LD?

---

## Stap 3 — Blog (long-tail SEO)

Nieuwe artikelen (juli 2026):

- EN: `/blog/book-tracker-app-no-account/`
- EN: `/blog/vinyl-collection-spreadsheet-alternative/`
- NL: `/nl/blog/boeken-tracker-app-zonder-account/`
- NL: `/nl/blog/vinyl-collectie-spreadsheet-alternatief/`

**Volgende onderwerpen (ideeën):**

- `dvd collection app no subscription`
- `how to catalog home library barcode`
- `discogs alternative for small collection`

---

## Stap 4 — Backlinks (klaar om te posten)

Post als **Maureen** (authentiek, geen spam). Pas aan waar nodig. Eén post per subreddit, niet overal tegelijk.

### Reddit r/books (EN)

**Titel:** I built a simple Android app to stop buying books I already own — no account needed

**Body:**

> I'm a collector (books + vinyl) and kept buying duplicates at charity shops because I couldn't remember what was on my shelf at home.
>
> I tried spreadsheets and Goodreads but wanted something calmer: scan the ISBN barcode, done. No login, data stays on the phone, works offline.
>
> I ended up building SeniorEase Library myself — it also handles music, DVDs and games in one app. Free for 10 items, €4.99 one-time for unlimited (no subscription).
>
> Android only, I'm afraid — no iOS version.
>
> If anyone's interested: https://seniorease.eu/blog/book-tracker-app-no-account/
>
> Happy to answer questions — and curious how others track their shelves!

### Reddit r/vinyl (EN)

**Titel:** Spreadsheet didn't work at record fairs — I switched to barcode scanning on my phone

**Body:**

> After my third duplicate Miles Davis pressing I knew my spreadsheet was useless when I'm standing in front of a crate at a fair.
>
> I scan barcodes at home (vinyl, CDs, books — same app). At the fair I just search my phone before I buy.
>
> SeniorEase Library — Android only, no account, offline catalog. Wrote up what worked for me: https://seniorease.eu/blog/vinyl-collection-spreadsheet-alternative/
>
> How do you check your collection when you're out digging?

### Reddit r/thenetherlands of r/NLquestions (NL)

**Titel:** App-tip voor verzamelaars: boeken/platen bijhouden zonder account?

**Body:**

> Ik ben zelf senior en verzamel boeken en vinyl. Steeds dubbel gekocht op markten omdat ik thuis niet meer wist wat ik al had.
>
> Ik heb zelf een Android-app gebouwd: barcode scannen, klaar. Geen account, gegevens blijven op je telefoon, werkt offline. Ook voor dvd's en games.
>
> Gratis tot 10 items, daarna eenmalig €4,99 — geen abonnement.
>
> https://seniorease.eu/nl/blog/boeken-tracker-app-zonder-account/
>
> Hoe houden jullie jullie collectie bij?

### Forums / Facebook-groepen

Zoek groepen: "platenbeurs", "boekenverzamelaars", "vinyl collectors NL". Reageer op bestaande vragen over catalogiseren met een korte tip + link naar relevant blogartikel (niet alleen de homepage).

---

## Bestanden in dit project

| Bestand | Doel |
|---------|------|
| `website/robots.txt` | Crawlers + sitemap-URL |
| `website/sitemap.xml` | Alle indexeerbare pagina's |
| `website/llms.txt` | AI-assistenten (ChatGPT, Claude, Perplexity) |
| `website/.env` | `GOOGLE_SITE_VERIFICATION` token |
| `website/build.js` | Injecteert GSC-tag + Vercel Analytics |
