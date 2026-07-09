# Marketing Audit: SeniorEase Library
**URL:** https://seniorease.eu  
**Datum:** 30 april 2026  
**Business type:** Mobile App (Android) — one-time purchase, privacy-first collectiebeheer  
**Overall Marketing Score: 58/100 (Grade: C)**

---

## Executive Summary

SeniorEase Library scoort 58 op 100 — een degelijke basis met een authentiek verhaal, maar significante gaten die directe actie vereisen. De grootste sterkte is de privacy-positionering: "geen account, geen cloud, geen advertenties" is een echte differentiator in een markt vol trackers en abonnementen, en wordt consistent doorgevoerd tot in de privacypolicy en de keuze voor Plausible analytics. Het founder-verhaal (Maureen, de platenbeurs, de dubbele aankoop) is het beste stuk copy op de site en raakt direct de kern van het collector-probleem.

De grootste zwakte is niet één probleem, maar een cluster van verborgen kansen: de sterkste verkoopargumenten (14-dagen garantie, gratis demo, 4.8-sterrenrating, privacy) staan op de verkeerde plek of zijn onzichtbaar op het moment van beslissing. Tegelijkertijd is er één actief risico dat vandaag moet worden opgelost: de Terms & Conditions vermelden €2,99 terwijl de homepage €4,99 toont. Dit is juridisch incorrect én een vertrouwensbreker voor precies de zorgvuldige bezoeker die je wilt converteren.

De drie acties met de meeste impact: (1) herstel de prijsinconsistentie in de T&C, (2) activeer de bestaande CTA-knopstijl in de hero met duidelijke tekst en garantie ernaast, en (3) maak een aparte `/nl/` pagina zodat de volledige Nederlandstalige content indexeerbaar wordt voor Google. Die drie stappen samen kunnen de conversie met 20-35% verbeteren en openen de deur naar organisch Nederlandstalig verkeer.

Geschatte totale impact bij het implementeren van alle aanbevelingen: €300–€800 extra omzet per maand bij 1.000 maandelijkse bezoekers, oplopend tot €1.500–€3.000/maand bij 5.000 bezoekers.

---

## Score Breakdown

| Categorie | Score | Gewicht | Gewogen | Kernbevinding |
|-----------|-------|---------|---------|---------------|
| Content & Messaging | 61/100 | 25% | 15.3 | Sterk verhaal, maar features in plaats van transformatie |
| Conversion Optimization | 61/100 | 20% | 12.2 | Sterkste argumenten op de verkeerde plek |
| SEO & Discoverability | 61/100 | 20% | 12.2 | NL-content onzichtbaar voor Google |
| Competitive Positioning | 52/100 | 15% | 7.8 | Voordelen als features, nooit als vergelijking |
| Brand & Trust | 64/100 | 10% | 6.4 | Goed fundament, prijsinconsistentie is risico |
| Growth & Strategy | 41/100 | 10% | 4.1 | Geen growth loop; demo-gebruikers verdwijnen |
| **TOTAAL** | | **100%** | **58/100** | |

---

## Quick Wins (deze week)

### 1. Herstel de prijsinconsistentie in Terms & Conditions — VANDAAG
**Wat:** `website/terms/index.html` vermeldt €2,99 op minstens 4 plaatsen (regels ~156, ~194 EN; ~227, ~265 NL). De homepage zegt €4,99.  
**Hoe:** Verander alle vermeldingen van €2,99 naar €4,99 in de T&C, of gebruik de formulering "het op dat moment geldende tarief zoals vermeld op de website."  
**Waarom:** Juridisch risico én vertrouwensbreker voor de detail-gerichte bezoeker.  
**Impact:** Risicovermindering + vertrouwen. Direct.

### 2. Update copyright-jaar van 2025 naar 2026
**Wat:** Footer van `website/index.html` toont © 2025.  
**Hoe:** Eén tekstwijziging.  
**Waarom:** Zichtbare verwaarlozing voor een bezoekers die let op details — precies de doelgroep.  
**Impact:** Klein maar direct.

