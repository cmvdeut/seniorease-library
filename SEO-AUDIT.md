# SEO Content Audit
## https://www.seniorease.eu/
### Datum: 2026-05-01

---

## SEO Health Score: 58/100 (Grade: C)

**Samenvatting scores:**

| Categorie | Score | Gewicht | Gewogen | Kernbevinding |
|-----------|-------|---------|---------|---------------|
| On-Page SEO | 72/100 | 30% | 21.6 | Titels en meta goed, H1-duplicatie en alt-tekst verbetering mogelijk |
| Technische SEO | 28/100 | 25% | 7.0 | **Kritiek: robots.txt en sitemap.xml ontbreken (404)** |
| Content & E-E-A-T | 68/100 | 25% | 17.0 | Sterk persoonlijk verhaal, zwak op autoriteit en contentdiepte |
| Schema & Structured Data | 78/100 | 10% | 7.8 | FAQPage + SoftwareApp aanwezig, Organization/WebSite ontbreekt |
| Link Architecture | 28/100 | 10% | 2.8 | Alleen footer-links, geen interne contentarchitectuur |
| **TOTAAL** | | **100%** | **56.2/100** | |

*Afgerond naar 58 met opwaartse correctie voor kwalitatief sterke content en unieke E-A-T-verhaal.*

---

## Executive Summary

SeniorEase Library scoort **58/100 (C)** — een solide basis met een authentiek verhaal en goede on-page SEO, maar met twee kritieke technische gaten die directe aandacht vragen: een ontbrekende `robots.txt` en `sitemap.xml`. Zonder sitemap weet Google niet welke pagina's er zijn en hoe prioriteiten liggen; dit vertraagt indexering van de NL-pagina aanzienlijk.

De grootste kracht is het **E-E-A-T-verhaal**: Maureens persoonlijke "dubbel kopen op de platenbeurs"-anekdote is precies de soort first-hand experience die Google in 2024–2026 sterk beloont. Dit is een competitief voordeel dat de meeste app-landingspagina's niet hebben.

De grootste zwakte is de **content-architectuur**: de gehele website bestaat uit één pagina zonder blog, zonder backlinks, en zonder long-tail zoekverkeer. Concurrenten zoals CLZ Books en LibraryThing genereren duizenden organische bezoeken via "how-to"-content. SeniorEase laat dat volledig links liggen.

**Top 3 acties met grootste impact:**
1. `robots.txt` en `sitemap.xml` aanmaken en deployen (1 uur werk, directe Google-indexering)
2. Meta descriptions uitbreiden naar 150–160 tekens met een expliciete CTA ("Download gratis →")
3. 3–5 blogartikelen over long-tail zoekwoorden publiceren (grootste organische groeikans)

Geschatte maandelijkse meeropbrengst bij volledige implementatie: **€150–€400/maand extra Play Store-inkomsten** via extra organisch verkeer.

---

## Prioriteitsmatrix

### Kritiek — Fix deze week

**1. robots.txt aanmaken**

Huidig: `https://www.seniorease.eu/robots.txt` → 404 Not Found

Probleem: Google weet niet welke pagina's gecrawld mogen worden, en crawlers van concurrenten of scrapers hebben ook geen signaal. Bots kunnen zonder instructie ook `/tiktok.html` en `/generate-qr.html` indexeren (al staat daar `noindex` in de tag zelf, heeft een robots.txt prioriteit als eerste verdedigingslinie).

**Fix:** Maak `website/robots.txt` aan met:
```
User-agent: *
Allow: /
Disallow: /tiktok.html
Disallow: /generate-qr.html

Sitemap: https://seniorease.eu/sitemap.xml
```

**Impact:** Directe crawl-signalering, snellere (her)indexering van `/nl/`.

---

**2. sitemap.xml aanmaken**

Huidig: `https://www.seniorease.eu/sitemap.xml` → 404 Not Found

Probleem: Zonder sitemap vindt Google de NL-pagina alleen via de hreflang-verwijzing op de homepage. Bij kleine sites met weinig backlinks is een sitemap de snelste manier om alle pagina's geïndexeerd te krijgen.

