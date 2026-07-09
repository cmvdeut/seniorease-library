# Brevo welkomstmail — SeniorEase Library

Eerste welkomstmail voor nieuwsbrief-inschrijvers via het formulier op seniorease.eu.

## Bestanden

| Bestand | Taal | Gebruik |
|---------|------|---------|
| `brevo-welcome-email-en.html` | Engels | Standaard (website is EN-first) |
| `brevo-welcome-email-nl.html` | Nederlands | Optioneel voor NL-lijst of later segmentatie |

## Onderwerpregels (A/B-test later)

**EN (aanbevolen)**
- Subject: `Welcome — your collection tips start here`
- Preheader: `Scan, track, never buy the same book twice.`

**NL**
- Subject: `Welkom — tips voor je collectie`
- Preheader: `Scannen, bijhouden, nooit hetzelfde boek dubbel kopen.`

## Template in Brevo (live)

| Veld | Waarde |
|------|--------|
| **Template ID** | `10` (EN) · `11` (NL) |
| **Naam EN** | Welcome — SeniorEase EN |
| **Naam NL** | Welcome — SeniorEase NL |
| **Subject EN** | Welcome — your collection tips start here |
| **Subject NL** | Welkom — tips voor je collectie |
| **Afzender** | Maureen \<support@seniorease.eu\> |
| **Status** | Actief |

Lokaal HTML-backup: `brevo-welcome-email-en.html`

**Bestaande automation-template (id 9):** Brevo-automation gebruikt soms een apart template. Koppel in **Automations** template **#10** als welkomstmail, of vervang template 9 in de editor.

## Lijsten in Brevo

| ID | Naam | Subscribers |
|----|------|-------------|
| 2 | Your first list | 2 (website-inschrijvingen) |
| 4 | Contacts involved in conversations | 1 |
| 5 | identified_contacts | 0 |

Website-formulier schrijft naar lijst **#2** (`Your first list`).


## Stap 2 — Automation (welkomstmail automatisch)

1. **Automations** → **Create an automation**
2. Trigger: **Contact added to a list**
3. Kies de lijst waar je website-formulier naartoe schrijft (controleer onder **Contacts → Lists** welke lijst groeit na een test-inschrijving)
4. Actie: **Send an email** → kies template `Welcome — SeniorEase EN`
5. Vertraging: **Immediately** (of 5 minuten als je wilt filteren op typos)
6. **Activate**

### Test

1. Schrijf je in via https://seniorease.eu (newsletter-sectie onderaan)
2. Controleer of contact in Brevo staat
3. Controleer of welkomstmail binnen 1–2 minuten aankomt (check spam)

## Stap 3 — Afzender

Gebruik een geverifieerd adres, bijv.:
- `Maureen <support@seniorease.eu>` (als domein geverifieerd)
- of je Brevo-default sender

Reply-to: `support@seniorease.eu`

## Website-koppeling

Het formulier op de site post al naar Brevo (`sibforms.com`). Geen codewijziging nodig voor de welkomstmail zelf.

Optioneel later: `locale`-veld in Brevo gebruiken om EN vs NL automation te splitsen.

## E-mail 2 — Conversie (dag 12)

| Veld | Waarde |
|------|--------|
| **Template ID** | `12` |
| **Naam** | Conversion — Day 12 — SeniorEase EN/NL |
| **Subject** | Your collection deserves better than a notebook / Je collectie verdient meer dan een notitieboekje |
| **Formaat** | Bilingual — EN boven, NL onder |
| **Backup HTML** | `brevo-conversion-day12-bilingual.html` |

### Automation (dag 12)

1. **Automations** → voeg stap toe na welkomstmail
2. **Wait** → **12 days** after contact added to list
3. **Send email** → template **#12**
4. Lijst: **Your first list** (id 2)

## Volgende mails (ideeën)

| # | Wanneer | Onderwerp |
|---|---------|-----------|
| 3 | +3 dagen na mail 2 | Tip-mail (kringloop / dubbel kopen) |
| 4 | +7 dagen | App-update highlight |

---

*Gemaakt: 2026-03-27. Stem af op brand brief: warm, no-nonsense, geen tech-jargon.*