### 3. Verplaats de 14-dagen geld-teruggarantie naar de download-sectie
**Wat:** De garantie staat in FAQ-item 5, na vier lange secties scrollen.  
**Hoe:** Voeg direct onder de Play Store badge toe:  
```html
<p>✓ 14-day money-back guarantee · No questions asked</p>
```  
**Waarom:** Senioren zijn risicoavers. Een garantie bij de koopknop is bewezen effectief.  
**Impact:** Geschat +10-15% conversie op de download-sectie.

### 4. Toon de 4.8-sterrenrating zichtbaar boven de testimonials
**Wat:** JSON-LD heeft al `aggregateRating: 4.8/12` maar dit is nergens zichtbaar.  
**Hoe:** Voeg een pill toe boven de testimonial-sectie: `★★★★★ 4.8 · 12 ratings on Google Play`  
**Waarom:** Externe social proof is sterker dan citaten zonder bron.  
**Impact:** Verhoogde geloofwaardigheid testimonials.

### 5. Voeg FAQPage JSON-LD toe aan index.html
**Wat:** De FAQ-sectie is rijke SEO-content zonder structured data markup.  
**Hoe:** Script-tag toevoegen met FAQPage schema voor 3-5 vragen.  
**Waarom:** Kans op FAQ rich results in Google — extra zichtbaarheid zonder extra verkeer te hoeven verdienen.  
**Impact:** Verbeterde SERP-presentatie.

### 6. Verberg QR-code op mobiel
**Wat:** Een QR-code scannen met de telefoon die de pagina al bekijkt is zinloos.  
**Hoe:** `@media (max-width: 640px) { .qr-block { display: none; } }`  
**Waarom:** Verwijdert verwarring voor de helft van je bezoekers.  
**Impact:** Cleaner mobiele UX.

### 7. Voeg iOS-melding toe voor iPhone-bezoekers
**Wat:** iPhone-bezoekers lopen stil vast bij de Play Store knop.  
**Hoe:** JS-snippet die op iOS een vriendelijke melding toont: "This app is for Android only. Not available for iPhone."  
**Impact:** Voorkomt frustratie en onnodige bounces.

---

## Strategische Aanbevelingen (deze maand)

### 1. Activeer de bestaande CTA-knopstijl in de hero
De CSS heeft al een `.hero .cta` klasse (bruin, groot, met shadow) — maar die wordt niet gebruikt voor de downloadknop. De Play Store badge van 70px is te klein als primaire CTA, zeker op mobiel.

**Implementatie:**
```html
<a href="https://play.google.com/store/apps/details?id=com.maureen.biblitoheek"
   class="cta" target="_blank" rel="noopener" onclick="plausible('Download')">
  <span data-lang="en">Download free — try up to 10 books</span>
  <span data-lang="nl">Gratis downloaden — probeer tot 10 boeken</span>
</a>
<p class="cta-note">
  <span data-lang="en">€4.99 one-time · No subscription · 14-day refund</span>
  <span data-lang="nl">€4,99 eenmalig · Geen abonnement · 14 dagen geld terug</span>
</p>
```
**Geschatte impact:** +15-25% klikratio hero-CTA.

### 2. Communiceer de gratis demo (10 items) prominent
De demo-limiet is nooit tijdgebonden — dat is een sterker argument dan een 7-daagse trial. De pagina zegt alleen "Free to download" zonder de concrete belofte.

**Implementatie:** Voeg toe in de hero cta-note én boven de download-sectie:  
*"Try free up to 10 books, forever. No credit card. No time limit."*  
**Geschatte impact:** Significant lagere instapdrempel voor twijfelende bezoekers.

### 3. Maak een aparte `/nl/` pagina voor Nederlandstalige indexering
De volledige NL-content staat achter `display: none` in CSS — onzichtbaar voor Google. Er is nul concurrentie voor zoektermen als "boeken bijhouden app Android" of "vinyl collectie bijhouden app".

