package com.seniorease.library.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seniorease.library.data.Item
import com.seniorease.library.ui.BarcodeScannerScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.seniorease.library.data.AppDatabase
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.res.stringResource
import com.seniorease.library.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.CircularProgressIndicator

// Data class voor boek zoekresultaten
data class BookSearchResult(
    val title: String,
    val author: String,
    val isbn: String,
    val coverUrl: String?
)

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
    var tbr by remember { mutableStateOf(item?.tbr ?: false) }
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
    var lastScannedCode by remember { mutableStateOf<String?>(null) }
    var bookSearchResults by remember { mutableStateOf<List<BookSearchResult>>(emptyList()) }
    var showBookSearchResults by remember { mutableStateOf(false) }
    // Geen debugregel meer nodig

    suspend fun fetchBookInfo(isbn: String): Triple<String, String, String?>? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val url = "https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn&key=${com.seniorease.library.BuildConfig.BOOKS_API_KEY}"
                android.util.Log.d("BooksAPI", "Fetching: $url")
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                android.util.Log.d("BooksAPI", "HTTP ${response.code}")
                val body = response.body?.string() ?: return@withContext null
                android.util.Log.d("BooksAPI", "Response: $body")
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
                android.util.Log.e("BooksAPI", "Exception: ${e.message}", e)
                null
            }
        }
    }
    
    // Zoek boek via titel of auteur (niet alleen ISBN) - retourneert lijst van resultaten
    suspend fun searchBooksByTitleOrAuthor(searchQuery: String): List<BookSearchResult> {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                // Zoek op titel of auteur - max 10 resultaten
                val encodedQuery = java.net.URLEncoder.encode(searchQuery, "UTF-8")
                val url = "https://www.googleapis.com/books/v1/volumes?q=$encodedQuery&maxResults=10"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: return@withContext emptyList()
                val json = JSONObject(body)
                val items = json.optJSONArray("items") ?: return@withContext emptyList()
                
                val results = mutableListOf<BookSearchResult>()
                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val volumeInfo = item.getJSONObject("volumeInfo")
                    val title = volumeInfo.optString("title", "")
                    val authors = volumeInfo.optJSONArray("authors")
                    val author = if (authors != null && authors.length() > 0) authors.getString(0) else ""
                    
                    // Haal ISBN op (probeer eerst ISBN_13, dan ISBN_10)
                    var isbn = ""
                    val industryIdentifiers = volumeInfo.optJSONArray("industryIdentifiers")
                    if (industryIdentifiers != null) {
                        for (j in 0 until industryIdentifiers.length()) {
                            val identifier = industryIdentifiers.getJSONObject(j)
                            val type = identifier.optString("type", "")
                            val identifierValue = identifier.optString("identifier", "")
                            if (type == "ISBN_13") {
                                isbn = identifierValue
                                break
                            } else if (type == "ISBN_10" && isbn.isEmpty()) {
                                isbn = identifierValue
                            }
                        }
                    }
                    
                    // Haal cover URL op
                    val imageLinks = volumeInfo.optJSONObject("imageLinks")
                    val coverUrl = imageLinks?.optString("thumbnail")?.replace("http://", "https://")
                    
                    if (title.isNotBlank()) {
                        results.add(BookSearchResult(title, author, isbn, coverUrl))
                    }
                }
                results
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun fetchBookInfoOpenLibrary(isbn: String): Triple<String, String, String?>? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val url = "https://openlibrary.org/api/books?bibkeys=ISBN:$isbn&format=json&jscmd=data"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: return@withContext null
                android.util.Log.d("BooksAPI", "OpenLibrary response: $body")
                val json = JSONObject(body)
                val key = "ISBN:$isbn"
                val bookObj = json.optJSONObject(key) ?: return@withContext null
                val title = bookObj.optString("title", "")
                val authorsArr = bookObj.optJSONArray("authors")
                val author = if (authorsArr != null && authorsArr.length() > 0)
                    authorsArr.getJSONObject(0).optString("name", "") else ""
                val coverObj = bookObj.optJSONObject("cover")
                val coverUrl = coverObj?.optString("medium")?.ifBlank { null }
                    ?: coverObj?.optString("large")?.ifBlank { null }
                if (title.isBlank()) return@withContext null
                Triple(title, author, coverUrl)
            } catch (e: Exception) {
                android.util.Log.e("BooksAPI", "OpenLibrary exception: ${e.message}", e)
                null
            }
        }
    }

    suspend fun tryFetchAndFill(isbn: String, forceUpdate: Boolean = false) {
        isLoading = true
        val result = fetchBookInfo(isbn) ?: fetchBookInfoOpenLibrary(isbn)
        if (result != null) {
            if (forceUpdate || title.isBlank()) {
                title = result.first
            }
            if (forceUpdate || authorOrArtist.isBlank()) {
                authorOrArtist = if (result.second.isNotBlank()) result.second else type
            }
            if (forceUpdate || coverUrl.isNullOrBlank()) {
                coverUrl = result.third
            }
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
                val url = "https://api.discogs.com/database/search?barcode=$barcode&type=release&token=${com.seniorease.library.BuildConfig.DISCOGS_TOKEN}"
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "BiblitoheekApp/1.0")
                    .build()
                val response = client.newCall(request).execute()
                
                // Check response code
                if (!response.isSuccessful) {
                    android.util.Log.e("DiscogsAPI", "HTTP error: ${response.code} - ${response.message}")
                    return@withContext Triple(null, "HTTP ${response.code}", null)
                }
                
                val body = response.body?.string() ?: return@withContext Triple(null, "Empty response", null)
                android.util.Log.d("DiscogsAPI", "Response body: $body")
                
                val json = JSONObject(body)
                
                // Check for error messages
                if (json.has("message")) {
                    val errorMsg = json.optString("message", "")
                    android.util.Log.e("DiscogsAPI", "Discogs error: $errorMsg")
                }
                
                val results = json.optJSONArray("results")
                if (results == null || results.length() == 0) {
                    android.util.Log.d("DiscogsAPI", "No results found for barcode: $barcode")
                    return@withContext Triple(null, body, null)
                }
                
                val release = results.optJSONObject(0) ?: return@withContext Triple(null, body, null)
                val title = release.optString("title", "")
                
                // Try different ways to get artist
                var artist = ""
                if (release.has("artist")) {
                    val artistValue = release.get("artist")
                    if (artistValue is org.json.JSONArray) {
                        artist = artistValue.optString(0, "")
                    } else {
                        artist = release.optString("artist", "")
                    }
                }
                
                android.util.Log.d("DiscogsAPI", "Found: title='$title', artist='$artist'")
                
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
                android.util.Log.e("DiscogsAPI", "Exception: ${e.message}", e)
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
                var url = "https://api.discogs.com/database/search?barcode=$ean&type=release&token=${com.seniorease.library.BuildConfig.DISCOGS_TOKEN}"
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
                    url = "https://api.discogs.com/database/search?q=$searchQuery&type=release&token=${com.seniorease.library.BuildConfig.DISCOGS_TOKEN}"
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

    suspend fun tryFetchAndFillDiscogs(barcode: String, forceUpdate: Boolean = false) {
        android.util.Log.d("DiscogsAPI", "tryFetchAndFillDiscogs called with barcode: $barcode, forceUpdate: $forceUpdate")
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
        android.util.Log.d("DiscogsAPI", "Trying codes: $codesToTry")
        var result: Pair<String, String>? = null
        var lastJson: String? = null
        var foundMedium: String? = null
        var discogsSuccess = false
        
        // Probeer eerst Discogs
        for (codeTry in codesToTry) {
            val fetchResult = fetchDiscogsInfoWithJson(codeTry)
            lastJson = fetchResult.second
            if (fetchResult.first != null) {
                result = fetchResult.first
                foundMedium = fetchResult.third
                discogsSuccess = true
                android.util.Log.d("DiscogsAPI", "Found result from Discogs: $result")
                break
            }
        }
        
        // Als Discogs niet werkt, probeer UPCitemdb als fallback
        if (!discogsSuccess) {
            android.util.Log.d("DiscogsAPI", "Discogs failed, trying UPCitemdb fallback")
            val upcResult = fetchGameInfoUpcitemdb(cleanBarcode)
            if (upcResult != null && upcResult.first.isNotBlank()) {
                android.util.Log.d("DiscogsAPI", "Found result from UPCitemdb: $upcResult")
                // Parse de titel om te proberen artiest en titel te scheiden
                val titleStr = upcResult.first
                var discogsTitle = titleStr
                var discogsArtist = upcResult.second
                
                // Probeer te splitsen op common patterns
                if (titleStr.contains(" - ")) {
                    val parts = titleStr.split(" - ", limit = 2)
                    if (parts.size == 2 && parts[0].isNotBlank() && parts[1].isNotBlank()) {
                        discogsArtist = parts[0].trim()
                        discogsTitle = parts[1].trim()
                    }
                } else if (titleStr.contains(" / ")) {
                    val parts = titleStr.split(" / ", limit = 2)
                    if (parts.size == 2 && parts[0].isNotBlank() && parts[1].isNotBlank()) {
                        discogsArtist = parts[0].trim()
                        discogsTitle = parts[1].trim()
                    }
                } else if (titleStr.contains(" by ")) {
                    val parts = titleStr.split(" by ", limit = 2)
                    if (parts.size == 2 && parts[0].isNotBlank() && parts[1].isNotBlank()) {
                        discogsTitle = parts[0].trim()
                        discogsArtist = parts[1].trim()
                    }
                }
                
                result = Pair(discogsTitle, discogsArtist)
                android.util.Log.d("DiscogsAPI", "Processed UPCitemdb result: title='$discogsTitle', artist='$discogsArtist'")
            }
        }
        
        if (result != null) {
            var discogsTitle = result.first
            var discogsArtist = result.second
            android.util.Log.d("DiscogsAPI", "Processing result: title='$discogsTitle', artist='$discogsArtist'")
            
            // Als artiest nog leeg is, probeer te splitsen op ' - '
            if ((forceUpdate || authorOrArtist.isBlank()) && discogsTitle.contains(" - ")) {
                val parts = discogsTitle.split(" - ", limit = 2)
                if (parts.size == 2) {
                    discogsArtist = parts[0]
                    discogsTitle = parts[1]
                    android.util.Log.d("DiscogsAPI", "Split title: '$discogsTitle' - '$discogsArtist'")
                }
            }
            if (forceUpdate || title.isBlank()) {
                title = discogsTitle
                android.util.Log.d("DiscogsAPI", "Updated title to: '$title'")
            }
            if (forceUpdate || authorOrArtist.isBlank()) {
                authorOrArtist = if (discogsArtist.isNotBlank()) discogsArtist else type
                android.util.Log.d("DiscogsAPI", "Updated authorOrArtist to: '$authorOrArtist'")
            }
            // Vul medium in als gevonden
            if (foundMedium != null && (forceUpdate || medium.isNullOrBlank())) {
                medium = foundMedium
                android.util.Log.d("DiscogsAPI", "Updated medium to: '$medium'")
            }
            
            // Haal cover URL op alleen als Discogs succesvol was
            if (discogsSuccess) {
                val fetchedCoverUrl = fetchMusicCover(barcode, discogsTitle, discogsArtist)
                if (fetchedCoverUrl != null && (forceUpdate || coverUrl.isNullOrBlank())) {
                    coverUrl = fetchedCoverUrl
                    android.util.Log.d("DiscogsAPI", "Updated coverUrl to: '$coverUrl'")
                }
            }
            
            // Genereer Google zoek URL voor muziek
            googleSearchUrl = "https://www.google.com/search?q=${title.replace(" ", "+")}+${authorOrArtist.replace(" ", "+")}+music"
        } else {
            android.util.Log.w("DiscogsAPI", "No result found for barcode: $barcode, lastJson: $lastJson")
        }
        // fullJson niet meer nodig
        isLoading = false
        android.util.Log.d("DiscogsAPI", "tryFetchAndFillDiscogs completed. Final title: '$title', authorOrArtist: '$authorOrArtist'")
    }

    suspend fun fetchDvdInfo(barcode: String): Pair<String, String>? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val url = "https://www.omdbapi.com/?apikey=${com.seniorease.library.BuildConfig.OMDB_API_KEY}&i=tt$barcode"
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

    suspend fun tryFetchAndFillDvd(barcode: String, forceUpdate: Boolean = false) {
        isLoading = true
        val result = fetchDvdInfoUpcitemdb(barcode)
        if (result != null) {
            if (forceUpdate || title.isBlank()) title = result.first
            if (forceUpdate || authorOrArtist.isBlank()) {
                authorOrArtist = if (result.second.isNotBlank()) result.second else type
            }
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

    suspend fun tryFetchAndFillGame(barcode: String, forceUpdate: Boolean = false) {
        isLoading = true
        val result = fetchGameInfoUpcitemdb(barcode)
        if (result != null) {
            if (type == "game") {
                val (rawTitle, platform) = extractPlatformFromTitle(result.first)
                val cleanTitle = cleanGameTitle(rawTitle)
                if (forceUpdate || title.isBlank()) title = cleanTitle
                if (forceUpdate || authorOrArtist.isBlank()) {
                    authorOrArtist = if (platform.isNotBlank()) platform else result.second
                    if (authorOrArtist.isBlank()) authorOrArtist = type
                }
                
                // Genereer Google zoek URL voor game
                googleSearchUrl = "https://www.google.com/search?q=${title.replace(" ", "+")}+${authorOrArtist.replace(" ", "+")}+game"
            } else {
                if (forceUpdate || title.isBlank()) title = result.first
                if (forceUpdate || authorOrArtist.isBlank()) {
                    authorOrArtist = if (result.second.isNotBlank()) result.second else type
                }
            }
            gameNotFound = false
        } else {
            gameNotFound = true
        }
        isLoading = false
    }

    // LaunchedEffect om automatisch informatie op te halen wanneer code wordt gescand
    LaunchedEffect(lastScannedCode) {
        lastScannedCode?.let { scannedCode ->
            android.util.Log.d("BarcodeScan", "LaunchedEffect triggered for scanned code: $scannedCode")
            val cleanCode = scannedCode.trim().replace(" ", "")
            android.util.Log.d("BarcodeScan", "Clean code: $cleanCode, type: $type")
            if (cleanCode.length in 12..13) {
                android.util.Log.d("BarcodeScan", "Code length valid, fetching info for type: $type")
                // Bij een scan altijd informatie ophalen, ook als velden al gevuld zijn
                when (type) {
                    "boek" -> tryFetchAndFill(cleanCode, forceUpdate = true)
                    "muziek" -> tryFetchAndFillDiscogs(cleanCode, forceUpdate = true)
                    "dvd" -> tryFetchAndFillDvd(cleanCode, forceUpdate = true)
                    "game" -> tryFetchAndFillGame(cleanCode, forceUpdate = true)
                    else -> android.util.Log.w("BarcodeScan", "Unknown type: $type")
                }
            } else {
                android.util.Log.w("BarcodeScan", "Code length invalid: ${cleanCode.length}")
            }
            lastScannedCode = null // Reset na gebruik
        }
    }

    // LaunchedEffect om automatisch informatie op te halen wanneer code handmatig wordt ingevoerd
    LaunchedEffect(code, type) {
        val cleanCode = code.trim().replace(" ", "")
        // Haal informatie op als code geldig is en velden leeg zijn (of bij nieuw item)
        // Alleen als dit niet via een scan komt (lastScannedCode is null)
        if (cleanCode.length in 12..13 && 
            lastScannedCode == null && 
            (item == null || title.isBlank() || authorOrArtist.isBlank())) {
            when (type) {
                "boek" -> tryFetchAndFill(cleanCode)
                "muziek" -> tryFetchAndFillDiscogs(cleanCode)
                "dvd" -> tryFetchAndFillDvd(cleanCode)
                "game" -> tryFetchAndFillGame(cleanCode)
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
        title = { Text(if (item == null) stringResource(R.string.add_item) else stringResource(R.string.edit_item)) },
        text = {
            Box {
                Column {
                    if (isLoading) {
                        Text(stringResource(R.string.fetching_book_data))
                    }
                    if (duplicateError) {
                        Text(stringResource(R.string.duplicate_code), color = MaterialTheme.colorScheme.error)
                    }
                    // Type dropdown menu
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                Text(stringResource(R.string.item_type))
                                Spacer(Modifier.width(8.dp))
                                var typeDropdownExpanded by remember { mutableStateOf(false) }
                                Box {
                                    OutlinedButton(
                                        onClick = { typeDropdownExpanded = true },
                                        modifier = Modifier.height(48.dp)
                                    ) {
                                        Text(
                                            text = when (type) {
                                                "boek" -> stringResource(R.string.item_type_book)
                                                "muziek" -> stringResource(R.string.item_type_music)
                                                "dvd" -> stringResource(R.string.item_type_dvd)
                                                "game" -> stringResource(R.string.item_type_game)
                                                else -> type.replaceFirstChar { it.uppercase() }
                                            },
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = typeDropdownExpanded,
                                        onDismissRequest = { typeDropdownExpanded = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.item_type_book)) },
                                            onClick = {
                                                type = "boek"
                                                typeDropdownExpanded = false
                                                onTypeChange?.invoke("boek")
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.item_type_music)) },
                                            onClick = {
                                                type = "muziek"
                                                typeDropdownExpanded = false
                                                onTypeChange?.invoke("muziek")
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.item_type_dvd)) },
                                            onClick = {
                                                type = "dvd"
                                                typeDropdownExpanded = false
                                                onTypeChange?.invoke("dvd")
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.item_type_game)) },
                                            onClick = {
                                                type = "game"
                                                typeDropdownExpanded = false
                                                onTypeChange?.invoke("game")
                                            }
                                        )
                                    }
                                }
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
                                    Text(stringResource(R.string.google), style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    // Titel veld met zoek knop
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text(stringResource(R.string.item_title)) },
                            modifier = Modifier.weight(1f),
                            trailingIcon = {
                                if (title.isNotBlank()) {
                                    IconButton(onClick = { title = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = stringResource(R.string.clear_title)
                                        )
                                    }
                                }
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (title.isNotBlank() || authorOrArtist.isNotBlank()) {
                                    // Zoek boek via Google Books API
                                    // Combinatie van titel + auteur heeft voorrang
                                    scope.launch {
                                        isLoading = true
                                        val searchQuery = when {
                                            title.isNotBlank() && authorOrArtist.isNotBlank() -> {
                                                // Combinatie: titel + auteur heeft voorrang
                                                "$title $authorOrArtist"
                                            }
                                            title.isNotBlank() -> title
                                            else -> authorOrArtist
                                        }
                                        val results = searchBooksByTitleOrAuthor(searchQuery)
                                        isLoading = false
                                        if (results.isNotEmpty()) {
                                            bookSearchResults = results
                                            showBookSearchResults = true
                                        } else {
                                            // Geen resultaat gevonden
                                            android.widget.Toast.makeText(context, context.getString(R.string.book_not_found), android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            enabled = (title.isNotBlank() || authorOrArtist.isNotBlank()) && !isLoading,
                            modifier = Modifier.height(56.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            } else {
                                Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
                            }
                        }
                    }
                    
                    // Boek zoekresultaten lijst
                    if (showBookSearchResults && bookSearchResults.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.search_results, bookSearchResults.size),
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.weight(1f)
                                    )
                                    TextButton(
                                        onClick = { 
                                            showBookSearchResults = false
                                            bookSearchResults = emptyList()
                                        }
                                    ) {
                                        Text(stringResource(R.string.close))
                                    }
                                }
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 300.dp)
                                ) {
                                    items(bookSearchResults) { result ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clickable {
                                                    // Vul alle velden in bij selectie
                                                    title = result.title
                                                    authorOrArtist = result.author
                                                    code = result.isbn
                                                    coverUrl = result.coverUrl
                                                    googleSearchUrl = "https://www.google.com/search?q=${result.title.replace(" ", "+")}+${result.author.replace(" ", "+")}"
                                                    showBookSearchResults = false
                                                    bookSearchResults = emptyList()
                                                },
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surface
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                // Cover thumbnail (klein)
                                                if (result.coverUrl != null) {
                                                    AsyncImage(
                                                        model = result.coverUrl,
                                                        contentDescription = "Cover",
                                                        modifier = Modifier
                                                            .size(50.dp)
                                                            .clip(RoundedCornerShape(4.dp))
                                                            .padding(end = 12.dp),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                }
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = result.title,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        maxLines = 2
                                                    )
                                                    if (result.author.isNotBlank()) {
                                                        Text(
                                                            text = result.author,
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                            maxLines = 1
                                                        )
                                                    }
                                                    if (result.isbn.isNotBlank()) {
                                                        Text(
                                                            text = "ISBN: ${result.isbn}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.primary
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // Auteur veld
                    OutlinedTextField(
                        value = authorOrArtist,
                        onValueChange = { authorOrArtist = it },
                        label = { Text(stringResource(R.string.item_author)) },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        trailingIcon = {
                            if (authorOrArtist.isNotBlank()) {
                                IconButton(onClick = { authorOrArtist = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                            contentDescription = stringResource(R.string.clear_author)
                                    )
                                }
                            }
                        }
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
                            label = { Text(stringResource(R.string.item_code)) },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            if (code.isNotBlank()) {
                                IconButton(onClick = { code = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                            contentDescription = stringResource(R.string.clear_code)
                                    )
                                }
                            }
                        }
                    )
                    
                        // Cover afbeelding niet weergeven
                    // Medium dropdown
                    if (type == "muziek") {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text(stringResource(R.string.item_medium))
                            Spacer(Modifier.width(8.dp))
                            var mediumDropdownExpanded by remember { mutableStateOf(false) }
                            Box {
                                Button(onClick = { mediumDropdownExpanded = true }) {
                                    Text(
                                        when (medium?.lowercase()) {
                                            "cd" -> stringResource(R.string.item_medium_cd)
                                            "lp" -> stringResource(R.string.item_medium_lp)
                                            "anders" -> stringResource(R.string.item_medium_other)
                                            else -> stringResource(R.string.item_medium_choose)
                                        }
                                    )
                                }
                                DropdownMenu(expanded = mediumDropdownExpanded, onDismissRequest = { mediumDropdownExpanded = false }) {
                                    DropdownMenuItem(text = { Text(stringResource(R.string.item_medium_cd)) }, onClick = {
                                        medium = "cd"
                                        mediumDropdownExpanded = false
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.item_medium_lp)) }, onClick = {
                                        medium = "lp"
                                        mediumDropdownExpanded = false
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.item_medium_other)) }, onClick = {
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
                            Text(stringResource(R.string.search_google))
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
                            Text(stringResource(R.string.search_google))
                        }
                    }
                    // Geen debugregel meer tonen
                    // Knop 'Test Discogs' is verwijderd
                    Button(
                        onClick = { showScanner = true },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(stringResource(R.string.scan_barcode))
                    }
                    // Checkboxes - netjes georganiseerd
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isReadOrListened,
                                onCheckedChange = { isReadOrListened = it },
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = stringResource(R.string.item_read_listened),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = inPossession,
                                onCheckedChange = { inPossession = it },
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = when (type) {
                                    "boek" -> stringResource(R.string.item_owned_book)
                                    "muziek" -> stringResource(R.string.item_owned_music)
                                    "game" -> stringResource(R.string.item_owned_game)
                                    "dvd" -> stringResource(R.string.item_owned_dvd)
                                    else -> stringResource(R.string.item_in_possession)
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 8.dp, end = 24.dp)
                            )
                            Checkbox(
                                checked = tbr,
                                onCheckedChange = { tbr = it },
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = stringResource(R.string.item_tbr),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    // Taal dropdown + vrije tekst bij 'Anders'
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.item_language),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            var languageDropdownExpanded by remember { mutableStateOf(false) }
                            Box {
                                OutlinedButton(
                                    onClick = { languageDropdownExpanded = true },
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Text(
                                        text = when (language) {
                                            "NL" -> stringResource(R.string.item_language_nl)
                                            "EN" -> stringResource(R.string.item_language_en)
                                            "ANDERS" -> if (customLanguage.isNotBlank()) customLanguage else stringResource(R.string.item_language_other)
                                            else -> language ?: stringResource(R.string.item_language_choose)
                                        },
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                DropdownMenu(expanded = languageDropdownExpanded, onDismissRequest = { languageDropdownExpanded = false }) {
                                    DropdownMenuItem(text = { Text(stringResource(R.string.item_language_nl)) }, onClick = {
                                        language = "NL"
                                        customLanguage = ""
                                        languageDropdownExpanded = false
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.item_language_en)) }, onClick = {
                                        language = "EN"
                                        customLanguage = ""
                                        languageDropdownExpanded = false
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.item_language_other)) }, onClick = {
                                        language = "ANDERS"
                                        languageDropdownExpanded = false
                                    })
                                }
                            }
                        }
                        if (language == "ANDERS") {
                            OutlinedTextField(
                                value = customLanguage,
                                onValueChange = { customLanguage = it },
                                label = { Text(stringResource(R.string.item_language_custom)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                    
                    if (showScanner) {
                        BarcodeScannerScreen(
                            onBarcodeScanned = { scannedCode ->
                                android.util.Log.d("BarcodeScan", "Barcode scanned: $scannedCode")
                                val scanned = scannedCode ?: ""
                                android.util.Log.d("BarcodeScan", "Setting code to: $scanned, type: $type")
                                code = scanned
                                lastScannedCode = scanned // Dit triggert de LaunchedEffect om informatie op te halen
                                android.util.Log.d("BarcodeScan", "lastScannedCode set to: $lastScannedCode")
                                showScanner = false
                            },
                            onCancel = { showScanner = false }
                        )
                    }
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
                                        tbr = tbr,
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
            ) { Text(stringResource(R.string.ok)) }
        },
        dismissButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item != null && onDelete != null) {
                    OutlinedButton(onClick = { showDeleteConfirm = true }) { 
                        Text(stringResource(R.string.delete)) 
                    }
                }
                OutlinedButton(onClick = onDismiss) { 
                    Text(stringResource(R.string.cancel)) 
                }
            }
        }
    )
    if (showDeleteConfirm && item != null && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(stringResource(R.string.delete_confirm_title)) },
            confirmButton = {
                Button(onClick = {
                    onDelete(item)
                    showDeleteConfirm = false
                    onDismiss()
                }) { Text(stringResource(R.string.delete_confirm_yes)) }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
    
    // Cover preview modal niet weergeven
    
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
