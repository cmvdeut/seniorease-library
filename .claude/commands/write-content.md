# Write Content — SeniorEase Wekelijkse Sociale Media Batch

Genereer een volledige week-batch sociale media content voor SeniorEase Library.

**Weekdatum / focus:** $ARGUMENTS  
_(Als leeg: vraag welke week dit is, bijv. "week van 9 juni 2026")_

---

## Stap 1 — Lees de context

Lees deze bestanden voordat je ook maar één woord schrijft:

1. **`brand-profile.json`** — brand voice, doelgroep, kleuren, verboden clichés
2. **`tiktok-remotion/content/seniorease-tiktok-scripts-en.csv`** — al gebruikte EN hooks (vermijd exacte herhalingen én vergelijkbare pijnpunten)
3. **`tiktok-remotion/content/seniorease-facebook-scripts-nl.csv`** — al gebruikte NL hooks (zelfde anti-herhalingsregel)
4. **`tiktok-remotion/content/blotato-schedule.csv`** — Blotato CSV-formaat referentie

Stel na het lezen vast: welke pijnpunten zijn al uitgeput? Welke hoeken zijn nog ongebruikt?

---

## Stap 2 — Genereer 8 posts

Maak precies **8 posts** met deze vaste verdeling:

| Post # | Categorie | Stijl |
|--------|-----------|-------|
| 1 | LP / vinyl | TikTok: prikkelende hook, snel tempo |
| 2 | LP / vinyl | Instagram: visueel, sfeervoller |
| 3 | Boeken | TikTok: prikkelende hook |
| 4 | Boeken | Instagram: visueel |
| 5 | Consolegames | TikTok: prikkelende hook |
| 6 | Consolegames | Instagram: visueel |
| 7 | Cd's | TikTok: prikkelende hook |
| 8 | Cd's | Instagram: visueel |

### Toon & stijl (uit brand-profile.json)
- Warm, direct, geen tech-jargon
- Doelgroep: verzamelaars 45–75 jaar, houden van fysieke media
- Geen corporate stock-foto-taal, geen startup-energie
- Engels: conversationeel en to-the-point
- Nederlands: Vlaams-vriendelijk, niet formeel

### CTA-template (altijd deze richting)
- EN: "Download SeniorEase free on Google Play — no account needed."
- NL: "Gratis downloaden in de Play Store — geen account nodig."

### Remotion compositie-advies
Kies per post de meest passende compositie:

| Situatie | Compositie |
|----------|------------|
| Dubbel kopen als pijnpunt | `DoubleBuyVideo` |
| Stap-voor-stap feature tonen | `FeatureDemo` |
| Emotioneel verhaal (frustratie → oplossing) | `HeartTransformation` |
| Collectie-showcase / app-screenshots | `ScreenshotSlideshow` |
| Promo / nieuw in Play Store | `NowOnPlayStore` |

---

## Stap 3 — Output schrijven

### 3a. EN Remotion CSV
Schrijf naar: `tiktok-remotion/content/week-{DATUM}-en.csv`

Formaat (exact deze kolommen, komma-gescheiden, velden met komma's tussen dubbele quotes):
```
id,hook_text,body_text,cta_text,duration_sec,visual_suggestion,caption,hashtags
```

- `id`: 1 t/m 8
- `hook_text`: maximaal 8 woorden, eindigt NIET met punt, pakt aandacht in eerste 1 seconde
- `body_text`: 1–2 zinnen, concreet voordeel, past op scherm
- `cta_text`: kort, directief, verwijst naar Play Store
- `duration_sec`: 10–15 seconden (gebruik 10 voor snelle TikTok-stijl, 15 voor Instagram/feature)
- `visual_suggestion`: korte beschrijving voor video-editor welke beelden te gebruiken
- `caption`: volledige caption voor Blotato (1–3 zinnen)
- `hashtags`: 5–8 relevante hashtags als één string, spatie-gescheiden

### 3b. NL Remotion CSV
Schrijf naar: `tiktok-remotion/content/week-{DATUM}-nl.csv`

Zelfde kolommen. Vertaal/herschrijf — niet letterlijk vertalen maar cultureel aanpassen voor Nederlandse/Vlaamse verzamelaars.

### 3c. Blotato planning CSV
Schrijf naar: `tiktok-remotion/content/week-{DATUM}-blotato.csv`

Formaat:
```
id,date,time_utc,platform,video_filename,caption,status
```

- Elke post krijgt 2 rijen: één voor TikTok (19:30 UTC), één voor Instagram (20:00 UTC)
- `date`: vul de weekdagen in (maandag t/m vrijdag + zaterdag), verspreid 8 posts over 5–6 dagen
- `platform`: `tiktok` of `instagram`
- `video_filename`: laat leeg (wordt later ingevuld na renderen)
- `caption`: gebruik de EN caption voor TikTok, NL caption voor Instagram
- `status`: `pending`
- Totaal: 16 rijen (8 posts × 2 platforms)

---

## Stap 4 — Chat-overzicht

Toon na het schrijven van de bestanden een leesbaar overzicht in de chat:

```
## Week {DATUM} — Content Batch

### Post 1 — LP/vinyl (TikTok)
**Compositie:** DoubleBuyVideo
**EN hook:** "..."
**NL hook:** "..."
**Caption EN:** ...
**Caption NL:** ...
**Hashtags:** ...

[herhaal voor alle 8 posts]

---
Bestanden aangemaakt:
- tiktok-remotion/content/week-{DATUM}-en.csv ✓
- tiktok-remotion/content/week-{DATUM}-nl.csv ✓
- tiktok-remotion/content/week-{DATUM}-blotato.csv ✓

Volgende stap: docker compose run render scripts-en (met --csv week-{DATUM}-en.csv)
```

---

## Bewezen hook-formules

Gebruik deze goedgekeurde hooks als inspiratie of pas ze aan per categorie. Gebruik nooit dezelfde hook twee keer.

| Hook | Beste categorie |
|------|----------------|
| Ownership is the new flex | vinyl, games |
| Subscription fatigue is real | alle categorieën |
| $75 a month vs. €5 once | alle categorieën |
| Your favorite [album/book] might disappear tomorrow | vinyl, boeken |
| Less scrolling. More collecting. | alle categorieën |
| One shelf beats five subscriptions | alle categorieën |
| From autoplay to ownership | vinyl, cd's |
| Vinyl sales just hit a 40-year high | vinyl |
| Five apps. Nothing you actually own | alle categorieën |
| Physical media never left. You just forgot to organize it | alle categorieën |
| The collection that never needed a comeback | vinyl, boeken, games |

---

## Regels

- **Geen enkele hook mag al bestaan** in de gelezen CSV-bestanden (controleer expliciet)
- **Geen abonnements-taal** (de app kost €4,99 eenmalig — geen maandelijkse kosten)
- **Geen account vereist** is een kernboodschap — benoem dit regelmatig
- **Geen jongeren-energie** — verzamelaars zijn volwassen, gepassioneerd, rustig
- **Offline werking** is een sterk verkoopargument — gebruik het minstens 1x per batch
- **Privacy** (geen account, geen tracking) — gebruik het minstens 1x per batch
- Video-scripts zijn in het **Engels** (internationale TikTok/Instagram doelgroep), captions voor Instagram mogen ook NL zijn