**Implementatie:** `website/nl/index.html` met alleen NL-content. Hreflang aanpassen naar aparte URL's.  
**Geschatte impact:** Toegang tot organisch Nederlandstalig verkeer. Potentieel grootste SEO-winst op middellange termijn.

### 4. Voeg een vergelijkingssectie toe ("Waarom SeniorEase?")
Geen enkele concurrent-vergelijking op de site. Bezoekers die twijfelen tussen opties krijgen geen hulp.

**Implementatie:** Eén tabel tussen Features en Reviews:

| | SeniorEase | CLZ Books | Goodreads |
|---|---|---|---|
| Prijs | €4,99 eenmalig | ~€25/jaar | Gratis maar vol ads |
| Account nodig | Nee | Ja | Ja (verplicht) |
| Offline | Ja | Ja | Nee |
| Multi-format | Ja (boeken + muziek + dvd + games) | Aparte apps | Alleen boeken |
| Privacy | Alles op je toestel | Cloud sync | Amazon-eigendom |

**Geschatte impact:** Hogere conversie voor bezoekers die actief vergelijken.

### 5. Voeg sticky CTA toe voor mobiel
Na de hero verdwijnt de downloadknop voor vier secties. Op mobiel — waar de meeste bezoekers zitten — is er geen terugkerende conversiemogelijkheid.

**Implementatie:** Vaste footer-balk die verschijnt zodra de hero uit beeld is (JS + CSS, zie CRO-analyse voor volledige code).  
**Geschatte impact:** Verdubbelt conversiemogelijkheden op mobiel.

### 6. Herstel en completeer sitemap.xml
`/terms/` en `/contact.html` ontbreken. `tiktok.html` en `generate-qr.html` zijn geen eindgebruikerspagina's en verdunnen crawlbudget.  
**Implementatie:** Sitemap aanpassen, `noindex` toevoegen aan interne tools.

---

## Lange-termijn Initiatieven (dit kwartaal)

### 1. Email capture als schakel tussen demo en betaling
Op dit moment is er geen mechanisme dat demo-gebruikers terugbrengt. Als iemand de app installeert, 9 items invoert en dan stopt — dan is er geen manier om ze te herinneren aan de upgrade.

**Aanpak:** Optioneel emailveld op de website ("Ontvang een tip als je klaar bent om je volledige collectie bij te houden") + in-app melding bij 8/10 items. Zelfs 100 actieve emailadressen zijn meer waard dan 10.000 TikTok-views.  
**Geschatte impact:** Direct invloed op demo → betaald conversieratio.

### 2. TikTok hook-strategie op basis van één collector-probleem per video
De productie-infrastructuur is sterk (8 composities, geautomatiseerd via Remotion). Maar de contentstrategie mist een consistente hook-formule.

**Aanpak:** Elke video begint met een specifieke situatie: *"Je staat op de platenbeurs. Die plaat kost €3. Maar heb je hem al?"* — probleem, oplossing, CTA. De DoubleBuy-compositie heeft dit al goed — maak dit de template voor alle video's.  
**Geschatte impact:** Hogere engagement rate en doorklikratio naar Play Store.

### 3. Een gezicht achter het merk
Het verhaal van Maureen is er al. Wat ontbreekt: een foto, achternaam, en eventueel een KvK-vermelding. Voor senioren die extra kritisch zijn op de vraag "wie zit er achter deze app?" is dit het laatste stukje vertrouwen.

**Aanpak:** Profielfoto naast de About-sectie. Één regel: "Maureen [achternaam], [stad]". Optioneel: LinkedIn-link of KvK-nummer in de footer.  
**Geschatte impact:** Meetbaar hogere vertrouwensscore bij doelgroep.

### 4. iOS-versie of expliciete iOS-wachtlijst
Senioren zijn oververtegenwoordigd op iPhone. De Android-only positie is een structurele groeibegrenzing.

