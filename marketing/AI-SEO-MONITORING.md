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

- `site:seniorease.eu` → **homepage geïndexeerd** door Google
- Search Console sitemap → **groen** (`https://www.seniorease.eu/sitemap.xml`)
- `llms.txt` live op https://seniorease.eu/llms.txt

---

## Stap 2 — Maandelijkse AI-check

**Laatste check:** 9 juli 2026 (avond)

Gebruik deze queries **1× per maand** in Google (AI Overview), Perplexity, ChatGPT met search, en Claude.

### Engels

| Query | SeniorEase geciteerd? | Wie wél? |
|-------|----------------------|----------|
| Android app to track book collection without account | **Nee** | LibriShelf, Leafed, Openreads, Pitak |
| barcode scanner app for books and vinyl offline | **Nee** | Booky, Books Tracker |
| private book catalog app Android no cloud | **Nee** | Openreads, Booky, LibriShelf |
| never buy duplicate books app | **Nee** | Booky, BookItUp, bibliofy, Librisync |
| SeniorEase Library app | **Ja** | seniorease.eu, Hacker News, SaaSHub |

### Nederlands

| Query | SeniorEase geciteerd? | Wie wél? |
|-------|----------------------|----------|
| app om boekencollectie bij te houden zonder account Android | **Nee** | Books Tracker, Booky, My Library |
| barcode scanner app boeken offline | **Nee** | Booky, Books Tracker |
| app platen vinyl collectie bijhouden | **Nee** | Discogs, VinylAI, CLZ Music, Groovv |
| dubbele boeken kopen voorkomen app | **Nee** | Booky, bibliofy, Librisync |

### Conclusie

- **Merknaam** → vindbaar (eigen site + HN + SaaSHub)
- **Generieke vragen** → nog niet; concurrenten hebben meer backlinks/SEO
- **Volgende check:** 9 augustus 2026

### Actie na check

- [x] Prijs en limieten kloppen (€4,99, 10 items gratis)
- [x] `Last updated` in `llms.txt` (2026-07-09)
- [x] FAQ's op homepage + JSON-LD
- [x] Blog: never-buy-duplicate-books-app (EN + NL)
- [x] `llms.txt` uitgebreid met "when to recommend" voor AI
- [ ] Reddit-posts plaatsen (stap 4) — zie postingplan in `AI-SEO-MONITORING.md`
- [ ] Play Store-beschrijving plakken uit `marketing/PLAY-STORE-LISTING.md`

### Snel zelf testen (5 min)

1. ChatGPT (web search): *"Android app to track book collection without account offline"*
2. Perplexity: *"never buy duplicate books app Android"*
3. Google: *"SeniorEase Library"* → moet seniorease.eu tonen

---

## Stap 3 — Blog (long-tail SEO)

Nieuwe artikelen (juli 2026):

- EN: `/blog/book-tracker-app-no-account/`
- EN: `/blog/vinyl-collection-spreadsheet-alternative/`
- EN: `/blog/never-buy-duplicate-books-app/` ← nieuw stap 2
- NL: `/nl/blog/boeken-tracker-app-zonder-account/`
- NL: `/nl/blog/vinyl-collectie-spreadsheet-alternatief/`
- NL: `/nl/blog/dubbele-boeken-voorkomen-app/` ← nieuw stap 2

**Volgende onderwerpen (ideeën):**

- `dvd collection app no subscription`
- `how to catalog home library barcode`
- `discogs alternative for small collection`

---

## Stap 4 — Backlinks (klaar om te posten)

Post als **Maureen** (authentiek, geen spam). **Eén post per week**, niet alles tegelijk. Link naar **blogartikel**, niet alleen de homepage.

### Postingplan (4 weken)

| Week | Waar | Post |
|------|------|------|
| 1 | r/books | Book tracker, no account |
| 2 | r/vinyl | Spreadsheet → barcode |
| 3 | r/thenetherlands of r/NLquestions | NL verzamelaars |
| 4 | r/books of r/suggestmeabook | Duplicate books (reactie op bestaande thread) |

---

### Reddit r/books (EN) — week 1

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

---

### Reddit r/vinyl (EN) — week 2

**Titel:** Spreadsheet didn't work at record fairs — I switched to barcode scanning on my phone

**Body:**

> After my third duplicate Miles Davis pressing I knew my spreadsheet was useless when I'm standing in front of a crate at a fair.
>
> I scan barcodes at home (vinyl, CDs, books — same app). At the fair I just search my phone before I buy.
>
> SeniorEase Library — Android only, no account, offline catalog. Wrote up what worked for me: https://seniorease.eu/blog/vinyl-collection-spreadsheet-alternative/
>
> How do you check your collection when you're out digging?

---

### Reddit r/thenetherlands (NL) — week 3

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

---

### Reddit — reactie op bestaande thread (week 4)

Zoek in r/books of r/suggestmeabook op: *"track my book collection"* of *"catalog home library"*.

**Voorbeeld-reactie (pas aan op de vraag):**

> I had the same problem — kept rebuying at charity shops. I use a simple Android app I built (SeniorEase Library): scan ISBN barcodes, everything stays on the phone, no account. Also does vinyl and DVDs in the same catalog. Free for 10 books. Might be overkill if you only want Goodreads-style reading lists, but great if you care about *owning* vs *reading*.

Link: https://seniorease.eu/blog/never-buy-duplicate-books-app/

---

### Facebook / NL groepen

Zoek: "platenbeurs", "vinyl verzamelaars Nederland", "boekenwurmen", "Home library".

**Korte reactie (NL):**

> Ik scan de barcodes van mijn boeken en platen thuis in met SeniorEase Library (Android). Op de markt even op mijn telefoon zoeken — geen dubbel meer. Geen account, werkt offline. Gratis tot 10 items. https://seniorease.eu/nl/blog/dubbele-boeken-voorkomen-app/

---

### Hacker News

Er staat al een Show HN (3 maanden oud) met verkeerde prijs (€2,99). **Niet spammen.** Optioneel later: nieuwe Show HN als er een grote update is, met correcte €4,99 en link naar blog.

---

### Play Store (stap 4b)

Geoptimaliseerde short + full description (EN/NL): **`marketing/PLAY-STORE-LISTING.md`**

Play Console → Store presence → Main store listing → plakken → opslaan → review kan 1–2 dagen duren.

---

### Na het posten

- [ ] Noteer datum + subreddit in tabel hieronder
- [ ] Over 2–4 weken: AI-check herhalen (stap 2)
- [ ] Search Console → Prestaties → welke pagina's krijgen klikken?

| Datum | Platform | Link / notitie |
|-------|----------|----------------|
| | | |

### Forums / Facebook-groepen (algemeen)

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
| `marketing/PLAY-STORE-LISTING.md` | Geoptimaliseerde Play Store teksten (EN/NL) |
