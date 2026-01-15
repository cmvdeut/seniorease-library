# 🇬🇧 Android App - Engelse Vertaling Plan

## 📋 Huidige Situatie

### Wat ik gevonden heb:
1. **Geen i18n setup** - Alleen `values/strings.xml` met alleen `app_name`
2. **Alle teksten hardcoded** - Nederlandse teksten staan direct in Kotlin code
3. **Veel teksten** - ~175 hardcoded strings gevonden

### Bestanden met teksten:
- `MainActivity.kt` - Menu items, dialogen, toasts
- `ItemListScreen.kt` - UI labels (Zoeken, Sorteren, Filter, etc.)
- `AddItemDialog.kt` - Formulier labels, buttons, messages
- `BarcodeScannerScreen.kt` - Scanner teksten
- `CoverFetchDialog.kt` - Cover ophalen teksten
- `SettingsScreen.kt` - Settings teksten

---

## 🎯 Plan van Aanpak

### Stap 1: Maak String Resources
1. **Maak `values/strings.xml`** - Alle Nederlandse teksten
2. **Maak `values-en/strings.xml`** - Alle Engelse vertalingen

### Stap 2: Vervang Hardcoded Strings
1. Vervang alle `Text("...")` door `Text(stringResource(R.string.xxx))`
2. Vervang alle `Toast.makeText(..., "...")` door `Toast.makeText(..., getString(R.string.xxx))`
3. Vervang alle hardcoded strings in PDF/CSV export

### Stap 3: Test
1. Build app in Android Studio
2. Test op emulator met Engelse taal
3. Test op emulator met Nederlandse taal

---

## 📝 Alle Teksten die Vertaald Moeten Worden

### MainActivity.kt
- "Bibliotheek"
- "Demo - X/Y items"
- "Backup maken"
- "Backup terugzetten"
- "Exporteren naar CSV"
- "Exporteren naar PDF"
- "PDF delen"
- "Statistieken"
- "Privacybeleid"
- "Wis alle data"
- "Demo versie limiet bereikt! Maximaal X items toegestaan. Koop de volledige versie voor onbeperkt gebruik."
- "Backup opgeslagen"
- "Fout bij opslaan backup"
- "X items toegevoegd uit backup"
- "Fout bij importeren backup"
- "CSV geëxporteerd"
- "Fout bij exporteren CSV"
- "PDF geëxporteerd"
- "Fout bij exporteren PDF"
- "Titel", "Auteur/Artiest", "Type", "Code", "Gelezen", "In bezit" (PDF headers)
- "Ja", "Nee" (PDF values)
- "Statistieken" (dialog title)
- "Totaal: X"
- "Boeken: X (NL: X, EN: X, Anders: X)"
- "Muziek: X (cd's: X, lp's: X)"
- "Games: X"
- "DVD's: X"
- "Gelezen/beluisterd: X"
- "In bezit: X"
- "OK"
- "⚠️ WAARSCHUWING: Wis alle data"
- "WAARSCHUWING: Deze actie zal ALLE data in je bibliotheek permanent verwijderen!\n\nDit is onomkeerbaar en kan niet ongedaan worden gemaakt.\n\nHeb je eerst een backup gemaakt?"
- "Alle data is gewist!"
- "JA, WIS ALLE DATA"
- "Annuleren"
- Privacy policy teksten (lange tekst)

### ItemListScreen.kt
- "Toevoegen"
- "Zoeken"
- "Sorteren:"
- "Titel", "Auteur", "Gelezen", "In bezit" (sort options)
- "Filter:"
- "Alles"
- "Gelezen"
- "In bezit"
- "Type: X"
- "Code: X"
- "Gelezen" / "Beluisterd" (checkbox label)
- "In bezit" (checkbox label)
- "Wis tekst" (content description)

