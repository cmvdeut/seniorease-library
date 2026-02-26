# CLAUDE.md — SeniorEase Library

This file provides context for AI assistants (Claude and others) working on this repository.

---

## Project Overview

**SeniorEase Library** (package `com.seniorease.library`) is an Android app that lets senior users manage a personal collection of books, music, DVDs, and games. The app has a freemium model: a **demo** flavour allows up to 10 items; the **full** version (unlocked after Stripe payment) is unlimited.

The repository is a monorepo containing four distinct sub-projects:

| Sub-project | Path | Tech |
|---|---|---|
| Android app | `app/` | Kotlin, Jetpack Compose, Room |
| Download website | `website/` | Static HTML + Vercel serverless JS |
| Next.js purchase-verification API | `nextjs-api/` | Next.js 14, TypeScript, Stripe |
| Standalone Express API (legacy) | `api/` | Node.js, Express, Stripe |

---

## Repository Layout

```
seniorease-library/
├── app/                         # Android application module
│   ├── build.gradle.kts         # App-level Gradle config (flavors, signing, deps)
│   ├── proguard-rules.pro
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/seniorease/library/
│   │   │   ├── MainActivity.kt          # Entry point + ViewModel
│   │   │   ├── data/
│   │   │   │   ├── AppDatabase.kt       # Room database (version 4, 3 migrations)
│   │   │   │   ├── Item.kt              # Room entity
│   │   │   │   └── ItemDao.kt           # DAO: CRUD + sort/filter queries
│   │   │   ├── ui/
│   │   │   │   ├── ItemListScreen.kt    # Main library screen (Compose)
│   │   │   │   ├── AddItemDialog.kt     # Add/edit item dialog
│   │   │   │   ├── BarcodeScannerScreen.kt
│   │   │   │   ├── CoverFetchDialog.kt
│   │   │   │   ├── CoverPreviewModal.kt
│   │   │   │   ├── SettingsScreen.kt
│   │   │   │   └── theme/
│   │   │   │       ├── Color.kt         # Brand colour palette
│   │   │   │       ├── Theme.kt         # BiblitoheekTheme + high-contrast schemes
│   │   │   │       ├── Type.kt
│   │   │   │       └── AccessibleTypography.kt
│   │   │   └── utils/
│   │   │       ├── LanguageHelper.kt    # Language preference (NL/EN/system)
│   │   │       ├── SettingsHelper.kt    # Large text + high contrast prefs
│   │   │       └── UnlockHelper.kt      # Demo/full unlock state (SharedPreferences)
│   │   └── res/
│   │       ├── values/strings.xml       # Default (Dutch) strings
│   │       └── values-en/strings.xml    # English strings
│   └── demo/release/                    # Pre-built demo APK (for distribution)
├── website/                     # Vercel-hosted download page
│   ├── index.html               # Landing/download page
│   ├── vercel.json              # APK Content-Type header rule
│   ├── package.json             # build script + Stripe dependency
│   ├── build.js                 # Build script for Vercel
│   ├── api/
│   │   ├── verify-purchase.js   # Vercel serverless: purchase verification (ACTIVE)
│   │   └── reset-download.js    # Vercel serverless: support reset endpoint
│   └── generate-qr-node.js
├── nextjs-api/                  # Alternative Next.js API (TypeScript)
│   └── pages/api/verify-purchase.ts
├── api/                         # Legacy standalone Express server
│   └── server.js
├── build.gradle.kts             # Root Gradle (plugin declarations only)
├── settings.gradle.kts          # Gradle: includes :app, defines repositories
├── gradle/libs.versions.toml    # Version catalog
├── gradle.properties            # JVM args, AndroidX, Kotlin code style
├── upload-keystore.jks          # Release signing keystore
├── bieb-keystore.jks            # Alternative keystore
└── Screenshots/                 # App screenshots (marketing)
```

---

## Android App

### Build System

- **AGP**: 8.13.2 | **Kotlin**: 2.0.21 | **compileSdk/targetSdk**: 36 | **minSdk**: 35
- Uses Kotlin Version Catalog (`gradle/libs.versions.toml`).
- `kotlin-kapt` is used for Room annotation processing (alongside the Compose plugin).
- Build is configured in `app/build.gradle.kts`.

### Product Flavors

```
flavorDimensions += "version"
  full  → applicationId: com.seniorease.library        IS_DEMO=false  MAX_ITEMS=-1
  demo  → applicationId: com.seniorease.library.demo   IS_DEMO=true   MAX_ITEMS=10
```

Access at runtime via `BuildConfig.IS_DEMO` and `BuildConfig.MAX_ITEMS`.

### Build Types

- **release**: minification + resource shrinking enabled; uses `upload-keystore.jks`.
- **debug**: standard debug (no explicit config).

