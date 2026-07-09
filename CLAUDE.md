# SeniorEase Library - Claude instructies

## Taal
- Communiceer altijd in het **Nederlands**
- Code en bestandsnamen blijven in het **Engels**
- Video-inhoud (titels, teksten) is in het **Engels** (doelgroep internationaal)

## Project structuur

| Map | Wat |
|-----|-----|
| `app/` | Android app (Kotlin) |
| `website/` | Statische website, gedeployed op Vercel |
| `api/` | Node.js Express API (Stripe betaalverificatie) |
| `nextjs-api/` | Next.js API (Stripe betaalverificatie) |
| `tiktok-remotion/` | Remotion video rendering + TikTok/Instagram posting |

## Docker (tiktok-remotion)

De sociale media automatisering draait via Docker:

```bash
cd tiktok-remotion
docker compose build        # eenmalig of na code wijzigingen
docker compose run render   # video's renderen naar output/
docker compose run post-tiktok    # posten naar TikTok
docker compose run post-instagram # posten naar Instagram
```

- Rendered video's komen in `tiktok-remotion/output/`
- Afbeeldingen voor video's staan in `tiktok-remotion/public/`
- API keys staan in `tiktok-remotion/.env` (nooit committen)
- Na elke code wijziging: eerst `docker compose build` dan pas `docker compose run`

## Remotion video's

| Compositie | Bestand | Inhoud |
|---|---|---|
| ScreenshotSlideshow | seniorease-slideshow-DATUM.mp4 | 5 app-screenshots met tekst |
| HeartTransformation | seniorease-heart-animatie-DATUM.mp4 | Emotioneel verhaal + CTA |
| DoubleBuyVideo | seniorease-doublebuy-DATUM.mp4 | "Stop buying the same book twice" |

- Afbeeldingspaden in `Root.tsx` zijn **relatief aan `public/`** (dus `"start.jpeg"`, niet `"../Screenshots/start.jpeg"`)
- `durationInFrames` in `Root.tsx` en het aantal frames in de compositie moeten overeenkomen

## Website
- Gedeployed op Vercel
- Domein: seniorease.eu
- Statische HTML, geen framework

## Conventies
- Geen automatische commits of pushes zonder bevestiging
- Geen bestanden aanmaken tenzij noodzakelijk
- Bij Docker wijzigingen: altijd vermelden dat `docker compose build` nodig is
