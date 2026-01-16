# APK upload stappen (live)

Gebruik dit bestand om de nieuwe APK te uploaden naar de website.

## 1) APK hernoemen

De APK is nu:

```
app\build\outputs\apk\demo\release\SeniorEase-Library-1.0.6.apk
```

## 2) Kopieer naar website downloads

Kopieer de APK naar:

```
website\downloads\SeniorEase-Library-1.0.6.apk
```

## 3) Update download link in `index.html`

Zoek in `website/index.html` naar de download link en zet die op:

```
/downloads/SeniorEase-Library-1.0.6.apk
```

## 4) Deploy

Push naar GitHub zodat Vercel opnieuw deployt, of redeploy in Vercel.

## 5) Check

- Open https://www.seniorease.eu/
- Download knop moet de nieuwe APK downloaden
- QR code moet hetzelfde blijven (linkt naar de homepage)
