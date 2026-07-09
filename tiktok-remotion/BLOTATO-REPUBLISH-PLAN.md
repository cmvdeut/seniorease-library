# Herplanning TikTok + Instagram met ondertitels (Blotato)

**Doel:** bestaande SeniorEase-video’s opnieuw inplannen voor **TikTok (EN)** en **Instagram Reels**, met **zichtbare ondertitels in de video**, publicatie via **Blotato**.

**Bronnen in deze repo:**
- Scripts + captions: `content/seniorease-tiktok-scripts-en.csv` (20 video’s)
- Kalender: `WEEKPLANNER-MAY-2026.md`, `content/social-posting-calendar-may-2026.md`
- Captions: `content/captions-tiktok-en.md`, `content/tiktok-en-upload-playbook.md`
- Renders: `output/` (Remotion) en eventueel oudere bestanden in `Tiktok/`
- Oude API-post: `scripts/post-tiktok.ts`, `scripts/post-instagram.ts` → **vervangen door Blotato** (UI of API)

---

## Belangrijk: wat “subtitle tracks” hier betekent

| Type | Wat het is | Blotato / platform |
|------|------------|-------------------|
| **On-video ondertitels** | Tekst in het beeld (hook/body/CTA) | Moet **in de MP4 zitten** vóór upload. Blotato publiceert alleen `mediaUrls`; geen aparte SRT-track bij publish. |
| **Post-caption** | Tekst onder de video op TikTok/IG | Blotato-veld `content.text` (+ hashtags). |
| **TikTok “auto captions”** | Door TikTok gegenereerd na upload | Optioneel in de app; **niet** via jullie huidige Remotion-export. |

**Aanbeveling:** ondertitels **in Remotion branden** (uit CSV: `hook_text`, `body_text`, `cta_text`), daarna **één MP4 per post** naar Blotato.

Blotato’s eigen templates (`/v2/videos/from-templates`) zijn handig voor **nieuwe** AI-video’s met voice + captions — niet nodig als je bestaande Remotion-beelden wilt hergebruiken.

---

## Workflow in 4 fasen

### Fase A — Blotato klaarzetten (eenmalig)