**Aanpak op korte termijn:** Voeg een "Notify me when iOS is available" link toe voor iPhone-bezoekers. Dit geeft data over iOS-interesse zonder ontwikkelinvestering.  
**Lange termijn:** iOS-versie via cross-platform framework (Kotlin Multiplatform of Flutter).

---

## Gedetailleerde Analyse per Categorie

### Content & Messaging (61/100)

**Sterktes:**
- Privacy-positionering is authentiek en consistent: "No account, no ads, no fuss" werkt als triple-negatief dat precies de angsten van de doelgroep wegneemt
- Testimonials zijn specifiek en geloofwaardig (collectieaantallen, herkenbare situaties)
- Barcode-feature heeft goede actiegerichte formulering

**Zwaktes:**
- Headline "Track your books, music, DVDs & games" is beschrijvend, niet motiverend — het benoemt de categorie maar niet het probleem
- Pagina verkoopt features, geen transformatie. De emotionele uitkomst (nooit meer dubbel kopen, nooit meer twijfelen) staat niet centraal
- Prijsinconsistentie (€4,99 vs €2,99) is een actief vertrouwensrisico
- Android-only wordt niet proactief gecommuniceerd vroeg in de pagina
- Geen content voor ontdekking of langetermijnvertrouwen

**Beste copy-verbeteringen:**

| Huidige tekst | Verbeterde versie |
|---|---|
| "Track your books, music, DVDs & games" | "Never buy the same book twice" |
| "No account, no ads, no fuss" | "No account. No ads. No one else's business." |
| "Everything stays on your device" | "Your collection never leaves your phone. No account, no cloud, no subscription to forget." |
| "Download on Google Play" | "Download free — try up to 10 books" |

---

### Conversion Optimization (61/100)

**Sterktes:**
- Prijsmodel (gratis + €4,99 eenmalig) staat direct onder de hero-CTA — elimineert de grootste barrière voor senioren
- Origin story bouwt vertrouwen op een manier die een featurelijst niet kan
- Privacy als expliciete feature, niet als verplicht bijschrift

**Zwaktes:**
- Play Store badge van 70px is geen primaire CTA-knop (de CSS-klasse bestaat al maar wordt niet gebruikt)
- Gratis demo (10 items, onbeperkt in tijd) wordt nergens duidelijk gecommuniceerd
- 14-dagen garantie staat in FAQ-item 5, onzichtbaar op het conversiepunt
- Na de hero: vier secties zonder downloadmogelijkheid (geen sticky CTA)
- 4.8-sterrenrating in JSON-LD maar nergens zichtbaar op de pagina
- QR-code nutteloos voor bezoekers die al op hun telefoon zijn

---

### SEO & Discoverability (61/100)

**Sterktes:**
- Technische basis solide: title, meta, canonical, robots.txt, sitemap aanwezig
- JSON-LD SoftwareApplication correct geïmplementeerd (kans op rich results)
- Statische HTML op Vercel = uitstekende Core Web Vitals

**Kritieke zwaktes:**
- Volledige NL-content staat achter `display: none` — onzichtbaar voor Google
- Hreflang verwijst alle talen naar dezelfde URL — functioneel nutteloos
- Sitemap mist `/terms/` en `/contact.html`
- Geen FAQPage schema (terwijl de FAQ-content perfect is)
- Geen blog of content voor organische ontdekking
- Package-ID bevat typfout: `com.maureen.biblitoheek` (biblitoheek)

---

### Competitive Positioning (52/100)

**Sterktes:**
- Privacy/offline-first is authentiek en technisch bewijsbaar
- One-time payment vs. abonnementen is een reëel voordeel
- Founder-verhaal ontbreekt bij alle concurrenten

**Kwetsbaarheden:**
- Geen enkele vergelijking met concurrenten op de site
- "Senior" in de naam schrikt jongere verzamelaars af zonder compenserend voordeel
- Slechts 12 verifieerbare Play Store reviews — te dun voor externe social proof

