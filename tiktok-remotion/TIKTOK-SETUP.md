# TikTok API Setup voor @seniorease

## Stap 1: TikTok Developer account aanmaken

1. Ga naar https://developers.tiktok.com
2. Log in met je TikTok account (@seniorease)
3. Klik op **"Manage apps"** → **"Create app"**
4. Vul in:
   - App name: `SeniorEase Auto Post`
   - Category: `Entertainment` of `Lifestyle`
   - Description: `Automatisch TikTok video's posten voor SeniorEase Library`

---

## Stap 2: Content Posting API toevoegen

1. In je app, ga naar **"Add products"**
2. Voeg toe: **Content Posting API**
3. Klik op **"Apply for access"** (dit duurt 1-5 werkdagen)
4. Vul het formulier in:
   - Use case: beschrijf dat je automatisch promotievideo's post voor je eigen app
   - Platform: TikTok
   - Scopes die je nodig hebt: `video.publish`, `video.upload`

---

## Stap 3: App credentials ophalen

Na goedkeuring:
1. Ga naar je app → **"Manage"** → **"App info"**
2. Kopieer `Client key` en `Client secret`
3. Zet ze in `.env`:
   ```
   TIKTOK_CLIENT_KEY=your_client_key
   TIKTOK_CLIENT_SECRET=your_client_secret
   ```

---

## Stap 4: Access token ophalen (OAuth)

De access token is gekoppeld aan het @seniorease account en geeft toestemming om te posten.

**Belangrijk:** OAuth werkt alleen als je app **Live** is (goedgekeurd na review). In Sandbox is de Content Posting API niet beschikbaar voor echte video’s.

### Optie A: Handmatig via browser (eenmalig)

1. Stel **één** redirect URI in op je app dashboard. TikTok accepteert **geen** `localhost`; gebruik een publieke HTTPS-URL, bijvoorbeeld:
   - **`https://www.seniorease.eu/tiktok-callback`** — deze pagina staat live en toont de autorisatiecode om te kopiëren, of
   - Een ngrok-URL voor lokaal testen: `https://xxxx.ngrok.io/callback`

2. Ga in de browser naar (vervang CLIENT_KEY en REDIRECT_URI exact zoals in het dashboard):
   ```
   https://www.tiktok.com/v2/auth/authorize/?client_key=CLIENT_KEY&scope=video.publish,video.upload&response_type=code&redirect_uri=REDIRECT_URI&state=seniorease
   ```
   Voorbeeld met seniorease.eu: `redirect_uri=https%3A%2F%2Fwww.seniorease.eu%2Ftiktok-callback` (URL-gecodeerd).

3. Log in als @seniorease en geef toestemming

4. Je wordt doorgestuurd naar je redirect URL met `?code=...&state=seniorease`. Kopieer de `code`.

5. Wissel de code in voor tokens (redirect_uri moet exact hetzelfde zijn als in stap 1):
   ```bash
   curl -X POST "https://open.tiktokapis.com/v2/oauth/token/" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "client_key=CLIENT_KEY&client_secret=CLIENT_SECRET&code=AUTHORIZATION_CODE&grant_type=authorization_code&redirect_uri=REDIRECT_URI"
   ```

7. Kopieer `access_token` en `refresh_token` naar `.env`

### Access token vernieuwen (elke 24 uur)

```bash
curl -X POST "https://open.tiktokapis.com/v2/oauth/token/" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_key=CLIENT_KEY&client_secret=CLIENT_SECRET&grant_type=refresh_token&refresh_token=REFRESH_TOKEN"
```

---

## Stap 5: Postlimieten

- **Max 2 posts per dag** per account via de API
- Video max **500 MB**, max **60 minuten**
- Onze videos zijn ~15-60 seconden → geen probleem

---

## Workflow (na setup)

```bash
# 1. Render nieuwe video's
npm run render:all

# 2. Post naar TikTok
npm run post:tiktok -- --file output/seniorease-slideshow-2026-02-27.mp4 --title "Nooit meer een boek dubbel kopen 📚 #boeken #lezen"

# Of automatisch (pakt meest recente video)
npm run post:tiktok
```

---

## Hashtag suggesties voor SeniorEase

```
#boeken #lezen #bibliotheek #leesplezier #senioren #boekenliefhebber #leestips #boekenapp #reading #books
```

---

## Waarom lukt TikTok OAuth niet?

### 1. App is nog niet **Live**
Je krijgt pas API-toegang (en dus OAuth) als je app door TikTok is **goedgekeurd** en de status **Live** heeft. Tot die tijd:
- **Draft** → nog niet ingediend; indienen voor review.
- **In review** → wachten (enkele dagen tot twee weken).
- **Not approved** → in het dashboard bij "Review comments" kijken en aanpassingen doen, daarna opnieuw indienen.

### 2. Site verification niet afgerond
Voor de **Content Posting API** moet je **alle gevraagde URL’s verifiëren** (o.a. website, privacy, terms). Zolang er "Unverified" staat bij een URL, kun je de app niet (volledig) indienen of wordt OAuth beperkt. Zorg dat de meta-tags op de juiste pagina’s staan en dat de live site (bijv. seniorease.eu) die tags toont.

### 3. Redirect URI: localhost mag niet
TikTok accepteert **geen** `http://localhost:...` of `https://localhost:...` als redirect URI. Gebruik een publieke **HTTPS**-URL, bijvoorbeeld:
- `https://www.seniorease.eu/tiktok-callback` (pagina die de `code` uit de URL toont of doorgeeft), of
- Een tunnel zoals **ngrok** (`ngrok http 3000`) en die HTTPS-URL in het TikTok-dashboard zetten.

### 4. Sandbox en Content Posting
In **Sandbox** kun je Login Kit e.d. testen, maar de **Content Posting API** (video’s posten) is daar **niet** beschikbaar. Om echt te posten moet je app in **Production** staan en **Live** zijn.

### 5. Exacte redirect_uri
De `redirect_uri` in de authorize-URL en in de token-request moet **exact** overeenkomen met wat in het TikTok-dashboard staat (inclusief trailing slash of niet, geen extra query parameters).