### Building

```bash
# Full release AAB (for Play Store)
./gradlew :app:bundleFullRelease

# Demo release APK (for direct distribution)
./gradlew :app:assembleDemoRelease

# Debug APK
./gradlew :app:assembleDebug

# Run unit tests
./gradlew :app:test

# Run instrumented tests (requires connected device/emulator)
./gradlew :app:connectedAndroidTest
```

Output locations:
- AABs → `app/release/`
- Demo APK → `app/demo/release/Seniorease-Bibliotheek-Demo.apk`

### Data Layer

**Room database** (`AppDatabase`, version 4):

| Migration | Change |
|---|---|
| 1→2 | Added `language TEXT` column |
| 2→3 | Added `coverUrl TEXT` and `googleSearchUrl TEXT` columns |
| 3→4 | Added `tbr INTEGER NOT NULL DEFAULT 0` column |

**Always add a migration when changing the `Item` entity schema.** Do not use `fallbackToDestructiveMigration()`.

`Item` entity fields:
- `id` (auto-generated PK)
- `type` — `"boek"`, `"muziek"`, `"dvd"`, `"game"` (Dutch type strings used internally)
- `title`, `authorOrArtist`, `code` (ISBN/EAN/barcode)
- `isReadOrListened`, `inPossession`, `tbr` (boolean flags)
- `medium` — `"cd"`, `"lp"`, or `null`
- `language` — `"NL"`, `"EN"`, or free text
- `coverUrl`, `googleSearchUrl`

### UI Architecture

- **Single Activity** (`MainActivity`) containing all Compose screens.
- Uses **ViewModel** (created via `ViewModelProvider.Factory` in `MainActivity`, not Hilt).
- State is managed with `StateFlow` / `MutableStateFlow` in the ViewModel.
- Screens are top-level `@Composable` functions, not separate activities/fragments.
- `@OptIn(ExperimentalMaterial3Api::class)` is applied file-wide in `MainActivity.kt`.

Main screens:
- `ItemListScreen` — library list, sort, filter, demo banner, unlock flow
- `AddItemDialog` — full-screen dialog for add/edit
- `BarcodeScannerScreen` — CameraX + ML Kit barcode scanner
- `CoverFetchDialog` / `CoverPreviewModal` — cover image search/preview
- `SettingsScreen` — language, large text, high contrast

### Accessibility

This app has explicit WCAG compliance goals for senior users:

- **Minimum body text**: 18sp (enforced in `AccessibleTypography.kt`)
- **Large text mode**: ×1.25 scale factor across all typography styles (stored in `app_prefs`)
- **High contrast mode**: WCAG AAA (21:1) black/white scheme (`HighContrastLightColorScheme` / `HighContrastDarkColorScheme`)
- Both settings are stored via `SettingsHelper` in `SharedPreferences("app_prefs")`.
- Dynamic colour (Material You) is **disabled** (`dynamicColor = false`) to preserve brand palette.

When adding UI, always use `MaterialTheme.typography` and `MaterialTheme.colorScheme` — never hardcode colours or text sizes.

### Localisation

- Default strings: `res/values/strings.xml` (Dutch — used as the master/fallback)
- English strings: `res/values-en/strings.xml`
- Language preference managed by `LanguageHelper` (options: `"system"`, `"nl"`, `"en"`).
- When adding a new string, add it to **both** `strings.xml` and `values-en/strings.xml`.
- String keys use snake_case with semantic grouping prefixes (e.g., `unlock_verify_`, `settings_`, `item_`).

### Unlock / Payment Flow

1. Demo user hits 10-item limit.
2. App shows payment prompt → opens Stripe payment link in browser.
3. After payment, user taps "I've paid — unlock" → enters email.
4. App calls `POST https://<vercel-domain>/api/verify-purchase` with `{"email":"..."}`.
5. API checks Stripe for a completed checkout session with matching email and product.
6. On success → `UnlockHelper.unlockDirectly(context)` persists unlock state.

`UnlockHelper` stores state in `SharedPreferences("app_prefs")` key `app_unlocked`.

**Note**: There is also a legacy static unlock code (`SENIOREASE2025`) in `UnlockHelper`, but the active UI flow uses email-based verification only.

### Key Dependencies

| Library | Version | Purpose |
|---|---|---|
| Jetpack Compose BOM | 2024.09.00 | UI framework |
| Material3 | via BOM | Design system |
| Room | 2.6.1 | Local SQLite database |
| CameraX | 1.3.x | Barcode camera preview |
| ML Kit Barcode Scanning | 17.2.0 | Barcode decoding |
| Coil Compose | 2.5.0 | Async cover image loading |
| OkHttp | 4.11.0 | HTTP for book/cover API calls |
| Gson | 2.10.1 | JSON parsing |

