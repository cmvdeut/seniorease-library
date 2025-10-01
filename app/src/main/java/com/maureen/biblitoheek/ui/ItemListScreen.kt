package com.maureen.biblitoheek.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalUriHandler
import coil.compose.AsyncImage
import java.util.Locale
import androidx.compose.ui.unit.dp
import com.maureen.biblitoheek.data.Item
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.maureen.biblitoheek.data.AppDatabase
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

fun stripVoorzetsel(titel: String): String {
    val voorzetsels = listOf("de ", "het ", "een ", "the ", "a ", "an ")
    val lower = titel.lowercase(Locale.getDefault())
    return voorzetsels.firstOrNull { lower.startsWith(it) }?.let { titel.drop(it.length) } ?: titel
}

fun extractAchternaam(naam: String): String {
    val delen = naam.trim().split(" ")
    return if (delen.size > 1) delen.last() else naam
}

@Composable
fun ItemListScreen(
    items: List<Item>,
    onAddClick: (String) -> Unit,
    onUpdateItem: (Item) -> Unit,
    onItemClick: (Item) -> Unit
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    var sortOption by remember { mutableStateOf("Alles") }
    var filterType by remember { mutableStateOf<String?>(null) }
    var filterRead by remember { mutableStateOf<Boolean?>(null) }
    var filterPossession by remember { mutableStateOf<Boolean?>(null) }
    var sortMenuExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showCoverPreview by remember { mutableStateOf(false) }
    var selectedCoverItem by remember { mutableStateOf<Item?>(null) }

    val sortedFilteredItems = items
        .filter { filterType == null || it.type == filterType }
        .filter { filterRead == null || it.isReadOrListened == filterRead }
        .filter { filterPossession == null || it.inPossession == filterPossession }
        .filter {
            val q = searchQuery.trim().lowercase(Locale.getDefault())
            q.isBlank() ||
                it.title.lowercase(Locale.getDefault()).contains(q) ||
                it.authorOrArtist.lowercase(Locale.getDefault()).contains(q) ||
                it.code.lowercase(Locale.getDefault()).contains(q)
        }
        .sortedWith(
            when (sortOption) {
                "Alles" -> compareBy { it.id } // Originele volgorde
                "Auteur" -> compareBy({ extractAchternaam(it.authorOrArtist).lowercase(Locale.getDefault()) }, { stripVoorzetsel(it.title).lowercase(Locale.getDefault()) })
                "Titel" -> compareBy { stripVoorzetsel(it.title).lowercase(Locale.getDefault()) }
                "Gelezen" -> compareByDescending<Item> { it.isReadOrListened }
                "In bezit" -> compareByDescending<Item> { it.inPossession }
                else -> compareBy { stripVoorzetsel(it.title).lowercase(Locale.getDefault()) }
            }
        )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClick("boek") },
                modifier = Modifier.size(72.dp), // Groter voor senioren
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineLarge // Groter voor senioren
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Zoeken") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            // Sorteer en filter sectie - verbeterd voor senioren
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Sorteer sectie
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sorteren:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    OutlinedButton(
                        onClick = { sortMenuExpanded = true },
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("$sortOption", style = MaterialTheme.typography.bodyLarge)
                    }
                    DropdownMenu(expanded = sortMenuExpanded, onDismissRequest = { sortMenuExpanded = false }) {
                        DropdownMenuItem(text = { Text("Alles", style = MaterialTheme.typography.bodyLarge) }, onClick = { sortOption = "Alles"; sortMenuExpanded = false })
                        DropdownMenuItem(text = { Text("Titel", style = MaterialTheme.typography.bodyLarge) }, onClick = { sortOption = "Titel"; sortMenuExpanded = false })
                        DropdownMenuItem(text = { Text("Auteur", style = MaterialTheme.typography.bodyLarge) }, onClick = { sortOption = "Auteur"; sortMenuExpanded = false })
                        DropdownMenuItem(text = { Text("Gelezen", style = MaterialTheme.typography.bodyLarge) }, onClick = { sortOption = "Gelezen"; sortMenuExpanded = false })
                        DropdownMenuItem(text = { Text("In bezit", style = MaterialTheme.typography.bodyLarge) }, onClick = { sortOption = "In bezit"; sortMenuExpanded = false })
                    }
                }
                
                // Filter sectie - horizontaal scrollbaar voor kleine schermen
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Filter:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    // Type filters
                    OutlinedButton(
                        onClick = { filterType = if (filterType == "boek") null else "boek" },
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(if (filterType == "boek") "Alle types" else "Boeken", style = MaterialTheme.typography.bodyLarge)
                    }
                    OutlinedButton(
                        onClick = { filterType = if (filterType == "muziek") null else "muziek" },
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(if (filterType == "muziek") "Alle types" else "Muziek", style = MaterialTheme.typography.bodyLarge)
                    }
                    OutlinedButton(
                        onClick = { filterType = if (filterType == "dvd") null else "dvd" },
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(if (filterType == "dvd") "Alle types" else "DVD", style = MaterialTheme.typography.bodyLarge)
                    }
                    OutlinedButton(
                        onClick = { filterType = if (filterType == "game") null else "game" },
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(if (filterType == "game") "Alle types" else "Game", style = MaterialTheme.typography.bodyLarge)
                    }
                    
                    // Status filters
                    OutlinedButton(
                        onClick = { filterRead = if (filterRead == true) null else true },
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(if (filterRead == true) "Alles" else "Gelezen/Beluisterd", style = MaterialTheme.typography.bodyLarge)
                    }
                    OutlinedButton(
                        onClick = { filterPossession = if (filterPossession == true) null else true },
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(if (filterPossession == true) "Alles" else "In bezit", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(sortedFilteredItems) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable { onItemClick(item) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Cover afbeelding (alleen weergave, niet klikbaar)
                            if (item.coverUrl != null) {
                                AsyncImage(
                                    model = item.coverUrl,
                                    contentDescription = "Cover van ${item.title}",
                                    modifier = Modifier
                                        .size(100.dp) // Groter voor senioren
                                        .clip(RoundedCornerShape(8.dp))
                                        .padding(end = 16.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title, 
                                    style = MaterialTheme.typography.titleLarge, // Groter voor senioren
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = item.authorOrArtist, 
                                    style = MaterialTheme.typography.titleMedium, // Groter voor senioren
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "Type: ${item.type}", 
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                                Text(
                                    text = "Code: ${item.code}", 
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                
                                // Checkboxes met grotere tekst
                                Row(
                                    modifier = Modifier.padding(top = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = item.isReadOrListened,
                                        onCheckedChange = { checked ->
                                            onUpdateItem(item.copy(isReadOrListened = checked))
                                        },
                                        modifier = Modifier.size(24.dp) // Groter voor senioren
                                    )
                                    Text(
                                        text = when (item.type) {
                                            "boek" -> "Gelezen"
                                            else -> "Beluisterd"
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(end = 16.dp)
                                    )
                                    Checkbox(
                                        checked = item.inPossession,
                                        onCheckedChange = { checked ->
                                            onUpdateItem(item.copy(inPossession = checked))
                                        },
                                        modifier = Modifier.size(24.dp) // Groter voor senioren
                                    )
                                    Text(
                                        text = "In bezit",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Cover preview modal
    if (showCoverPreview && selectedCoverItem != null) {
        CoverPreviewModal(
            coverUrl = selectedCoverItem!!.coverUrl!!,
            title = selectedCoverItem!!.title,
            authorOrArtist = selectedCoverItem!!.authorOrArtist,
            onDismiss = { 
                showCoverPreview = false
                selectedCoverItem = null
            }
        )
    }
}
