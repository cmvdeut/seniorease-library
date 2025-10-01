package com.maureen.biblitoheek.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maureen.biblitoheek.data.Item
import com.maureen.biblitoheek.ui.BarcodeScannerScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.maureen.biblitoheek.data.AppDatabase
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment

// Fallback-functie voor medium bij muziek-items
suspend fun fetchGameInfoUpcitemdb(barcode: String): Pair<String, String>? {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val url = "https://api.upcitemdb.com/prod/trial/lookup?upc=$barcode"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: return@withContext null
            val json = JSONObject(body)
            val items = json.optJSONArray("items")
            if (items == null || items.length() == 0) return@withContext null
            val item = items.optJSONObject(0) ?: return@withContext null
            val title = item.optString("title", "")
            val brand = item.optString("brand", "")
            Pair(title, brand)
        } catch (e: Exception) {
            null
        }
    }
}

@Composable
fun AddItemDialog(
    onAdd: (Item) -> Unit,
    onDismiss: () -> Unit,
    item: Item? = null,
    onDelete: ((Item) -> Unit)? = null,
    initialType: String = "boek",
    onTypeChange: ((String) -> Unit)? = null,
    allAuthors: List<String> = emptyList()
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current
    var dvdNotFound by remember { mutableStateOf(false) }
    var type by remember { mutableStateOf(item?.type ?: initialType) }
    var title by remember { mutableStateOf(item?.title ?: "") }
    var authorOrArtist by remember { mutableStateOf(item?.authorOrArtist ?: "") }
    var code by remember { mutableStateOf(item?.code ?: "") }
    var isReadOrListened by remember { mutableStateOf(item?.isReadOrListened ?: false) }
    var inPossession by remember { mutableStateOf(item?.inPossession ?: false) }
    var showScanner by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var duplicateError by remember { mutableStateOf(false) }
    var debugInfo by remember { mutableStateOf("") }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var gameNotFound by remember { mutableStateOf(false) }
    var medium by remember { mutableStateOf(item?.medium) }
    var language by remember { mutableStateOf(item?.language ?: "NL") }
    var customLanguage by remember { mutableStateOf(if (item?.language != null && item.language != "NL" && item.language != "EN") item.language else "") }
    var coverUrl by remember { mutableStateOf(item?.coverUrl) }
    var googleSearchUrl by remember { mutableStateOf(item?.googleSearchUrl) }
    var showCoverPreview by remember { mutableStateOf(false) }
    var showCoverFetchDialog by remember { mutableStateOf(false) }
    // Geen debugregel meer nodig

    suspend fun fetchBookInfo(isbn: String): Triple<String, String, String?>? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val url = "https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: return@withContext null
                val json = JSONObject(body)
                val items = json.optJSONArray("items") ?: return@withContext null
                if (items.length() == 0) return@withContext null
                val volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo")
                val title = volumeInfo.optString("title", "")
                val authors = volumeInfo.optJSONArray("authors")
                val author = if (authors != null && authors.length() > 0) authors.getString(0) else ""
                
                // Haal cover URL op
                val imageLinks = volumeInfo.optJSONObject("imageLinks")
                val coverUrl = imageLinks?.optString("thumbnail")?.replace("http://", "https://")
                
                Triple(title, author, coverUrl)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun tryFetchAndFill(isbn: String) {
        isLoading = true
        val result = fetchBookInfo(isbn)
        if (result != null) {
            title = result.first
            if (authorOrArtist.isBlank()) {
                authorOrArtist = if (result.second.isNotBlank()) result.second else type
            }
            coverUrl = result.third
            // Genereer Google zoek URL
            googleSearchUrl = "https://www.google.com/search?q=${title.replace(" ", "+")}+${authorOrArtist.replace(" ", "+")}"
        }
        isLoading = false
    }

    // Helper die zowel het resultaat als de JSON teruggeeft
    suspend fun fetchDiscogsInfoWithJson(barcode: String): Triple<Pair<String, String>?, String?, String?> {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val url = "https://api.discogs.com/database/search?barcode=$barcode&type=release&token=ohIRvHHOdaUMTJIImBDJRXPhMjOZowRZZrDvAzVh"
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "BiblitoheekApp/1.0 (maureen@email.com)")
                    .build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: return@withContext Triple(null, null, null)
                val json = JSONObject(body)
                val results = json.optJSONArray("results")
                if (results == null || results.length() == 0) {
                    return@withContext Triple(null, body, null)
                }
                val release = results.optJSONObject(0) ?: return@withContext Triple(null, body, null)
                val title = release.optString("title", "")
                val artist = release.optJSONArray("artist")?.optString(0)
                    ?: release.optString("artist", "")
                // Haal medium uit 'format' of 'formats'
                var medium: String? = null
                val formats = release.optJSONArray("format") ?: release.optJSONArray("formats")
                if (formats != null && formats.length() > 0) {
                    medium = formats.optString(0)
                } else {
                    val formatStr = release.optString("format", null)
                    if (!formatStr.isNullOrBlank()) medium = formatStr
                }
                if (title.isBlank() && artist.isBlank()) {
                    return@withContext Triple(null, body, medium)
                }
                Triple(Pair(title, artist), body, medium)
            } catch (e: Exception) {
                Triple(null, "Discogs exception: ${e.message}", null)
            }
        }
    }

    // Functie om muziek cover op te halen via Discogs API
    suspend fun fetchMusicCover(ean: String, title: String, artist: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                // Probeer eerst met EAN
                var url = "https://api.discogs.com/database/search?barcode=$ean&type=release&token=ohIRvHHOdaUMTJIImBDJRXPhMjOZowRZZrDvAzVh"
                var request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "BiblitoheekApp/1.0 (maureen@email.com)")
                    .build()
                var response = client.newCall(request).execute()
                var body = response.body?.string() ?: ""
                var json = JSONObject(body)
                var results = json.optJSONArray("results")
                
                // Als EAN niet werkt, probeer met titel en artiest
                if (results == null || results.length() == 0) {
                    val searchQuery = "$title $artist".replace(" ", "+")
                    url = "https://api.discogs.com/database/search?q=$searchQuery&type=release&token=ohIRvHHOdaUMTJIImBDJRXPhMjOZowRZZrDvAzVh"
                    request = Request.Builder()
                        .url(url)
                        .header("User-Agent", "BiblitoheekApp/1.0 (maureen@email.com)")
                        .build()
                    response = client.newCall(request).execute()
                    body = response.body?.string() ?: return@withContext null
                    json = JSONObject(body)
                    results = json.optJSONArray("results")
                }
                
                if (results != null && results.length() > 0) {
                    val firstResult = results.getJSONObject(0)
                    val thumb = firstResult.optString("thumb")
                    if (thumb.isNotEmpty()) {
                        return@withContext thumb.replace("http://", "https://")
                    }
                }
                null
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun tryFetchAndFillDiscogs(barcode: String) {
        isLoading = true
        debugInfo = ""
        val cleanBarcode = barcode.trim().replace(" ", "")
        val codesToTry = mutableSetOf<String>()
        if (cleanBarcode.length == 12) {
            codesToTry.add(cleanBarcode)
            codesToTry.add("0$cleanBarcode")
        } else if (cleanBarcode.length == 13 && cleanBarcode.startsWith("0")) {
            codesToTry.add(cleanBarcode)
            codesToTry.add(cleanBarcode.substring(1))
        } else {
            codesToTry.add(cleanBarcode)
        }
        var result: Pair<String, String>? = null
        var lastJson: String? = null
        var foundMedium: String? = null
        for (codeTry in codesToTry) {
            val fetchResult = fetchDiscogsInfoWithJson(codeTry)
            lastJson = fetchResult.second
            if (fetchResult.first != null) {
                result = fetchResult.first
                foundMedium = fetchResult.third
                break
            }
        }
        if (result != null) {
            var discogsTitle = result.first
            var discogsArtist = result.second
            // Als artiest nog leeg is, probeer te splitsen op ' - '
            if (authorOrArtist.isBlank() && discogsTitle.contains(" - ")) {
                val parts = discogsTitle.split(" - ", limit = 2)
                if (parts.size == 2) {
                    discogsArtist = parts[0]
                    discogsTitle = parts[1]
                }
            }
            if (title.isBlank()) title = discogsTitle
            if (authorOrArtist.isBlank()) {
                authorOrArtist = if (discogsArtist.isNotBlank()) discogsArtist else type
            }
            // Vul medium in als gevonden
            if (foundMedium != null && medium.isNullOrBlank()) medium = foundMedium
            
            // Haal cover URL op
            val fetchedCoverUrl = fetchMusicCover(barcode, discogsTitle, discogsArtist)
            if (fetchedCoverUrl != null) {
                coverUrl = fetchedCoverUrl
            }
            
            // Genereer Google zoek URL voor muziek
            googleSearchUrl = "https://www.google.com/search?q=${title.replace(" ", "+")}+${authorOrArtist.replace(" ", "+")}+music"
        } else {
            // Fallback: probeer medium uit UPCitemdb (gebruik fetchGameInfoUpcitemdb voor muziek)
            val upcResult = fetchGameInfoUpcitemdb(barcode)
            if (upcResult != null && medium.isNullOrBlank()) {
                // UPCitemdb geeft soms het medium in het 'brand' veld
                medium = upcResult.second
            }
        }
        // fullJson niet meer nodig
        isLoading = false
    }

    suspend fun fetchDvdInfo(barcode: String): Pair<String, String>? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val url = "https://www.omdbapi.com/?apikey=47b06298&i=tt$barcode"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: return@withContext null
                val json = JSONObject(body)
                if (json.optString("Response") != "True") return@withContext null
                val title = json.optString("Title", "")
                val director = json.optString("Director", "")
                Pair(title, director)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun fetchDvdInfoUpcitemdb(barcode: String): Pair<String, String>? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val url = "https://api.upcitemdb.com/prod/trial/lookup?upc=$barcode"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: return@withContext null
                val json = JSONObject(body)
                val items = json.optJSONArray("items")
                if (items == null || items.length() == 0) return@withContext null
                val item = items.optJSONObject(0) ?: return@withContext null
                val title = item.optString("title", "")
                val brand = item.optString("brand", "")
                Pair(title, brand)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun tryFetchAndFillDvd(barcode: String) {
        isLoading = true
        val result = fetchDvdInfoUpcitemdb(barcode)
        if (result != null) {
            if (title.isBlank()) title = result.first
            if (authorOrArtist.isBlank()) authorOrArtist = type // Vul type in als artiest leeg is
            else authorOrArtist = result.second
            dvdNotFound = false
            
            // Genereer Google zoek URL voor DVD
            googleSearchUrl = "https://www.google.com/search?q=${title.replace(" ", "+")}+${authorOrArtist.replace(" ", "+")}+dvd"
        } else {
            dvdNotFound = true
        }
        isLoading = false
    }

    fun extractPlatformFromTitle(title: String): Pair<String, String> {
        val platforms = listOf(
            "PS4", "PlayStation 4", "PS5", "PlayStation 5", "Xbox One", "Xbox Series X", "Switch", "Nintendo Switch", "Wii U", "Wii", "PC", "Windows"
        )
        var foundPlatform: String? = null
        var cleanTitle = title
        for (platform in platforms) {
            val regex = Regex("\\b$platform\\b", RegexOption.IGNORE_CASE)
            if (regex.containsMatchIn(cleanTitle)) {
                foundPlatform = platform
                cleanTitle = cleanTitle.replace(regex, "").replace("()", "").replace("  ", " ").trim()
                break
            }
        }
        return Pair(cleanTitle.trim(), foundPlatform ?: "")
    }

    fun cleanGameTitle(title: String): String {
        // Verwijder leidende streepjes, spaties, haakjes
        var t = title.trim().removePrefix("-").trim().removeSurrounding("(", ")").trim()
        t = t.replace(Regex("""^[-\s]+"""), "").replace(Regex("""\s+"""), " ")
        // Capitaliseer elk woord
        t = t.split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } }
        return t
    }

    suspend fun tryFetchAndFillGame(barcode: String) {
        isLoading = true
        val result = fetchGameInfoUpcitemdb(barcode)
        if (result != null) {
            if (type == "game") {
                val (rawTitle, platform) = extractPlatformFromTitle(result.first)
                val cleanTitle = cleanGameTitle(rawTitle)
                if (title.isBlank()) title = cleanTitle
                if (authorOrArtist.isBlank()) authorOrArtist = type // Vul type in als artiest leeg is
                else if (authorOrArtist.isBlank()) authorOrArtist = if (platform.isNotBlank()) platform else result.second
                
                // Genereer Google zoek URL voor game
                googleSearchUrl = "https://www.google.com/search?q=${title.replace(" ", "+")}+${authorOrArtist.replace(" ", "+")}+game"
            } else {
                if (title.isBlank()) title = result.first
                if (authorOrArtist.isBlank()) authorOrArtist = type // Vul type in als artiest leeg is
                else authorOrArtist = result.second
            }
            gameNotFound = false
        } else {
            gameNotFound = true
        }
        isLoading = false
    }

    LaunchedEffect(code, type) {
        val cleanCode = code.trim().replace(" ", "")
        if (cleanCode.length in 12..13 && (title.isBlank() || authorOrArtist.isBlank())) {
            if (type == "boek") {
                tryFetchAndFill(code)
            } else if (type == "muziek") {
                tryFetchAndFillDiscogs(code)
            } else if (type == "dvd") {
                tryFetchAndFillDvd(code)
            } else if (type == "game") {
                tryFetchAndFillGame(code)
            }
        }
    }

    val isOkEnabled = (title.isNotBlank() && authorOrArtist.isNotBlank()) || code.isNotBlank()

    var authorSuggestions by remember { mutableStateOf(listOf<String>()) }
    LaunchedEffect(authorOrArtist, allAuthors) {
        authorSuggestions = allAuthors.filter { it.isNotBlank() && it.contains(authorOrArtist, ignoreCase = true) && it != authorOrArtist }.take(5)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Nieuw item toevoegen" else "Item bewerken") },
        text = {
            Box {
                Column {
                    if (isLoading) {
                        Text("Boekgegevens ophalen...")
                    }
                    if (duplicateError) {
                        Text("Deze code bestaat al in je collectie!", color = MaterialTheme.colorScheme.error)
                    }
                    // Type weergave als gewone tekst (niet klikbaar)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text("Type:")
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = when (type) {
                                    "boek" -> "Boek"
                                    "muziek" -> "Muziek"
                                    "dvd" -> "DVD"
                                    "game" -> "Game"
                                    else -> type.replaceFirstChar { it.uppercase() }
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        // Google knop (alleen als er nog geen cover is en er een code is)
                        if (coverUrl.isNullOrBlank() && code.isNotBlank()) {
                            OutlinedButton(
                                onClick = {
                                    showCoverFetchDialog = true
                                },
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Google", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Titel") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    OutlinedTextField(
                        value = authorOrArtist,
                        onValueChange = { authorOrArtist = it },
                        label = { Text("Auteur/Artiest") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    if (authorOrArtist.isNotBlank() && authorSuggestions.isNotEmpty()) {
                        Card(Modifier.fillMaxWidth().padding(horizontal = 0.dp)) {
                            Column {
                                authorSuggestions.forEach { suggestion ->
                                    TextButton(
                                        onClick = { authorOrArtist = suggestion },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(suggestion, maxLines = 1)
                                    }
                                }
                            }
                        }
                    }
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("ISBN/EAN code") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    
                        // Cover afbeelding weergave met kleine actie knoppen
                        if (coverUrl != null) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Cover:", modifier = Modifier.padding(end = 8.dp))
                                    AsyncImage(
                                        model = coverUrl,
                                        contentDescription = "Cover van $title - klik om te vergroten",
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { showCoverPreview = true },
                                        contentScale = ContentScale.Crop
                                    )
                                    
                                    // Kleine actie knoppen naast de cover
                                    Column(
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        // Google zoek knop (klein)
                                        if (googleSearchUrl != null) {
                                            IconButton(
                                                onClick = {
                                                    uriHandler.openUri(googleSearchUrl!!)
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Search, 
                                                    contentDescription = "Zoek op Google",
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                        
                                        // Cover preview knop (klein)
                                        IconButton(
                                            onClick = {
                                                showCoverPreview = true
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Search, 
                                                contentDescription = "Bekijk cover",
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    // Medium dropdown in plaats van OutlinedTextField
                    if (type == "muziek") {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text("Medium:")
                            Spacer(Modifier.width(8.dp))
                            var mediumDropdownExpanded by remember { mutableStateOf(false) }
                            Box {
                                Button(onClick = { mediumDropdownExpanded = true }) {
                                    Text(
                                        when (medium?.lowercase()) {
                                            "cd" -> "cd"
                                            "lp" -> "lp"
                                            "anders" -> "anders"
                                            else -> "kies medium"
                                        }
                                    )
                                }
                                DropdownMenu(expanded = mediumDropdownExpanded, onDismissRequest = { mediumDropdownExpanded = false }) {
                                    DropdownMenuItem(text = { Text("cd") }, onClick = {
                                        medium = "cd"
                                        mediumDropdownExpanded = false
                                    })
                                    DropdownMenuItem(text = { Text("lp") }, onClick = {
                                        medium = "lp"
                                        mediumDropdownExpanded = false
                                    })
                                    DropdownMenuItem(text = { Text("anders") }, onClick = {
                                        medium = "anders"
                                        mediumDropdownExpanded = false
                                    })
                                }
                            }
                        }
                    }
                    if (type == "dvd" && dvdNotFound && code.isNotBlank()) {
                        Button(
                            onClick = {
                                val query = code.trim().replace(" ", "")
                                uriHandler.openUri("https://www.google.com/search?q=$query")
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Zoek op Google")
                        }
                    }
                    if (type == "game" && gameNotFound && code.isNotBlank()) {
                        Button(
                            onClick = {
                                val query = code.trim().replace(" ", "")
                                uriHandler.openUri("https://www.google.com/search?q=$query")
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Zoek op Google")
                        }
                    }
                    // Geen debugregel meer tonen
                    // Knop 'Test Discogs' is verwijderd
                    Button(
                        onClick = { showScanner = true },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Scan barcode")
                    }
                    Row(Modifier.padding(top = 8.dp)) {
                        Checkbox(checked = isReadOrListened, onCheckedChange = { isReadOrListened = it })
                        Text("Gelezen/Beluisterd")
                    }
                    Row(Modifier.padding(top = 8.dp)) {
                        Checkbox(checked = inPossession, onCheckedChange = { inPossession = it })
                        Text("In bezit")
                    }
                    // Taal dropdown + vrije tekst bij 'Anders'
                    var languageDropdownExpanded by remember { mutableStateOf(false) }
                    Box {
                        Button(onClick = { languageDropdownExpanded = true }) {
                            Text(
                                when (language) {
                                    "NL" -> "Nederlands"
                                    "EN" -> "Engels"
                                    "ANDERS" -> if (customLanguage.isNotBlank()) customLanguage else "Anders"
                                    else -> language ?: "Kies taal"
                                }
                            )
                        }
                        DropdownMenu(expanded = languageDropdownExpanded, onDismissRequest = { languageDropdownExpanded = false }) {
                            DropdownMenuItem(text = { Text("Nederlands") }, onClick = {
                                language = "NL"
                                customLanguage = ""
                                languageDropdownExpanded = false
                            })
                            DropdownMenuItem(text = { Text("Engels") }, onClick = {
                                language = "EN"
                                customLanguage = ""
                                languageDropdownExpanded = false
                            })
                            DropdownMenuItem(text = { Text("Anders") }, onClick = {
                                language = "ANDERS"
                                languageDropdownExpanded = false
                            })
                        }
                    }
                    if (language == "ANDERS") {
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(
                            value = customLanguage,
                            onValueChange = { customLanguage = it },
                            label = { Text("Voer taal in") },
                            modifier = Modifier.width(120.dp)
                        )
                    }
                }
                
                
                if (showScanner) {
                    BarcodeScannerScreen(
                        onBarcodeScanned = {
                            code = it ?: ""
                            showScanner = false
                        },
                        onCancel = { showScanner = false }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    scope.launch {
                        if (isOkEnabled) {
                            val exists = db.itemDao().getByCode(code)
                            if (item == null && exists != null && code.isNotBlank()) {
                                duplicateError = true
                            } else {
                                duplicateError = false
                                onAdd(
                                    Item(
                                        id = item?.id ?: 0,
                                        type = type,
                                        title = title,
                                        authorOrArtist = authorOrArtist,
                                        code = code,
                                        isReadOrListened = isReadOrListened,
                                        inPossession = inPossession,
                                        medium = medium,
                                        language = if (language == "ANDERS") customLanguage else language,
                                        coverUrl = coverUrl,
                                        googleSearchUrl = googleSearchUrl
                                    )
                                )
                                onDismiss()
                            }
                        }
                    }
                },
                enabled = isOkEnabled
            ) { Text("OK") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Annuleren") }
            if (item != null && onDelete != null) {
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = { showDeleteConfirm = true }) { Text("Verwijderen") }
            }
        }
    )
    if (showDeleteConfirm && item != null && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Weet je zeker dat je dit item wilt verwijderen?") },
            confirmButton = {
                Button(onClick = {
                    onDelete(item)
                    showDeleteConfirm = false
                    onDismiss()
                }) { Text("Ja, verwijderen") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) { Text("Annuleren") }
            }
        )
    }
    
    // Cover preview modal
    if (showCoverPreview && coverUrl != null) {
        CoverPreviewModal(
            coverUrl = coverUrl!!,
            title = title,
            authorOrArtist = authorOrArtist,
            onDismiss = { showCoverPreview = false }
        )
    }
    
    // Cover fetch dialog
    if (showCoverFetchDialog) {
        CoverFetchDialog(
            type = type,
            code = code,
            title = title,
            authorOrArtist = authorOrArtist,
            onCoverFetched = { fetchedCoverUrl, fetchedGoogleUrl ->
                coverUrl = fetchedCoverUrl
                googleSearchUrl = fetchedGoogleUrl
            },
            onDismiss = { showCoverFetchDialog = false }
        )
    }
}