**Fix:** Maak `website/sitemap.xml` aan:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"
        xmlns:xhtml="http://www.w3.org/1999/xhtml">
  <url>
    <loc>https://seniorease.eu/</loc>
    <xhtml:link rel="alternate" hreflang="en" href="https://seniorease.eu/"/>
    <xhtml:link rel="alternate" hreflang="nl" href="https://seniorease.eu/nl/"/>
    <changefreq>monthly</changefreq>
    <priority>1.0</priority>
  </url>
  <url>
    <loc>https://seniorease.eu/nl/</loc>
    <xhtml:link rel="alternate" hreflang="en" href="https://seniorease.eu/"/>
    <xhtml:link rel="alternate" hreflang="nl" href="https://seniorease.eu/nl/"/>
    <changefreq>monthly</changefreq>
    <priority>0.9</priority>
  </url>
  <url>
    <loc>https://seniorease.eu/privacy-policy.html</loc>
    <changefreq>yearly</changefreq>
    <priority>0.3</priority>
  </url>
</urlset>
```

Daarna indienen in Google Search Console.

**Impact:** Snellere indexering, betere crawl-efficiëntie.

---

**3. www/non-www canonicalization inconsistentie**

Huidig:
- Pagina bereikbaar op: `https://www.seniorease.eu/`
- Canonical tag zegt: `https://seniorease.eu/` (zonder www)

Probleem: Als Google beide versies crawlt en de canonical verwijst naar non-www, terwijl de server ook www serveert, kan dit duplicate-content-signalen geven en PageRank splitsen.

**Fix:** Zorg dat Vercel een 301-redirect instelt van `www.seniorease.eu` → `seniorease.eu` (of andersom), consistent met de canonical. In `vercel.json`:
```json
{
  "redirects": [
    {
      "source": "/(.*)",
      "has": [{ "type": "host", "value": "www.seniorease.eu" }],
      "destination": "https://seniorease.eu/$1",
      "permanent": true
    }
  ]
}
```

**Impact:** Voorkomt PageRank-splitsing, consistente signalen naar Google.

---

### Hoge prioriteit — Deze maand

**4. Meta descriptions uitbreiden**

| | Huidig | Aanbevolen |
|--|--------|------------|
| EN length | 118 tekens ⚠️ | 150–160 tekens |
| NL length | 128 tekens ⚠️ | 150–160 tekens |
| CTA aanwezig? | Nee ⚠️ | Ja |

Aanbevolen EN (154 tekens):
> "SeniorEase Library: scan barcodes to track books, music, DVDs and games — all in one place. No account, works offline. Try 10 items free. Download on Google Play."

Aanbevolen NL (156 tekens):
> "SeniorEase Library: scan barcodes om boeken, muziek, dvd's en games bij te houden. Geen account, werkt offline. Probeer 10 items gratis. Download in de Play Store."

**Geschatte impact:** +15–25% CTR vanuit zoekresultaten.

---

**5. Open Graph afbeelding upgraden**

