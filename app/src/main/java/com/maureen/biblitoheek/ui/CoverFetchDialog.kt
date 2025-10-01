package com.maureen.biblitoheek.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.platform.LocalUriHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@Composable
fun CoverFetchDialog(
    type: String,
    code: String,
    title: String,
    authorOrArtist: String,
    onCoverFetched: (String?, String?) -> Unit,
    onDismiss: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Titel en sluitknop
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Zoek op Google Afbeeldingen",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Sluit")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Item informatie
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = authorOrArtist,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Type: $type | Code: $code",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Foutmelding
                if (errorMessage != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text(
                            text = errorMessage!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Google Afbeeldingen knop
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        scope.launch {
                            try {
                                when (type) {
                                    "boek" -> {
                                        // Gebruik Google Afbeeldingen in plaats van Google Books API
                                        val googleImagesUrl = "https://www.google.com/search?q=${title.replace(" ", "+")}+${authorOrArtist.replace(" ", "+")}+book+cover&tbm=isch"
                                        uriHandler.openUri(googleImagesUrl)
                                        onCoverFetched(null, googleImagesUrl) // Geen cover URL, maar wel Google Afbeeldingen link
                                        onDismiss()
                                    }
                                    "muziek" -> {
                                        // Gebruik Google Afbeeldingen voor muziek covers
                                        val googleImagesUrl = "https://www.google.com/search?q=${title.replace(" ", "+")}+${authorOrArtist.replace(" ", "+")}+album+cover&tbm=isch"
                                        uriHandler.openUri(googleImagesUrl)
                                        onCoverFetched(null, googleImagesUrl) // Geen cover URL, maar wel Google Afbeeldingen link
                                        onDismiss()
                                    }
                                    "dvd" -> {
                                        val googleImagesUrl = "https://www.google.com/search?q=${title.replace(" ", "+")}+${authorOrArtist.replace(" ", "+")}+dvd+cover&tbm=isch"
                                        uriHandler.openUri(googleImagesUrl)
                                        onCoverFetched(null, googleImagesUrl)
                                        onDismiss()
                                    }
                                    "game" -> {
                                        val googleImagesUrl = "https://www.google.com/search?q=${title.replace(" ", "+")}+${authorOrArtist.replace(" ", "+")}+game+cover&tbm=isch"
                                        uriHandler.openUri(googleImagesUrl)
                                        onCoverFetched(null, googleImagesUrl)
                                        onDismiss()
                                    }
                                }
                            } catch (e: Exception) {
                                errorMessage = "Fout bij ophalen: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Bezig met ophalen...")
                    } else {
                        Text("Zoek op Google Afbeeldingen")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Annuleren knop
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Annuleren")
                }
            }
        }
    }
}

// Functie om boek cover op te halen
suspend fun fetchBookCover(isbn: String, title: String = "", author: String = ""): String? {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            
            // Probeer eerst met ISBN
            var url = "https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn"
            var request = Request.Builder().url(url).build()
            var response = client.newCall(request).execute()
            var body = response.body?.string() ?: ""
            var json = JSONObject(body)
            var items = json.optJSONArray("items")
            
            // Debug: log de response
            println("Google Books API response for ISBN $isbn: $body")
            
            // Als ISBN niet werkt, probeer met titel en auteur
            if ((items == null || items.length() == 0) && title.isNotEmpty() && author.isNotEmpty()) {
                val searchQuery = "$title $author".replace(" ", "+")
                url = "https://www.googleapis.com/books/v1/volumes?q=$searchQuery"
                request = Request.Builder().url(url).build()
                response = client.newCall(request).execute()
                body = response.body?.string() ?: ""
                json = JSONObject(body)
                items = json.optJSONArray("items")
                println("Google Books API response for title+author: $body")
            }
            
            // Als nog steeds geen resultaten, probeer alleen met titel
            if ((items == null || items.length() == 0) && title.isNotEmpty()) {
                val searchQuery = title.replace(" ", "+")
                url = "https://www.googleapis.com/books/v1/volumes?q=$searchQuery"
                request = Request.Builder().url(url).build()
                response = client.newCall(request).execute()
                body = response.body?.string() ?: ""
                json = JSONObject(body)
                items = json.optJSONArray("items")
                println("Google Books API response for title only: $body")
            }
            
            if (items == null || items.length() == 0) {
                return@withContext null
            }
            
            // Zoek de beste cover
            for (i in 0 until items.length()) {
                val volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo")
                val imageLinks = volumeInfo.optJSONObject("imageLinks")
                
                // Probeer verschillende cover groottes
                val coverUrl = imageLinks?.optString("large") 
                    ?: imageLinks?.optString("medium")
                    ?: imageLinks?.optString("small")
                    ?: imageLinks?.optString("thumbnail")
                
                if (coverUrl != null && coverUrl.isNotEmpty()) {
                    return@withContext coverUrl.replace("http://", "https://")
                }
            }
            
            null
        } catch (e: Exception) {
            null
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
                .addHeader("User-Agent", "BiblitoheekApp/1.0 (maureen@email.com)")
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
                    .addHeader("User-Agent", "BiblitoheekApp/1.0 (maureen@email.com)")
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
