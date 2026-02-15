# CLAUDE.md - SeniorEase Library

## Project Overview

SeniorEase Library is an accessibility-focused Android application for managing personal book and media collections. Users can scan barcodes (ISBN/EAN), catalog items, track reading status, and export data. The project includes the Android app, backend APIs for Stripe payment verification, and a static download website.

**Monetization**: Freemium model with a demo flavor (10-item limit) and a paid full version unlocked via Stripe purchase.

**Languages**: The app supports Dutch (default) and English. Code comments and documentation are a mix of Dutch and English.

## Repository Structure

```
/
├── app/                    # Android application (Kotlin + Jetpack Compose)
│   ├── build.gradle.kts    # App-level Gradle config (flavors, signing, dependencies)
│   ├── proguard-rules.pro  # ProGuard/R8 rules
│   ├── src/main/java/com/seniorease/library/
│   │   ├── MainActivity.kt           # Main entry point, navigation, export, settings
│   │   ├── ui/
│   │   │   ├── ItemListScreen.kt      # Book/media list with sorting and filtering
│   │   │   ├── AddItemDialog.kt       # Add/edit items, barcode scanning integration
│   │   │   ├── BarcodeScannerScreen.kt # Camera-based barcode scanner (ML Kit)
│   │   │   ├── SettingsScreen.kt      # Language and accessibility settings
│   │   │   ├── CoverFetchDialog.kt    # Book cover fetching UI
│   │   │   ├── CoverPreviewModal.kt   # Cover image preview
│   │   │   └── theme/                 # Material 3 theming, colors, typography
│   │   ├── data/
│   │   │   ├── AppDatabase.kt         # Room database with migrations (v1→v4)
│   │   │   ├── Item.kt                # Room entity data class
│   │   │   └── ItemDao.kt             # DAO interface for CRUD operations
│   │   └── utils/
│   │       ├── UnlockHelper.kt        # Purchase verification via SharedPreferences
│   │       ├── LanguageHelper.kt      # Multi-language support (NL/EN)
│   │       └── SettingsHelper.kt      # Accessibility settings persistence
│   └── src/main/res/                  # Android resources (layouts, strings, drawables)
├── api/                    # Express.js API for Stripe purchase verification
│   ├── server.js           # Express server with rate limiting
│   └── package.json
├── nextjs-api/             # Next.js alternative API implementation
│   ├── pages/api/verify-purchase.ts  # TypeScript API route
│   └── package.json
├── website/                # Static download page (deployed to Vercel)
│   ├── api/verify-purchase.js  # Vercel serverless function
│   └── package.json
├── build.gradle.kts        # Root Gradle configuration
├── settings.gradle.kts     # Gradle settings (project name: "Biblitoheek")
├── gradle/libs.versions.toml  # Gradle version catalog
└── gradle.properties       # JVM and AndroidX configuration
```

## Tech Stack

### Android App
- **Language**: Kotlin 2.0.21
- **UI**: Jetpack Compose with Material 3
- **Database**: Room 2.6.1 (SQLite ORM) with kapt annotation processing
- **Image loading**: Coil 2.5.0
- **Networking**: OkHttp 4.11.0
- **Barcode scanning**: Google ML Kit 17.2.0
- **Camera**: CameraX 1.3.3
- **Serialization**: Gson 2.10.1
- **Min SDK**: 35 (Android 14) / **Target SDK**: 36 (Android 15)
- **JVM Target**: Java 11
- **Compose Compiler**: 1.5.10

### Backend
- **Express API**: Node.js, Express 4.18, Stripe 14.21, express-rate-limit
- **Next.js API**: Next.js 14, TypeScript 5, Stripe 14.21
- **Website**: Static HTML/JS on Vercel with Stripe serverless function

## Build Commands

### Android App (via Gradle wrapper)

```bash
# Debug builds
./gradlew assembleFullDebug       # Full version debug APK
./gradlew assembleDemoDebug       # Demo version debug APK

# Release builds
./gradlew assembleFullRelease     # Full version release APK
./gradlew assembleDemoRelease     # Demo version release APK
./gradlew bundleFullRelease       # Google Play AAB bundle
```

### Product Flavors

| Flavor | App ID Suffix | `IS_DEMO` | `MAX_ITEMS` | Description |
|--------|--------------|-----------|-------------|-------------|
| `full` | _(none)_ | `false` | `-1` (unlimited) | Paid/unlocked version |
| `demo` | `.demo` | `true` | `10` | Free trial, limited items |

### API Servers

