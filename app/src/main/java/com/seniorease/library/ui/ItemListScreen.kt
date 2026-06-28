package com.seniorease.library.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalUriHandler
import coil.compose.AsyncImage
import java.util.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seniorease.library.data.Item
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.seniorease.library.data.AppDatabase
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.seniorease.library.R
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
    onItemClick: (Item) -> Unit,
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    var sortOption by remember { mutableStateOf(context.getString(R.string.sort_title)) }
    var filterRead by remember { mutableStateOf<Boolean?>(null) }
    var filterPossession by remember { mutableStateOf<Boolean?>(null) }
    var filterTbr by remember { mutableStateOf<Boolean?>(null) }
    var filterType by remember { mutableStateOf<String?>(null) }
    var sortMenuExpanded by remember { mutableStateOf(false) }
    var filterMenuExpanded by remember { mutableStateOf(false) }
    var typeMenuExpanded by remember { mutableStateOf(false) }
    
    // Bepaal welke filter actief is voor display
    val activeFilterText = when {
        filterRead == true -> stringResource(R.string.filter_read)
        filterPossession == true -> stringResource(R.string.filter_possession)
        filterTbr == true -> stringResource(R.string.filter_tbr)
        else -> stringResource(R.string.filter_all)
    }
    var searchQuery by remember { mutableStateOf("") }
    var showCoverPreview by remember { mutableStateOf(false) }
    var selectedCoverItem by remember { mutableStateOf<Item?>(null) }
    var expandedItemId by remember { mutableStateOf<Int?>(null) }

    val sortedFilteredItems = items
        .filter { filterType == null || it.type == filterType }
        .filter { filterRead == null || it.isReadOrListened == filterRead }
        .filter { filterPossession == null || it.inPossession == filterPossession }
        .filter { filterTbr == null || it.tbr == filterTbr }
        .filter {
            val q = searchQuery.trim().lowercase(Locale.getDefault())
            q.isBlank() ||
                it.title.lowercase(Locale.getDefault()).contains(q) ||
                it.authorOrArtist.lowercase(Locale.getDefault()).contains(q) ||
                it.code.lowercase(Locale.getDefault()).contains(q)
        }
        .sortedWith(
            when (sortOption) {
                context.getString(R.string.sort_title) -> compareBy { stripVoorzetsel(it.title).lowercase(Locale.getDefault()) }
                context.getString(R.string.sort_author) -> compareBy({ extractAchternaam(it.authorOrArtist).lowercase(Locale.getDefault()) }, { stripVoorzetsel(it.title).lowercase(Locale.getDefault()) })
                context.getString(R.string.sort_recent) -> compareByDescending<Item> { it.id } // Nieuwste eerst (hoogste ID = laatst gescand)
                else -> compareBy { stripVoorzetsel(it.title).lowercase(Locale.getDefault()) } // Standaard alfabetisch op titel
            }
        )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClick("boek") },
                modifier = Modifier.size(72.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineLarge
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
                label = { Text(stringResource(R.string.search)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                            contentDescription = stringResource(R.string.clear_text)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            // Sorteer- en filtercontrols: één rij met 3 gelijke kolommen
            val filterButtonPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
            val filterButtonTextStyle = MaterialTheme.typography.labelMedium
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sort
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.sort),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Box {
                        OutlinedButton(
                            onClick = { sortMenuExpanded = true },
                            modifier = Modifier.height(44.dp).fillMaxWidth(),
                            contentPadding = filterButtonPadding
                        ) {
                            Text(
                                text = sortOption,
                                style = filterButtonTextStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        DropdownMenu(expanded = sortMenuExpanded, onDismissRequest = { sortMenuExpanded = false }) {
                            DropdownMenuItem(text = { Text(stringResource(R.string.sort_title), style = MaterialTheme.typography.bodyLarge) }, onClick = { sortOption = context.getString(R.string.sort_title); sortMenuExpanded = false })
                            DropdownMenuItem(text = { Text(stringResource(R.string.sort_author), style = MaterialTheme.typography.bodyLarge) }, onClick = { sortOption = context.getString(R.string.sort_author); sortMenuExpanded = false })
                            DropdownMenuItem(text = { Text(stringResource(R.string.sort_recent), style = MaterialTheme.typography.bodyLarge) }, onClick = { sortOption = context.getString(R.string.sort_recent); sortMenuExpanded = false })
                        }
                    }
                }
                // Type
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.filter_type),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Box {
                        OutlinedButton(
                            onClick = { typeMenuExpanded = true },
                            modifier = Modifier.height(44.dp).fillMaxWidth(),
                            contentPadding = filterButtonPadding
                        ) {
                            Text(
                                text = when (filterType) {
                                    "boek" -> stringResource(R.string.item_type_book)
                                    "muziek" -> stringResource(R.string.item_type_music)
                                    "dvd" -> stringResource(R.string.item_type_dvd)
                                    "game" -> stringResource(R.string.item_type_game)
                                    else -> stringResource(R.string.filter_all)
                                },
                                style = filterButtonTextStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        DropdownMenu(
                            expanded = typeMenuExpanded,
                            onDismissRequest = { typeMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.filter_all), style = MaterialTheme.typography.bodyLarge) },
                                onClick = { filterType = null; typeMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.item_type_book), style = MaterialTheme.typography.bodyLarge) },
                                onClick = { filterType = "boek"; typeMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.item_type_music), style = MaterialTheme.typography.bodyLarge) },
                                onClick = { filterType = "muziek"; typeMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.item_type_dvd), style = MaterialTheme.typography.bodyLarge) },
                                onClick = { filterType = "dvd"; typeMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.item_type_game), style = MaterialTheme.typography.bodyLarge) },
                                onClick = { filterType = "game"; typeMenuExpanded = false }
                            )
                        }
                    }
                }
                // Filter
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.filter),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Box {
                        OutlinedButton(
                            onClick = { filterMenuExpanded = true },
                            modifier = Modifier.height(44.dp).fillMaxWidth(),
                            contentPadding = filterButtonPadding
                        ) {
                            Text(
                                text = activeFilterText,
                                style = filterButtonTextStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        DropdownMenu(
                            expanded = filterMenuExpanded,
                            onDismissRequest = { filterMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.filter_all), style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    filterRead = null
                                    filterPossession = null
                                    filterTbr = null
                                    filterMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.filter_read), style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    filterRead = true
                                    filterPossession = null
                                    filterTbr = null
                                    filterMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.filter_possession), style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    filterRead = null
                                    filterPossession = true
                                    filterTbr = null
                                    filterMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.filter_tbr), style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    filterRead = null
                                    filterPossession = null
                                    filterTbr = true
                                    filterMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            // Item teller
            Text(
                text = stringResource(R.string.items_count, sortedFilteredItems.size),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            // Lege staat
            if (sortedFilteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isBlank() && filterType == null && filterRead == null && filterPossession == null && filterTbr == null)
                            stringResource(R.string.empty_collection)
                        else
                            stringResource(R.string.empty_search),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(sortedFilteredItems) { item ->
                    val isExpanded = expandedItemId == item.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clickable {
                                expandedItemId = if (isExpanded) null else item.id
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                    Text(
                                        text = item.authorOrArtist,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                if (isExpanded) {
                                    IconButton(onClick = { onItemClick(item) }) {
                                        Icon(
                                            imageVector = Icons.Filled.Edit,
                                            contentDescription = stringResource(R.string.edit)
                                        )
                                    }
                                }
                            }
                            if (isExpanded) {
                                Text(
                                    text = stringResource(R.string.item_type_label, item.type),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                                )
                                Text(
                                    text = stringResource(R.string.item_code_label, item.code),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = item.isReadOrListened,
                                        onCheckedChange = { checked ->
                                            onUpdateItem(item.copy(isReadOrListened = checked))
                                        }
                                    )
                                    Text(
                                        text = when (item.type) {
                                            "boek" -> stringResource(R.string.item_read)
                                            else -> stringResource(R.string.item_listened)
                                        },
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = item.inPossession,
                                        onCheckedChange = { checked ->
                                            onUpdateItem(item.copy(inPossession = checked))
                                        }
                                    )
                                    Text(
                                        text = when (item.type) {
                                            "boek" -> stringResource(R.string.item_owned_book)
                                            "muziek" -> stringResource(R.string.item_owned_music)
                                            "game" -> stringResource(R.string.item_owned_game)
                                            "dvd" -> stringResource(R.string.item_owned_dvd)
                                            else -> stringResource(R.string.item_in_possession)
                                        },
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = item.tbr,
                                        onCheckedChange = { checked ->
                                            onUpdateItem(item.copy(tbr = checked))
                                        }
                                    )
                                    Text(
                                        text = stringResource(R.string.item_tbr),
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
    
    // Cover preview modal niet weergeven
}
