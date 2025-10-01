@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.maureen.biblitoheek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.maureen.biblitoheek.ui.theme.BiblitoheekTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maureen.biblitoheek.data.AppDatabase
import com.maureen.biblitoheek.data.Item
import com.maureen.biblitoheek.ui.ItemListScreen
import com.maureen.biblitoheek.ui.AddItemDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.io.InputStream
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import android.net.Uri
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import android.content.Intent

class MainViewModel(private val db: AppDatabase) : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            _items.value = db.itemDao().getAllSortedByNewest()
        }
    }

    fun addItem(item: Item, onResult: () -> Unit = {}) {
        viewModelScope.launch {
            db.itemDao().insert(item)
            loadItems()
            onResult()
        }
    }

    fun deleteItem(item: Item, onResult: () -> Unit = {}) {
        viewModelScope.launch {
            db.itemDao().delete(item)
            loadItems()
            onResult()
        }
    }

    fun updateItem(item: Item, onResult: () -> Unit = {}) {
        viewModelScope.launch {
            db.itemDao().update(item)
            loadItems()
            onResult()
        }
    }
}

class MainViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = AppDatabase.getDatabase(applicationContext)
        setContent {
            BiblitoheekTheme {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                var menuExpanded by remember { mutableStateOf(false) }
                var showDialog by remember { mutableStateOf(false) }
                var showStatsDialog by remember { mutableStateOf(false) } // Nieuw
                var selectedItem by remember { mutableStateOf<Item?>(null) }
                var lastType by remember { mutableStateOf("boek") }
                val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(db))
                val items by viewModel.items.collectAsState()
                val allAuthors = items.map { it.authorOrArtist }.distinct()

                // File picker launcher voor export
                val exportLauncher = rememberLauncherForActivityResult(CreateDocument("application/json")) { uri ->
                    if (uri != null) {
                        scope.launch {
                            try {
                                val gson = Gson()
                                val json = gson.toJson(items)
                                context.contentResolver.openOutputStream(uri)?.use { it.write(json.toByteArray()) }
                                Toast.makeText(context, "Backup opgeslagen", Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Fout bij opslaan backup", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                // File picker launcher voor import
                val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                    if (uri != null) {
                        scope.launch {
                            try {
                                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                                val json = inputStream?.bufferedReader()?.use { it.readText() }
                                if (json != null) {
                                    val gson = Gson()
                                    val importedItems = gson.fromJson(json, Array<Item>::class.java).toList()
                                    val existingCodes = viewModel.items.value.map { it.code }
                                    var added = 0
                                    for (item in importedItems) {
                                        if (!existingCodes.contains(item.code)) {
                                            viewModel.addItem(item)
                                            added++
                                        }
                                    }
                                    Toast.makeText(context, "$added items toegevoegd uit backup", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Fout bij importeren backup", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                val exportCsvLauncher = rememberLauncherForActivityResult(CreateDocument("text/csv")) { uri: Uri? ->
                    if (uri != null) {
                        scope.launch {
                            try {
                                val csvHeader = "Titel,Auteur/Artiest,Type,Code,Gelezen/In bezit,Medium,Taal\n"
                                val csvRows = items.joinToString("\n") { item ->
                                    listOf(
                                        item.title,
                                        item.authorOrArtist,
                                        item.type,
                                        item.code,
                                        "${if (item.isReadOrListened) "Ja" else "Nee"}/${if (item.inPossession) "Ja" else "Nee"}",
                                        item.medium ?: "",
                                        item.language ?: ""
                                    ).joinToString(",") { it.replace(",", " ") }
                                }
                                val csv = csvHeader + csvRows
                                context.contentResolver.openOutputStream(uri)?.use { it.write(csv.toByteArray()) }
                                Toast.makeText(context, "CSV geëxporteerd", Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Fout bij exporteren CSV", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                val exportPdfLauncher = rememberLauncherForActivityResult(CreateDocument("application/pdf")) { uri ->
                    if (uri != null) {
                        scope.launch {
                            try {
                                val pdfDocument = PdfDocument()
                                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
                                val page = pdfDocument.startPage(pageInfo)
                                val canvas = page.canvas
                                val paint = android.graphics.Paint()
                                paint.textSize = 14f
                                var y = 40f
                                // Header
                                canvas.drawText("Titel", 20f, y, paint)
                                canvas.drawText("Auteur/Artiest", 120f, y, paint)
                                canvas.drawText("Type", 300f, y, paint)
                                canvas.drawText("Code", 360f, y, paint)
                                canvas.drawText("Gelezen", 440f, y, paint)
                                canvas.drawText("In bezit", 500f, y, paint)
                                y += 24f
                                // Lijnen
                                paint.strokeWidth = 1f
                                canvas.drawLine(20f, y, 570f, y, paint)
                                y += 18f
                                // Data
                                items.forEach { item ->
                                    canvas.drawText(item.title.take(15), 20f, y, paint)
                                    canvas.drawText(item.authorOrArtist.take(15), 120f, y, paint)
                                    canvas.drawText(item.type, 300f, y, paint)
                                    canvas.drawText(item.code, 360f, y, paint)
                                    canvas.drawText(if (item.isReadOrListened) "Ja" else "Nee", 440f, y, paint)
                                    canvas.drawText(if (item.inPossession) "Ja" else "Nee", 500f, y, paint)
                                    y += 20f
                                    if (y > 800f) return@forEach // Simpel: max 1 pagina
                                }
                                pdfDocument.finishPage(page)
                                context.contentResolver.openOutputStream(uri)?.use { out ->
                                    pdfDocument.writeTo(out)
                                }
                                pdfDocument.close()
                                Toast.makeText(context, "PDF geëxporteerd", Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Fout bij exporteren PDF", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                fun sharePdfFile(context: android.content.Context, items: List<Item>) {
                    val pdfDocument = PdfDocument()
                    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                    val page = pdfDocument.startPage(pageInfo)
                    val canvas = page.canvas
                    val paint = android.graphics.Paint()
                    paint.textSize = 14f
                    var y = 40f
                    canvas.drawText("Titel", 20f, y, paint)
                    canvas.drawText("Auteur/Artiest", 120f, y, paint)
                    canvas.drawText("Type", 300f, y, paint)
                    canvas.drawText("Code", 360f, y, paint)
                    canvas.drawText("Gelezen", 440f, y, paint)
                    canvas.drawText("In bezit", 500f, y, paint)
                    y += 24f
                    paint.strokeWidth = 1f
                    canvas.drawLine(20f, y, 570f, y, paint)
                    y += 18f
                    items.forEach { item ->
                        canvas.drawText(item.title.take(15), 20f, y, paint)
                        canvas.drawText(item.authorOrArtist.take(15), 120f, y, paint)
                        canvas.drawText(item.type, 300f, y, paint)
                        canvas.drawText(item.code, 360f, y, paint)
                        canvas.drawText(if (item.isReadOrListened) "Ja" else "Nee", 440f, y, paint)
                        canvas.drawText(if (item.inPossession) "Ja" else "Nee", 500f, y, paint)
                        y += 20f
                        if (y > 800f) return@forEach
                    }
                    pdfDocument.finishPage(page)
                    val file = File(context.cacheDir, "biblitoheek_export.pdf")
                    file.outputStream().use { pdfDocument.writeTo(it) }
                    pdfDocument.close()
                    val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Deel PDF via..."))
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Biblitoheek") },
                            actions = {
                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                                }
                                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                                    DropdownMenuItem(text = { Text("Backup maken") }, onClick = {
                                        menuExpanded = false
                                        exportLauncher.launch("biblitoheek_backup.json")
                                    })
                                    DropdownMenuItem(text = { Text("Backup terugzetten") }, onClick = {
                                        menuExpanded = false
                                        importLauncher.launch(arrayOf("application/json"))
                                    })
                                    DropdownMenuItem(text = { Text("Exporteren naar CSV") }, onClick = {
                                        menuExpanded = false
                                        exportCsvLauncher.launch("biblitoheek_export.csv")
                                    })
                                    DropdownMenuItem(text = { Text("Exporteren naar PDF") }, onClick = {
                                        menuExpanded = false
                                        exportPdfLauncher.launch("biblitoheek_export.pdf")
                                    })
                                    DropdownMenuItem(text = { Text("PDF delen") }, onClick = {
                                        menuExpanded = false
                                        sharePdfFile(context, items)
                                    })
                                    DropdownMenuItem(text = { Text("Statistieken") }, onClick = {
                                        menuExpanded = false
                                        showStatsDialog = true
                                    })
                                }
                            }
                        )
                    },
                    content = { padding ->
                        Column(modifier = Modifier.padding(padding)) {
                            ItemListScreen(
                                items = items,
                                onAddClick =
                                    { type ->
                                    selectedItem = null
                                    lastType = type
                                    showDialog = true
                                },
                                onUpdateItem = { item -> viewModel.updateItem(item) },
                                onItemClick = { item ->
                                    selectedItem = item
                                    showDialog = true
                                }
                            )
                            if (showDialog) {
                                AddItemDialog(
                                    onAdd = { item ->
                                        if (selectedItem == null) {
                                            viewModel.addItem(item) { showDialog = false }
                                            lastType = item.type
                                        } else {
                                            viewModel.updateItem(item) { showDialog = false; selectedItem = null }
                                            lastType = item.type
                                        }
                                    },
                                    onDelete = { item ->
                                        viewModel.deleteItem(item) { showDialog = false; selectedItem = null }
                                    },
                                    onDismiss = {
                                        showDialog = false
                                        selectedItem = null
                                    },
                                    item = selectedItem,
                                    initialType = if (selectedItem == null) lastType else selectedItem?.type ?: lastType,
                                    onTypeChange = { lastType = it },
                                    allAuthors = allAuthors
                                )
                            }
                            if (showStatsDialog) {
                                val total = items.size
                                val books = items.count { it.type == "boek" }
                                val booksNL = items.count { it.type == "boek" && (it.language == "NL") }
                                val booksEN = items.count { it.type == "boek" && (it.language == "EN") }
                                val booksOther = items.count { it.type == "boek" && (it.language != null && it.language != "NL" && it.language != "EN") }
                                val music = items.count { it.type == "muziek" }
                                val games = items.count { it.type == "game" }
                                val dvds = items.count { it.type == "dvd" }
                                val readOrListened = items.count { it.isReadOrListened }
                                val inPossession = items.count { it.inPossession }
                                val cds = items.count { it.type == "muziek" && (it.medium?.lowercase() == "cd") }
                                val lps = items.count { it.type == "muziek" && (it.medium?.lowercase() == "lp") }
                                AlertDialog(
                                    onDismissRequest = { showStatsDialog = false },
                                    title = { Text("Statistieken") },
                                    text = {
                                        Column {
                                            Text("Totaal: $total")
                                            Text("Boeken: $books (NL: $booksNL, EN: $booksEN, Anders: $booksOther)")
                                            Text("Muziek: $music (cd's: $cds, lp's: $lps)")
                                            Text("Games: $games")
                                            Text("DVD's: $dvds")
                                            Text("Gelezen/beluisterd: $readOrListened")
                                            Text("In bezit: $inPossession")
                                        }
                                    },
                                    confirmButton = {
                                        Button(onClick = { showStatsDialog = false }) { Text("OK") }
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BiblitoheekTheme {
        Greeting("Android")
    }
}