```bash
# Express API (/api)
cd api && npm install && npm start    # Runs on port 3000

# Next.js API (/nextjs-api)
cd nextjs-api && npm install && npm run dev   # Dev server on port 3000
cd nextjs-api && npm run build && npm start   # Production build

# Website (/website)
cd website && npm install && npm run build    # Static build
```

## Testing

- **Unit tests**: JUnit 4.13.2 at `app/src/test/`
- **Instrumented tests**: AndroidX Test + Espresso at `app/src/androidTest/`
- **Compose UI tests**: `ui-test-junit4`

```bash
./gradlew test                    # Run unit tests
./gradlew connectedAndroidTest    # Run instrumented tests (requires device/emulator)
```

Note: The test directories currently have no committed test files.

## Architecture & Patterns

### Code Organization
- **UI layer** (`ui/`): Composable screens and dialogs
- **Data layer** (`data/`): Room database, entities, DAOs
- **Utilities** (`utils/`): Helper classes for settings, language, unlock state

### State Management
- `ViewModel` with `Flow`/`StateFlow` for reactive state
- `mutableStateOf` for local Compose UI state
- `rememberCoroutineScope` for async operations in Composables

### Database
- Room database at version 4 with explicit migrations (1→2→3→4)
- Single `items` table with fields: id, type, title, authorOrArtist, code, isReadOrListened, inPossession, tbr, medium, language, coverUrl, googleSearchUrl
- All DAO methods are `suspend` functions
- Singleton pattern via `companion object` with `@Volatile` + `synchronized`

### API Pattern
- Email-based Stripe purchase verification
- Rate limiting per IP (10-15 requests per 15 minutes)
- Case-insensitive email matching
- Multiple fallback search strategies for Stripe lookups

## Naming Conventions

| Element | Convention | Examples |
|---------|-----------|----------|
| Composable functions | PascalCase | `ItemListScreen`, `AddItemDialog` |
| Regular functions | camelCase | `stripVoorzetsel`, `extractAchternaam` |
| Constants | UPPER_SNAKE_CASE | `VALID_UNLOCK_CODE`, `TEST_PRICE_ID` |
| String resources | snake_case | `app_name`, `demo_status` |
| Data classes | PascalCase | `Item`, `BookSearchResult` |
| Package names | lowercase | `com.seniorease.library.data` |

## Configuration & Environment

### Android SharedPreferences Keys
- `app_language` — Language setting: `"system"`, `"nl"`, `"en"`
- `app_unlocked` — Boolean unlock state
- `large_text_enabled` — Accessibility: large text mode
- `high_contrast_enabled` — Accessibility: high contrast mode

### BuildConfig Fields (set per flavor)
- `IS_DEMO` (boolean) — Whether running in demo mode
- `MAX_ITEMS` (int) — Item limit (-1 = unlimited, 10 for demo)

### Environment Variables (API servers)
- `STRIPE_SECRET_KEY` — Required for all API implementations
- `STRIPE_PRODUCT_ID` — Required for website serverless function
- `PORT` — Optional, defaults to 3000

## Accessibility

The app has substantial accessibility support built in:
- **Large Text Mode**: Scales all typography for readability
- **High Contrast Mode**: WCAG AAA compliant (21:1 contrast ratio)
- Accessible Material 3 color schemes
- Custom `AccessibleTypography` with scalable font definitions

## Deployment

- **Android**: Manual release builds, uploaded to Google Play as AAB
- **Website**: Deployed to Vercel at `seniorease.eu`
- **APIs**: Express can be self-hosted; Next.js/Website versions run on Vercel
- No CI/CD pipelines (GitHub Actions) are configured

## Key External Services

- **Stripe** — Payment processing and purchase verification
- **Google Books API** — Book metadata and cover art lookups
- **UPC Item Database API** — Game/media barcode information
- **Google ML Kit** — On-device barcode scanning

## Important Notes for AI Assistants

1. **Signing credentials are in `build.gradle.kts`** — The release signing config contains hardcoded keystore passwords. Do not expose these further or commit them to public repositories.
2. **Dutch/English mix** — Code comments, variable names (e.g., `stripVoorzetsel`), and item types (`"boek"`, `"muziek"`, `"dvd"`, `"spel"`) use Dutch. UI strings support both languages via resources.
3. **MainActivity is large** (~1,160 lines) — It contains navigation, export logic, settings, and unlock flows. When making changes, be mindful of its scope.
4. **Room migrations are sequential** — When adding database columns, always create a new migration object and increment the version number.
5. **No automated test suite** — Test directories exist but contain no committed tests. Manual QA via APK builds.
6. **Keystore files at root** — `upload-keystore.jks` and `bieb-keystore.jks` are in the repository root. These are sensitive files.