---

## Website (`website/`)

Static HTML download page deployed on **Vercel**:

- `index.html` — landing page with download button, screenshots, pricing
- `api/verify-purchase.js` — **active** Vercel serverless function for purchase verification
- `api/reset-download.js` — support tool to reset `download_used` metadata on a PaymentIntent
- `vercel.json` — sets `Content-Type: application/vnd.android.package-archive` for `/downloads/*`
- APK files go in `website/downloads/` (not committed to git; uploaded separately)

### Deploying the Website

```bash
cd website
# Deploy via Vercel CLI
vercel --prod

# Or push to GitHub; Vercel auto-deploys from main branch
```

Root directory in Vercel project settings must be set to `website`.

### Environment Variables (Vercel)

| Variable | Required | Description |
|---|---|---|
| `STRIPE_SECRET_KEY` | Yes | Stripe secret key (`sk_live_...` or `sk_test_...`) |
| `STRIPE_PRODUCT_ID` | Yes | Stripe Product ID to validate purchases against |

---

## Purchase Verification API

There are **three implementations** of the purchase verification API — only `website/api/verify-purchase.js` is active in production:

### Active: `website/api/verify-purchase.js`
- Vercel serverless function
- Triple-strategy Stripe lookup: (1) search by email+paid, (2) search paid-only + local match, (3) list recent sessions
- Marks used purchases via PaymentIntent metadata (`download_used: "true"`) to prevent reuse
- Rate limit: 20 req/min per IP
- Requires `STRIPE_PRODUCT_ID` env var (throws on startup if missing)

### Legacy: `nextjs-api/pages/api/verify-purchase.ts`
- Next.js 14 TypeScript version
- Simpler: single-pass checkout session list, matches by price ID (`TEST_PRICE_ID` hardcoded)
- Rate limit: 10 req/15 min per IP
- Not deployed; kept for reference

### Legacy: `api/server.js`
- Express.js standalone server
- Two-method search (PaymentIntents → Checkout Sessions)
- Rate limit: 10 req/15 min per IP (via `express-rate-limit`)
- Not deployed; kept for reference

---

## Important Security Notes

> **WARNING — hardcoded keystore credentials**: `app/build.gradle.kts` contains the keystore store/key password in plaintext. Do not copy or log these values. In a production setup, move them to `local.properties` or environment variables.

- The static unlock code `SENIOREASE2025` in `UnlockHelper.kt` is not the active unlock path (email verification is used instead), but it should not be publicised.
- API keys and Stripe secrets must never be committed — they are injected as Vercel environment variables.
- Both serverless endpoints implement IP-based rate limiting to prevent brute-force purchase verification.

---

## Git Conventions

- **Main branch**: `master`
- **Feature branches**: `claude/<description>` for AI-generated changes
- Commit messages are imperative and scoped, e.g. `Fix mobile scroll on homepage`, `Add third TikTok instructional video`
- No conventional-commits prefix enforced, but use natural language that describes what changed and why

---

## Files to Ignore / Not Modify

- `app/release/*.aab` — build artifacts, do not edit
- `app/demo/release/*.apk` — distribution APKs, do not edit
- `*.jks` — keystore files, do not edit or commit new passwords
- `Tiktok/` — marketing video assets
- `Screenshots/` — marketing screenshots
- `*.md` files in `website/` (Dutch how-to guides for the owner, not code docs)
- Root-level Dutch `.md` files (e.g., `APK-BOUWEN-NU.md`) — operational guides for the owner

---

## Common Tasks

### Adding a New Item Type

1. Add the type string to `Item.kt` comment.
2. Add display strings to both `values/strings.xml` and `values-en/strings.xml`.
3. Update type handling in `AddItemDialog.kt` and `ItemListScreen.kt`.
4. No database migration needed (type is stored as a `TEXT` field).

### Adding a New Room Column

1. Add the field to `Item.kt`.
2. Increment `@Database(version = N)` in `AppDatabase.kt`.
3. Add a `MIGRATION_(N-1)_N` object with the `ALTER TABLE` statement.
4. Register the migration in `addMigrations(...)`.

### Adding a New Settings Toggle

1. Add a key constant and getter/setter to `SettingsHelper.kt`.
2. Add UI in `SettingsScreen.kt`.
3. Thread the value through `BiblitoheekTheme` or wherever it applies.
4. Add strings to both `strings.xml` files.

### Updating the Demo APK for Distribution

1. Build: `./gradlew :app:assembleDemoRelease`
2. Copy output to `website/downloads/app-demo-release.apk` (or update filename in `index.html`).
3. Update QR code if the download URL changed.
4. Deploy website to Vercel.
