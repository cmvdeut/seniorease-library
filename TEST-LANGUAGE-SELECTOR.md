# 🧪 Taalselector Testen

## ✅ Wat is Gefixed

1. **attachBaseContext override** - Taal wordt nu correct ingesteld VOOR onCreate
2. **createConfigurationContext** - De geretourneerde context wordt nu gebruikt
3. **Activity recreate** - Kleine delay toegevoegd zodat het settings dialoog eerst sluit

---

## 🧪 Hoe Te Testen

### Stap 1: Build de App
```
In Android Studio: Build → Make Project
```

### Stap 2: Installeer op Emulator/Telefoon
- Run de app op een emulator of echte telefoon
- Zorg dat de device taal op **Nederlands** staat (voor duidelijk contrast)

### Stap 3: Test Taalwijziging

1. **Open de App**
   - App start in Nederlands (device taal)

2. **Open Instellingen**
   - Klik op menu (3 puntjes rechtsboven)
   - Klik op "Instellingen"

3. **Wijzig naar Engels**
   - Scroll naar "Taal" sectie
   - Klik op de dropdown knop (toont "Systeemtaal" of "Nederlands")
   - Selecteer "English"
   - **Verwacht gedrag:** App herstart automatisch

4. **Verifieer Engels**
   - Na herstart zou ALLES in Engels moeten zijn:
     - Menu items: "Menu", "Create backup", "Statistics", etc.
     - Knoppen: "Add", "Search", "OK", "Cancel"
     - Labels: "Title", "Author", "Type", etc.
     - Alle dialogen en berichten

5. **Wijzig terug naar Nederlands**
   - Open Instellingen opnieuw
   - Selecteer "Nederlands"
   - **Verwacht gedrag:** App herstart opnieuw

6. **Verifieer Nederlands**
   - Alles zou nu weer in Nederlands moeten zijn

---

## 🔍 Debug Tips

### Als het niet werkt:

1. **Check SharedPreferences**
   ```kotlin
   // Voeg toe in SettingsScreen voor debug:
   Log.d("Language", "Saved: ${LanguageHelper.getSavedLanguage(context)}")
   ```

2. **Check Locale**
   ```kotlin
   // In MainActivity.attachBaseContext:
   Log.d("Language", "Locale: ${locale.language}")
   ```

3. **Check Activity Recreate**
   - Zorg dat `recreate()` wordt aangeroepen
   - Check of er errors zijn in Logcat

4. **Test op Verschillende Android Versies**
   - Android 7.0+ (API 24+): gebruikt `createConfigurationContext`
   - Android 6.0 en lager: gebruikt `updateConfiguration`

---

## ✅ Verwachte Resultaten

### Nederlands:
- "Bibliotheek" (niet "Library")
- "Toevoegen" (niet "Add")
- "Zoeken" (niet "Search")
- "Gelezen" (niet "Read")
- "In bezit" (niet "In possession")

### Engels:
- "Library" (niet "Bibliotheek")
- "Add" (niet "Toevoegen")
- "Search" (niet "Zoeken")
- "Read" (niet "Gelezen")
- "In possession" (niet "In bezit")

---

## 🐛 Bekende Issues

- **Activity recreate kan kort duren** - Dit is normaal, de app moet opnieuw opstarten
- **Settings dialoog sluit automatisch** - Dit is gewenst gedrag

---

**Klaar om te testen!** 🎉