### AddItemDialog.kt
- "Nieuw item toevoegen"
- "Item bewerken"
- "Boekgegevens ophalen..."
- "Deze code bestaat al in je collectie!"
- "Type:"
- "Boek", "Muziek", "DVD", "Game"
- "Google"
- "Titel"
- "Geen boek gevonden. Controleer de titel of auteur."
- "Zoeken" (button)
- "Sluiten"
- "Auteur"
- "ISBN/EAN code"
- "Cover:"
- "Medium:"
- "cd", "lp", "anders"
- "Zoek op Google"
- "Scan barcode"
- "Gelezen/Beluisterd"
- "In bezit"
- "Taal:"
- "Nederlands", "Engels", "Anders"
- "Voer taal in"
- "OK"
- "Annuleren"
- "Verwijderen"
- "Weet je zeker dat je dit item wilt verwijderen?"
- "Ja, verwijderen"

### BarcodeScannerScreen.kt
- "Camera-toestemming is nodig om te scannen."
- "Fout: X"

### CoverFetchDialog.kt
- (Teksten moeten nog bekeken worden)

### SettingsScreen.kt
- (Teksten moeten nog bekeken worden)

---

## 🔧 Implementatie Stappen

### Stap 1: Maak String Resources Bestanden

**`app/src/main/res/values/strings.xml`** (Nederlands - uitbreiden)
**`app/src/main/res/values-en/strings.xml`** (Engels - nieuw)

### Stap 2: Code Aanpassingen

Voor elke hardcoded string:
```kotlin
// VOOR:
Text("Bibliotheek")

// NA:
Text(stringResource(R.string.library))
```

Voor Toasts:
```kotlin
// VOOR:
Toast.makeText(context, "Backup opgeslagen", Toast.LENGTH_LONG).show()

// NA:
Toast.makeText(context, context.getString(R.string.backup_saved), Toast.LENGTH_LONG).show()
```

---

## 📦 String Resources Structuur

