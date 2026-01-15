# 🔧 Handmatig Pushen naar GitHub

## ⚠️ Probleem
- `index.html` op GitHub is nog van 3 dagen geleden
- Automatische push werkt niet

## ✅ Handmatige Oplossing

### Stap 1: Open PowerShell in Project Folder

```powershell
cd D:\MAUREEN\DEV\SeniorEase-Library
```

### Stap 2: Check Status

```powershell
git status
```

**Als je uncommitted changes ziet:**
- `website/index.html` zou moeten verschijnen als gewijzigd

### Stap 3: Add en Commit

```powershell
git add website/index.html
git commit -m "Update responsive mobile homepage with NL/EN support"
```

### Stap 4: Push

```powershell
git push origin master
```

**Als dit faalt met "updates were rejected":**

```powershell
git pull origin master
git push origin master
```

**Als dit nog steeds faalt:**

```powershell
git push origin master --force
```

⚠️ **Waarschuwing:** `--force` overschrijft remote changes. Gebruik alleen als je zeker weet dat je de juiste versie hebt.

### Stap 5: Verifieer op GitHub

1. **Ga naar:** https://github.com/cmvdeut/seniorease-library
2. **Klik op:** `website` folder
3. **Klik op:** `index.html`
4. **Check:**
   - Laatste wijziging zou "just now" of "a few minutes ago" moeten zijn
   - Zoek naar `mobile-short` in de code
   - Zoek naar `data-lang="nl"` en `data-lang="en"`

---

## 🔍 Troubleshooting

### "Permission denied"
- Check of je ingelogd bent in GitHub
- Check of je SSH key of token correct is ingesteld

### "Repository not found"
- Check of de repository naam correct is: `cmvdeut/seniorease-library`
- Check of je toegang hebt tot de repository

### "Everything up-to-date"
- Dit betekent dat er geen nieuwe commits zijn om te pushen
- Check of je wijzigingen wel gecommit zijn: `git log --oneline -3`

---

**Probeer handmatig te pushen via PowerShell!**