1. Account op [my.blotato.com](https://my.blotato.com) — plan met TikTok + Instagram.
2. **TikTok** en **Instagram** koppelen (Professional/Creator).
3. **API key** (als je later wilt automatiseren): `BLOTATO_API_KEY` in `tiktok-remotion/.env` (niet committen).
4. Optioneel: **kalenderslots** in Blotato (`useNextFreeSlot`) — bijv. TikTok 19:30, Instagram 12:00 of 20:00.
5. Documentatie: [Publish Post](https://help.blotato.com/api/publish-post), [API voor LLM’s](https://help.blotato.com/api/llm.md).

### Fase B — Video’s met ondertitels (Remotion)

1. **Inventaris** — welke MP4’s blijven geldig (zelfde boodschap als CSV ID 1–20)?
2. **Per script-ID** tekst uit CSV → tijdlijn:
   - 0–3s: hook  
   - 3–10s: body  
   - laatste 2–3s: CTA  
3. **Her-render** (na implementatie subtitle-laag in `FeatureDemo` / script-compositie):
   ```bash
   cd tiktok-remotion
   npm run render:scripts:en
   ```
   Output: `output/seniorease-script-en-{id}-...mp4`
4. **Naamconventie** (voor planning):
   `subs-en-{id}-seniorease-script-en-{id}-YYYY-MM-DD.mp4`

*Technisch: `SubtitleOverlay` in `FeatureDemo` + `showSubtitles: true` bij script-renders (`render:scripts:en`).*

### Fase C — Video’s publiek bereikbaar maken

Blotato vereist **publieke URL’s** in `mediaUrls` (max ~400 MB–1 GB afhankelijk van plan).

| Optie | Actie |
|-------|--------|
| **A — Website** | Upload MP4 naar `website/public/videos/` (of CDN), deploy Vercel → `https://www.seniorease.eu/videos/...mp4` |
| **B — Blotato upload** | `POST /v2/media/uploads` → presigned URL → `publicUrl` in `mediaUrls` |

### Fase D — Inplannen in Blotato

**Handmatig (start):** Blotato-dashboard → Content calendar → video + caption + TikTok/IG.

**API (later):** per post één `POST https://backend.blotato.com/v2/posts` met:
- `scheduledTime` op **root-niveau** (niet in `post` nesten!)
- `content.mediaUrls`: [publieke MP4-URL]
- `content.text`: caption uit CSV + hashtags
- `target` TikTok: alle verplichte velden (`privacyLevel`, `isAiGenerated`, …)
- `target` Instagram: `mediaType: "reel"`, optioneel `shareToFeed: true`

TikTok EN-voorbeeld uit playbook: 19:30; pas datums aan op **jouw** startweek.

---

## Herplanningskalender (voorstel — pas datums aan)

Gebruik **zelfde ID-volgorde** als `WEEKPLANNER-MAY-2026.md` (TikTok EN). Instagram kan **dezelfde video** 1 dag later of zelfde dag ander tijdstip.

| Week | Ma–Vr | TikTok EN (script ID) | Instagram (zelfde ID) | Opmerking |
|------|-------|------------------------|------------------------|-----------|
| 1 | 3 posts | 1, 2, 3 | 1, 2, 3 | Eerst drafts in Blotato controleren |
| 2 | 5 posts | 4–8 | 4–8 | |
| 3 | 5 posts | 9–13 | 9–13 | |
| 4 | 5 posts | 14–18 | 14–18 | |
| 5 | 2 + recap | 19, 20 + beste ID | idem | Beste hook opnieuw |

**Dagelijkse checklist (Blotato):**
- [ ] MP4 = versie **met** ondertitels  
- [ ] Caption = `captions-tiktok-en.md` / CSV-kolom `caption` + `hashtags`  
- [ ] Link in bio / caption: Google Play  
- [ ] TikTok: Commercial Music Library indien muziek in app  
- [ ] Instagram: reel, cover indien nodig  

---

## Wat je **niet** meer hoeft (tenzij backup)

- Docker `post-tiktok` / `post-instagram` voor deze ronde  
- TikTok OAuth-stress (`TIKTOK-SETUP.md`) — Blotato regelt koppeling in hun UI  
- `publish-scheduled.ts` + Google Sheet — vervang door Blotato calendar of eigen CSV → API-script (later)

**Behouden:** Remotion render, CSV’s, playbooks, `output/` als bron.

---

## Volgende technische stappen in de repo (wanneer je wilt)

1. **SubtitleOverlay** in Remotion + koppeling aan `featurePropsFromRow`  
2. **Map** `content/blotato-schedule.csv`: `id, brand, date, time_utc, platform, video_filename, caption, status`  
   - `brand` = `seniorease` (27720 / 50066) of `shelfieease` (44442 / 50003) — nooit mixen  
   - ShelfieEase template: `content/blotato-schedule-shelfieease-template.csv`
3. Optioneel: `scripts/post-blotato.ts` (schedule vanuit CSV + `.env`)  
4. **Map** `website/public/videos/` + vermelding in deploy-checklist  

---

## Blotato vs huidige setup

| | Oud | Nieuw (Blotato) |
|---|-----|-----------------|
| Posten | Eigen TikTok/Meta API scripts | Blotato dashboard of API |
| Planning | Sheet + `publish-scheduled.ts` | Blotato calendar / `scheduledTime` |
| Ondertitels | Alleen tekst op slides | **Gebrand in MP4** (Remotion) |
| Hosting | Instagram: URL verplicht; TikTok: lokaal bestand | Beide: **publieke URL** (of Blotato upload) |

---

*Laatste update: mei 2026 — bij start herplanning datums in WEEKPLANNER en dit bestand gelijk trekken.*