```xml
<!-- Common -->
<string name="app_name">SeniorEasy Bieb</string>
<string name="ok">OK</string>
<string name="cancel">Annuleren</string>
<string name="close">Sluiten</string>
<string name="delete">Verwijderen</string>
<string name="add">Toevoegen</string>
<string name="search">Zoeken</string>
<string name="yes">Ja</string>
<string name="no">Nee</string>

<!-- Main Screen -->
<string name="library">Bibliotheek</string>
<string name="demo_status">Demo - %1$d/%2$d items</string>

<!-- Menu -->
<string name="backup_create">Backup maken</string>
<string name="backup_restore">Backup terugzetten</string>
<string name="export_csv">Exporteren naar CSV</string>
<string name="export_pdf">Exporteren naar PDF</string>
<string name="share_pdf">PDF delen</string>
<string name="statistics">Statistieken</string>
<string name="privacy_policy">Privacybeleid</string>
<string name="clear_all_data">Wis alle data</string>

<!-- Sort & Filter -->
<string name="sort">Sorteren:</string>
<string name="filter">Filter:</string>
<string name="sort_title">Titel</string>
<string name="sort_author">Auteur</string>
<string name="sort_read">Gelezen</string>
<string name="sort_possession">In bezit</string>
<string name="filter_all">Alles</string>
<string name="filter_read">Gelezen</string>
<string name="filter_possession">In bezit</string>

<!-- Item Dialog -->
<string name="add_item">Nieuw item toevoegen</string>
<string name="edit_item">Item bewerken</string>
<string name="item_title">Titel</string>
<string name="item_author">Auteur</string>
<string name="item_code">ISBN/EAN code</string>
<string name="item_type">Type:</string>
<string name="item_type_book">Boek</string>
<string name="item_type_music">Muziek</string>
<string name="item_type_dvd">DVD</string>
<string name="item_type_game">Game</string>
<string name="item_read">Gelezen</string>
<string name="item_listened">Beluisterd</string>
<string name="item_in_possession">In bezit</string>
<string name="item_language">Taal:</string>
<string name="item_language_nl">Nederlands</string>
<string name="item_language_en">Engels</string>
<string name="item_language_other">Anders</string>
<string name="item_medium">Medium:</string>
<string name="item_medium_cd">cd</string>
<string name="item_medium_lp">lp</string>
<string name="item_medium_other">anders</string>
<string name="item_cover">Cover:</string>
<string name="scan_barcode">Scan barcode</string>
<string name="search_google">Zoek op Google</string>

<!-- Messages -->
<string name="demo_limit_reached">Demo versie limiet bereikt! Maximaal %1$d items toegestaan. Koop de volledige versie voor onbeperkt gebruik.</string>
<string name="backup_saved">Backup opgeslagen</string>
<string name="backup_error">Fout bij opslaan backup</string>
<string name="backup_imported">%1$d items toegevoegd uit backup</string>
<string name="backup_import_error">Fout bij importeren backup</string>
<string name="csv_exported">CSV geëxporteerd</string>
<string name="csv_export_error">Fout bij exporteren CSV</string>
<string name="pdf_exported">PDF geëxporteerd</string>
<string name="pdf_export_error">Fout bij exporteren PDF</string>
<string name="duplicate_code">Deze code bestaat al in je collectie!</string>
<string name="book_not_found">Geen boek gevonden. Controleer de titel of auteur.</string>
<string name="fetching_book_data">Boekgegevens ophalen...</string>

<!-- Delete Confirmation -->
<string name="delete_confirm_title">Weet je zeker dat je dit item wilt verwijderen?</string>
<string name="delete_confirm_yes">Ja, verwijderen</string>

<!-- Clear All Data -->
<string name="clear_all_title">⚠️ WAARSCHUWING: Wis alle data</string>
<string name="clear_all_message">WAARSCHUWING: Deze actie zal ALLE data in je bibliotheek permanent verwijderen!\n\nDit is onomkeerbaar en kan niet ongedaan worden gemaakt.\n\nHeb je eerst een backup gemaakt?</string>
<string name="clear_all_confirm">JA, WIS ALLE DATA</string>
<string name="all_data_cleared">Alle data is gewist!</string>

<!-- Statistics -->
<string name="statistics_title">Statistieken</string>
<string name="statistics_total">Totaal: %1$d</string>
<string name="statistics_books">Boeken: %1$d (NL: %2$d, EN: %3$d, Anders: %4$d)</string>
<string name="statistics_music">Muziek: %1$d (cd\'s: %2$d, lp\'s: %3$d)</string>
<string name="statistics_games">Games: %1$d</string>
<string name="statistics_dvds">DVD\'s: %1$d</string>
<string name="statistics_read">Gelezen/beluisterd: %1$d</string>
<string name="statistics_possession">In bezit: %1$d</string>

<!-- PDF Headers -->
<string name="pdf_header_title">Titel</string>
<string name="pdf_header_author">Auteur/Artiest</string>
<string name="pdf_header_type">Type</string>
<string name="pdf_header_code">Code</string>
<string name="pdf_header_read">Gelezen</string>
<string name="pdf_header_possession">In bezit</string>

<!-- CSV Headers -->
<string name="csv_header_title">Titel</string>
<string name="csv_header_author">Auteur/Artiest</string>
<string name="csv_header_type">Type</string>
<string name="csv_header_code">Code</string>
<string name="csv_header_status">Gelezen/In bezit</string>
<string name="csv_header_medium">Medium</string>
<string name="csv_header_language">Taal</string>

<!-- Privacy Policy -->
<string name="privacy_policy_title">Privacybeleid</string>
<!-- (Lange privacy policy tekst) -->

<!-- Scanner -->
<string name="camera_permission_required">Camera-toestemming is nodig om te scannen.</string>
<string name="error">Fout: %1$s</string>
```

---

## 🚀 Volgende Stappen

1. **Maak string resources bestanden** (NL en EN)
2. **Pas Kotlin code aan** om string resources te gebruiken
3. **Test** op emulator met verschillende talen
4. **Build APK** in Engels

---

**Klaar om te beginnen!** 🎉