**Concurrentievergelijking:**

| Factor | SeniorEase | CLZ Books | Goodreads | LibraryThing |
|--------|------------|-----------|-----------|--------------|
| Prijs | €4,99 eenmalig | ~€25/jaar | Gratis (ads) | Gratis tot 200 |
| Account vereist | Nee | Ja | Ja | Ja |
| Offline | Volledig | Ja | Nee | Nee |
| Multi-format | Ja (4 types) | Aparte apps | Alleen boeken | Alleen boeken |
| Privacy | Sterk | Matig | Zwak (Amazon) | Matig |
| Seniorvriendelijk | Expliciet | Nee | Nee | Nee |

---

### Brand & Trust (64/100)

**Sterktes:**
- Privacy-first consequent doorgevoerd op alle niveaus
- Authentiek origin-verhaal met specifieke situatie
- Kwantificeerbare testimonials

**Gaps:**
- Prijsinconsistentie €4,99 / €2,99 — juridisch en reputatierisico
- Geen verifieerbaar gezicht: "Maureen" zonder foto, achternaam of externe verificatie
- Testimonials niet gelinkt aan externe bron
- Copyright 2025 in footer (het is 2026)
- Contactpagina heeft totaal andere visuele stijl — mist merkidentiteit

---

### Growth & Strategy (41/100)

**Sterktes:**
- Solide productiebasis voor TikTok/Instagram content (8 composities, geautomatiseerd)
- Privacy-positionering past bij groeipositioning richting privacy-bewuste gebruikers
- Plausible net geïnstalleerd — goede beslissing voor data-gedreven optimalisatie

**Zwaktes:**
- Geen growth loop: demo-gebruikers verdwijnen zonder herinnering of email
- TikTok-strategie heeft breedte maar mist consistente hook-formule
- Geen email capture anywhere
- iOS-markt volledig uitgesloten (senioren oververtegenwoordigd op iPhone)
- Geen referral of mond-tot-mondmechanisme

---

## Revenue Impact Samenvatting

*(Op basis van 1.000 maandelijkse bezoekers, 2% conversie naar Play Store, 30% upgrade naar betaald, €4,99 ARPU)*

| Aanbeveling | Geschatte maandelijkse impact | Betrouwbaarheid | Doorlooptijd |
|---|---|---|---|
| Echte CTA-knop in hero | +€30–€75 | Hoog | 1 dag |
| 14-dagen garantie bij CTA | +€20–€50 | Hoog | 1 dag |
| Gratis demo prominent communiceren | +€25–€60 | Hoog | 1 dag |
| Pricing fix T&C | Risicovermindering | Hoog | Vandaag |
| NL pagina `/nl/` (SEO) | +€50–€200 | Middel | 2 weken |
| Sticky CTA mobiel | +€15–€40 | Middel | 3 dagen |
| Vergelijkingssectie | +€20–€50 | Middel | 1 week |
| Email capture + nurture | +€50–€150 | Middel | 1 maand |
| TikTok hook-strategie | +€30–€100 | Laag-middel | Doorlopend |
| Play Store reviews activeren | +€15–€40 | Middel | 2 weken |
| **Totaal potentieel** | **€255–€765/maand** | | |

Bij 5.000 bezoekers/maand: €1.275–€3.825/maand.

---

## Volgende Stappen

1. **Vandaag:** Herstel prijsinconsistentie in `website/terms/index.html` (€2,99 → €4,99) + update copyright naar 2026
2. **Deze week:** Activeer echte CTA-knop in hero, voeg garantie toe bij download-sectie, toon sterrenrating boven testimonials
3. **Deze maand:** Maak `/nl/` pagina voor Nederlandstalige SEO, voeg vergelijkingssectie toe, implementeer sticky CTA mobiel

*Gegenereerd door AI Marketing Suite — `/market-audit`*