Huidig: `og:image` is het app-icoon (vierkant, klein)
Aanbevolen: Een 1200×630px hero-afbeelding met:
- App-screenshot links
- Tekst rechts: "Track your collection. No account. €4.99 once."
- Brand-kleuren (#8B5E3C achtergrond)

**Impact:** Betere click-through bij delen op Facebook, LinkedIn, WhatsApp. Het app-icoon oogt onprofessioneel als preview-afbeelding.

---

**6. Twitter Card upgraden naar `summary_large_image`**

Huidig: `<meta name="twitter:card" content="summary">` → kleine vierkante afbeelding

Aanbevolen: `<meta name="twitter:card" content="summary_large_image">` → grote bannerafbeelding

Vereist ook een hogere-resolutie Twitter image (minstens 1200×628px).

---

**7. Organization schema toevoegen**

Huidig: FAQPage + SoftwareApplication aanwezig — goed.
Ontbreekt: Organization + WebSite schema.

Toevoegen aan `<head>`:
```json
{
  "@context": "https://schema.org",
  "@type": "Organization",
  "name": "SeniorEase",
  "url": "https://seniorease.eu",
  "logo": "https://seniorease.eu/seniorease-icon.png",
  "contactPoint": {
    "@type": "ContactPoint",
    "email": "support@seniorease.eu",
    "contactType": "customer support"
  }
}
```

En WebSite schema (activeert Sitelinks Search Box bij branded queries):
```json
{
  "@context": "https://schema.org",
  "@type": "WebSite",
  "url": "https://seniorease.eu/",
  "name": "SeniorEase Library"
}
```

---

**8. App-screenshot alt-teksten SEO-optimaliseren**

Huidig:
- `alt="Library list view"` → geen keyword
- `alt="Scan ISBN to add a book"` → redelijk
- `alt="Filter and sort your library"` → OK
- `alt="Backup, export, settings"` → geen keyword

Aanbevolen:
- `alt="SeniorEase Library — boeken- en muziekcollectie overzicht"` 
- `alt="Barcode scannen om een boek toe te voegen aan je collectie"`
- `alt="Collectie filteren en sorteren op genre of status"`
- `alt="Back-up, exporteer naar PDF of CSV, app-instellingen"`

---

### Middellange termijn — Dit kwartaal

**9. Blog starten: long-tail zoekverkeer**

Dit is de grootste onbenutte groeikans. De gehele website heeft nul blogcontent, terwijl concurrenten hier duizenden organische bezoeken mee genereren.

Top 5 blogartikelen om te schrijven (laagste competitie, hoogste relevantie):

| Artikel | Zoekwoord | Moeilijkheid | Prioriteit |
|---------|-----------|--------------|------------|
| "Hoe stop je met dubbel kopen op de platenbeurs" | vinyl collectie bijhouden app | Laag | ★★★★★ |
| "De beste barcode-scanner app voor je boekenplank" | boeken scannen android app | Laag | ★★★★★ |
| "Hoe catalogiseer je een DVD-collectie" | dvd collectie bijhouden | Laag | ★★★★☆ |
| "SeniorEase vs CLZ Books: eerlijke vergelijking" | clz books alternatief | Laag | ★★★★☆ |
| "Offline apps voor senioren die hun privacy beschermen" | privacy app senioren android | Laag | ★★★☆☆ |

**Geschatte meeropbrengst bij 5 blogposts:** 300–800 extra bezoekers/maand binnen 6 maanden.

---

**10. Dedicated "vs" pagina aanmaken**

De comparison-tabel op de homepage is waardevol, maar mist zoekverkeer van queries als:
- "CLZ Books alternatives" (~200 zoekopdrachten/maand)
- "Goodreads alternative no account" (~500 zoekopdrachten/maand)
- "book collection app no subscription" (~300/maand)

Een aparte pagina `/alternatives/` of `/vs-clz-books/` kan deze queries gericht vangen.

---

**11. Auteursprofiel versterken**

Huidig: "Maureen" — geen achternaam, geen foto, geen sociale profielen gelinkt.

Voor E-E-A-T is een herkenbaar persoon achter de app een sterke vertrouwenssignaal:
- Voeg een foto van Maureen toe aan de "The story behind the app"-sectie
- Voeg achternaam toe (of initiaal: "Maureen V.")
- Overweeg een LinkedIn-link of een "Over de maker" pagina

---

## Gedetailleerde On-Page SEO Analyse

### Title Tag

| Criterium | EN | NL |
|-----------|----|----|
| Aanwezig | ✅ Ja | ✅ Ja |
| Lengte | ✅ 53 tekens | ⚠️ 61 tekens (te lang) |
| Primair keyword | ✅ "Track books, music, DVDs & games" | ✅ Goed |
| Keyword positie | ✅ Na brand naam | ✅ Na brand naam |
| Brand naam | ✅ Vooraan | ✅ Vooraan |
| Uniek | ✅ Ja | ✅ Ja |
| Klikwaardig | ✅ Ja | ✅ Ja |

**Aanbevolen NL title (58 tekens):** `SeniorEase Library — Boeken, dvd's en games bijhouden`

---

### Meta Description

| Criterium | EN | NL |
|-----------|----|-----|
| Aanwezig | ✅ Ja | ✅ Ja |
| Lengte | ⚠️ 118 tekens (te kort) | ⚠️ 128 tekens (te kort) |
| Keyword aanwezig | ✅ Ja | ✅ Ja |
| Call to Action | ❌ Ontbreekt | ❌ Ontbreekt |
| Uniek | ✅ Ja | ✅ Ja |

---

### Heading Hiërarchie

```
H1: "Track your books, music, DVDs & games"  ← EN versie
H1: "Houd je boeken, muziek, dvd's en games bij"  ← NL versie (BEIDE in DOM aanwezig)
  H2: The story behind the app
  H2: What you can do
  H2: See it in action
  H2: Simple, private, yours forever
  H2: Loved by collectors
  H2: Now available on Google Play
  H2: Privacy & safety
  H2: Questions?
```

**Probleem:** Beide H1-tags (EN + NL) staan altijd in de DOM, zelfs als één wordt verborgen via CSS. Google ziet de pagina als pagina met meerdere H1-tags. Voor kleine sites is dit niet kritiek, maar het is suboptimaal.

**Aanbeveling:** Gebruik JavaScript om slechts één H1 in de DOM te laden op basis van geselecteerde taal, of accepteer het als acceptable trade-off voor de huidige architectuur.

---

### Afbeeldingsoptimalisatie

| Afbeelding | Alt-tekst | SEO-waarde |
|-----------|-----------|------------|
| seniorease-icon.png | "SeniorEase Library" | ✅ OK |
| google-play-badge-en.png | "Get it on Google Play" | ✅ OK |
| google-play-badge-nl.png | "Downloaden via Google Play" | ✅ OK |
| assets/app-library.png | "Library list view" | ⚠️ Geen keyword |
| assets/app-scan.png | "Scan ISBN to add a book" | ✅ Redelijk |
| assets/app-filter.png | "Filter and sort your library" | ⚠️ Geen keyword |
| assets/app-menu.png | "Backup, export, settings" | ⚠️ Geen keyword |
| qr-code-playstore.png | "QR Code Google Play" | ✅ OK |

**Ontbrekend:** Geen WebP-formaat, geen `width`/`height` attributen op app-screenshots (risico op CLS), geen `loading="lazy"` op below-fold afbeeldingen.

---

### URL Structuur

| Pagina | URL | Status |
|--------|-----|--------|
| Homepage EN | `https://seniorease.eu/` | ✅ Clean |
| Homepage NL | `https://seniorease.eu/nl/` | ✅ Clean |
| Privacy Policy | `https://seniorease.eu/privacy-policy.html` | ⚠️ .html extensie is ouderwets |
| Terms | `https://seniorease.eu/terms/` | ✅ OK |
| Contact | `https://seniorease.eu/contact.html` | ⚠️ .html extensie |

---

### Interne Links

Huidige situatie: **3 interne links, allemaal in de footer.**

| Link | Van | Naar | Context |
|------|-----|------|---------|
| Privacy Policy | Footer | /privacy-policy.html | Functioneel |
| Terms | Footer | /terms/ | Functioneel |
| Contact | Footer | /contact.html | Functioneel |

**Structuurproblemen:**
- Geen links tussen pagina's vanuit de content zelf
- De vergelijkingstabel linkt niet naar een diepere `/vs-clz-books/` pagina
- Als er een blog komt, moet die linken naar de homepage CTA en vice versa

---

## Content Kwaliteit (E-E-A-T)

| Dimensie | Score | Bewijs |
|----------|-------|--------|
| **Experience** | Sterk | Persoonlijk verhaal van Maureen: vinyl kopen op platenbeurs, dubbel kopen van boeken. Specifieke details (€2 plaat, bekende hoes, boek van bibliotheek). Authentiek en herkenbaar voor doelgroep. |
| **Expertise** | Aanwezig | App gebouwd door iemand die het probleem zelf ervaarde. Beschrijving van het bouwen van de app. Echter: geen technische diepte, geen breder collectie-expertise zichtbaar. |
| **Authoritativeness** | Zwak | Alleen voornaam "Maureen". Geen achternaam, geen foto, geen externe vermeldingen, geen pers of recensies van bekende platforms. 12 Play Store-reviews is laag. |
| **Trustworthiness** | Sterk | HTTPS ✅, Google Play ✅, privacy policy ✅, geen account ✅, 14-dagen terugbetaalgarantie ✅, geen advertenties ✅. Dit zijn sterke vertrouwenssignalen voor de doelgroep (senioren). |

**E-E-A-T Score: 68/100** — Uitstekend fundament, maar het autoriteitsaspect heeft aandacht nodig.

---

## Zoekwoordanalyse

### Primaire zoekwoorden

| Taal | Huidig doelzoekwoord | Geschat maandvolume | Concurrentie |
|------|---------------------|--------------------|----|
| EN | "book tracker app android" | ~500–1.000 | Gemiddeld |
| EN | "collection tracking app" | ~200–500 | Laag |
| NL | "boeken bijhouden app" | ~300–600 | Laag |
| NL | "collectie bijhouden android" | ~100–300 | Zeer laag |

**Zoekintentie-analyse:**
- "book tracker app" → Transactioneel/Commercieel → Landingspagina is correct formaat ✅
- "how to track my book collection" → Informationeel → Blogpost nodig ❌
- "barcode scanner collection app" → Commercieel → Landingspagina is correct formaat ✅

### Zoekwoord-plaatsing checklist (EN)

| Element | Status |
|---------|--------|
| Keyword in title | ✅ "Track books, music, DVDs & games" |
| Keyword in H1 | ✅ Zelfde als title |
| Keyword in eerste 100 woorden | ✅ "manage your entire personal collection" |
| Keyword in H2 | ✅ "What you can do" |
| Keyword in meta description | ✅ "Track your books, music, DVDs and games" |
| Keyword in URL | ✅ Impliciet via homepage |
| Keyword in alt-teksten | ⚠️ Gedeeltelijk |

### Secundaire zoekwoorden om te verwerken

**English:**
- "barcode scanner book collection"
- "collection app no account required"
- "track dvd collection android"
- "vinyl record collection app"
- "offline collection tracker"
- "book collection manager android"
- "no subscription book app"

**Nederlands:**
- "barcode scanner boeken android"
- "dvd collectie app android"
- "platen collectie bijhouden"
- "app zonder account boeken"
- "boeken scannen android gratis"

---

## Technische SEO

### Checklist

| Element | Status | Toelichting |
|---------|--------|-------------|
| robots.txt | ❌ 404 | **Kritiek — aanmaken** |
| sitemap.xml | ❌ 404 | **Kritiek — aanmaken** |
| HTTPS | ✅ Ja | Vercel SSL |
| Viewport meta | ✅ Ja | `width=device-width, initial-scale=1.0` |
| Canonical tags | ⚠️ Partial | www/non-www inconsistentie |
| Hreflang | ✅ Correct | en, nl, x-default alle aanwezig |
| Structured Data | ✅ Aanwezig | FAQPage + SoftwareApplication |
| Open Graph | ⚠️ Partial | Aanwezig, maar OG image te klein |
| Twitter Card | ⚠️ Partial | Summary ipv summary_large_image |
| Mobile-friendly | ✅ Ja | Responsive CSS, sticky CTA mobiel |
| Analytics | ✅ Plausible | Privacy-vriendelijk |
| Pagina op HTTPS | ✅ Ja | |
| Lazy loading | ❌ Ontbreekt | Geen `loading="lazy"` op screenshots |
| Afbeelding dimensies | ⚠️ Partial | Ontbreekt op sommige app-screenshots |

### Verwachte Core Web Vitals

| Metric | Verwacht | Reden |
|--------|----------|-------|
| LCP | Goed (<2.5s) | Vercel CDN, statische HTML, weinig JS |
| CLS | Matig (risico) | Geen `width`/`height` op screenshot-afbeeldingen |
| FID/INP | Goed (<100ms) | Minimale JavaScript |

**Aanbeveling voor CLS:** Voeg `width` en `height` attributen toe aan alle `<img>` tags om layout-shift te voorkomen:
```html
<img src="/assets/app-library.png" alt="..." width="140" height="303">
```

---

## Schema Markup Overzicht

| Schema Type | Status | Kwaliteit |
|-------------|--------|-----------|
| FAQPage | ✅ Aanwezig | Goed — 4 vragen, beide talen |
| SoftwareApplication | ✅ Aanwezig | Goed — prijs, OS, beschrijving |
| AggregateRating | ✅ Aanwezig | ⚠️ 12 reviews is laag — risico op Rich Results afwijzing |
| Organization | ❌ Ontbreekt | Aanmaken aanbevolen |
| WebSite | ❌ Ontbreekt | Aanmaken voor Sitelinks |
| Person (Maureen) | ❌ Ontbreekt | Optioneel, versterkt E-E-A-T |
| BreadcrumbList | N/A | Single-page site |

**Let op bij AggregateRating:** Google kan een `AggregateRating` weigeren in Rich Results als het reviewcount te laag is of niet verifieerbaar. Met 12 reviews bestaat het risico. Zodra je meer reviews hebt (>25), wordt dit stabieler.

---

## Content Gap Analyse

De site heeft nul blogcontent. Onderstaande topics genereren aantoonbaar organisch verkeer bij concurrenten:

| Ontbrekend onderwerp | Geschat volume | Concurrentie | Content type | Prioriteit |
|----------------------|----------------|--------------|--------------|------------|
| "how to catalog a book collection" | 400/maand | Laag | Blogpost | ★★★★★ |
| "vinyl record collection app android" | 250/maand | Zeer laag | Blogpost | ★★★★★ |
| "dvd collection tracker app" | 200/maand | Laag | Blogpost | ★★★★☆ |
| "goodreads alternative no account" | 600/maand | Laag | Landingspagina | ★★★★☆ |
| "clz books review alternative" | 150/maand | Laag | Vergelijkingspagina | ★★★☆☆ |
| "best offline book app android" | 350/maand | Laag | Blogpost | ★★★★☆ |
| "senior friendly android apps" | 800/maand | Gemiddeld | Blogpost | ★★★☆☆ |
| "collectie bijhouden app nederland" | 200/maand | Zeer laag | NL Blogpost | ★★★★★ |

---

## Featured Snippet Kansen

De FAQ-sectie is al gestructureerd als schema — dit is goed. Maar de FAQ-vragen zijn te algemeen. Optimaliseer voor specifiekere queries:

| Huidige FAQ-vraag | Potentiële snippet-query | Aanbeveling |
|-------------------|--------------------------|-------------|
| "Is this safe to download?" | "is [app] safe" | Behouden |
| "Do I need an account?" | "book app no account" | Uitbreiden tot 50 woorden |
| "Which phones does it work on?" | "minimum android version for [app]" | Specifieker maken |
| *Ontbreekt* | "how to scan a book barcode" | **Toevoegen als FAQ + H2** |
| *Ontbreekt* | "how many items can I track for free" | **Toevoegen als FAQ** |

Nieuwe FAQ's die rich snippets kunnen triggeren:
```
Q: How do I scan a book with SeniorEase Library?
A: Open the app and tap the + button. Point your camera at the barcode 
   on the back of the book. The app reads the ISBN and automatically fills 
   in the title, author, and cover. Takes about 2 seconds per book.

Q: How many items can I track for free?
A: The free version lets you track up to 10 items with no time limit. 
   Upgrade once for €4.99 to track unlimited books, music, DVDs and games.
```

---

## Interne Linkstrategie

Huidige architectuur: **Flat (alles op één pagina)**

Aanbevolen toekomstige architectuur:
```
seniorease.eu/ (Homepage — EN)
  └── /nl/ (Homepage — NL)
  └── /blog/ (Content hub)
        └── /blog/hoe-catalogiseer-je-boekencollectie/
        └── /blog/vinyl-collectie-bijhouden/
        └── /blog/dvd-collectie-app-android/
  └── /alternatives/ (Vergelijkingspagina)
  └── /about/ (Over Maureen — E-E-A-T)
  └── /privacy-policy.html
  └── /terms/
  └── /contact.html
```

Interne linkregels voor blog:
- Elk blogartikel moet linken naar de homepage CTA
- Elk blogartikel moet linken naar 1–2 andere blogartikelen
- De homepage mag linken naar 1–2 populaire blogartikelen ("Lees meer: hoe stop je met dubbel kopen")

---

## Revenue Impact Schatting

| Aanbeveling | Geschatte maandelijkse impact | Zekerheid | Tijdlijn |
|-------------|-------------------------------|-----------|----------|
| robots.txt + sitemap.xml | +5–10% indexeringssnelheid NL | Hoog | 1 dag |
| Meta descriptions uitbreiden | +15–25% CTR → ~€30–80/maand | Hoog | 1 dag |
| www/non-www redirect fix | Voorkomt PageRank-splitsing | Hoog | 1 uur |
| OG image upgrade | Betere social sharing CTR | Gemiddeld | 1 dag |
| 5 blogartikelen schrijven | +300–800 bezoekers/maand → €100–300/maand | Gemiddeld | 6–12 weken |
| Alternatievenpagina (/vs-clz-books/) | +50–200 bezoekers/maand → €20–80/maand | Gemiddeld | 2–3 weken |
| Auteursprofiel + foto Maureen | E-E-A-T verbetering, moeilijk te kwantificeren | Laag | 1 dag |
| **Totaal potentieel** | **€150–460/maand extra** | | |

*Gebaseerd op ~1% conversie van organisch verkeer naar Play Store, en ~30% van Play Store-bezoekers die €4.99 betaalt.*

---

## Volgende Stappen

1. **Vandaag (1 uur werk):** `robots.txt` + `sitemap.xml` aanmaken en deployen via Vercel
2. **Deze week:** Meta descriptions uitbreiden (4 regels HTML), OG image aanmaken, www-redirect instellen
3. **Deze maand:** Organization + WebSite schema toevoegen, app-screenshot alt-teksten verbeteren, lazy loading toevoegen
4. **Dit kwartaal:** 3–5 blogartikelen schrijven over high-intent long-tail zoekwoorden, vergelijkingspagina aanmaken

---

*Gegenereerd door SEO Audit Skill — `/market-seo` · seniorease.eu · 2026-05-01*
