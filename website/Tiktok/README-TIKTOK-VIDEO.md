# TikTok-video van 4 screenshots

## Optie 1: Script met ffmpeg (automatisch)

1. **Installeer ffmpeg** (eenmalig):
   - Windows: `winget install ffmpeg`
   - Of: https://ffmpeg.org/download.html

2. **Run het script** (in PowerShell, vanuit deze map):
   ```powershell
   cd website\Tiktok
   .\MAKE-TIKTOK-VAN-SCREENSHOTS.ps1
   ```

3. **Output:** `SeniorEase-Library-4screenshots.mp4` (9:16, 16 seconden, 4 screenshots × 4 sec).

4. Upload dit bestand in de TikTok-app; je kunt er nog muziek of tekst aan toevoegen.

---

## Optie 2: Zonder ffmpeg (handmatig)

- **Canva:** Nieuw project → Video 9:16 → upload de 4 plaatjes uit `website/assets/` (app-library.png, app-scan.png, app-filter.png, app-menu.png) → elk 3–4 seconden tonen → exporteer als video.
- **CapCut (gratis):** Zelfde idee: 9:16 project, 4 afbeeldingen toevoegen, duur instellen, export.
- **TikTok zelf:** Bij “Create” kun je meerdere foto’s kiezen en omzetten naar een slideshow-video.

Screenshots staan in: `website/assets/app-library.png`, `app-scan.png`, `app-filter.png`, `app-menu.png`.
