# 🔍 App Unlock Debug - API Werkt Maar App Toont Fout

## ✅ Status
- ✅ API werkt: `{ "paid": true }`
- ❌ App toont nog steeds foutmelding

---

## 🔍 Mogelijke Oorzaken

### 1. App Gebruikt Oude APK
**Probleem:** App is gebouwd voordat de unlock code was toegevoegd
**Check:**
- Wanneer is de APK gebouwd?
- Is het na de unlock code implementatie?

**Fix:**
- Rebuild de app
- Installeer nieuwe APK

### 2. Exception Wordt Gevangen
**Probleem:** Er is een exception maar die wordt niet getoond
**Check:**
- Open Android Studio
- Verbind telefoon
- Open Logcat
- Filter op: `UnlockVerify`
- Check voor errors

**Fix:**
- Check de exception message
- Fix de code

### 3. UI Update Niet Correct
**Probleem:** Unlock wordt opgeslagen maar UI update niet
**Check:**
- Check of `viewModel.loadItems()` wordt aangeroepen
- Check of `showUnlockVerifyDialog = false` wordt gezet

**Fix:**
- Mogelijk moet `LaunchedEffect` worden gebruikt voor UI update

### 4. SharedPreferences Niet Opgeslagen
**Probleem:** `commit()` faalt stil
**Check:**
- Check Logcat voor errors
- Test of unlock status wordt opgeslagen

**Fix:**
- Gebruik `apply()` in plaats van `commit()`
- Of check return value van `commit()`

---

## 🧪 Debug Stappen

### Stap 1: Check Logcat

**In Android Studio:**

1. **Verbind telefoon**
2. **Open Logcat**
3. **Filter op:** `UnlockVerify`
4. **Test unlock opnieuw**
5. **Check logs voor:**
   - `Response code: 200`
   - `Response body: {"paid":true}`
   - `JSON parsing error` (als dit er is)
   - `Exception type: ...` (als dit er is)

### Stap 2: Check Unlock Status

**Na unlock poging:**

1. **Check SharedPreferences:**
   - App data → Clear data
   - Of: Check via ADB: `adb shell run-as com.seniorease.library cat /data/data/com.seniorease.library/shared_prefs/app_prefs.xml`
   - Zoek naar: `app_unlocked` = `true`

### Stap 3: Test Unlock Direct

**Als unlock niet werkt via API:**

1. **Test unlock code:**
   - Gebruik unlock code: `SENIOREASE2025`
   - Check of dit werkt

**Als unlock code wel werkt:**
- Probleem is in API response parsing
- Check JSON parsing code

**Als unlock code niet werkt:**
- Probleem is in unlock logica zelf
- Check `UnlockHelper.unlockDirectly()`

---

## 🔧 Mogelijke Fixes

### Fix 1: Gebruik `apply()` in plaats van `commit()`

```kotlin
fun unlockDirectly(context: Context): Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putBoolean(KEY_UNLOCKED, true).apply() // apply() in plaats van commit()
    return true
}
```

### Fix 2: Force UI Update

```kotlin
if (paid) {
    UnlockHelper.unlockDirectly(context)
    viewModel.loadItems()
    // Force recomposition
    showUnlockVerifyDialog = false
    showUnlockDialog = false
    // Toast
    // ...
}
```

### Fix 3: Check Response Parsing

**Mogelijk probleem:** Response heeft extra whitespace of formatting

```kotlin
val responseBody = response.body?.string()?.trim()
val jsonResponse = org.json.JSONObject(responseBody)
val paid = jsonResponse.optBoolean("paid", false)
```

---

## 📋 Checklist

- [ ] Check Logcat voor errors
- [ ] Check response body in logs
- [ ] Check of unlock status wordt opgeslagen
- [ ] Check of UI update wordt getriggerd
- [ ] Test unlock code direct (SENIOREASE2025)
- [ ] Rebuild app als nodig

---

## 🚀 Snelle Fix

**1. Check Logcat voor errors**
**2. Check response body: moet `{"paid":true}` zijn**
**3. Check of unlock status wordt opgeslagen**
**4. Rebuild app als oude versie**

**Deel de Logcat logs zodat we kunnen zien wat er misgaat!**
