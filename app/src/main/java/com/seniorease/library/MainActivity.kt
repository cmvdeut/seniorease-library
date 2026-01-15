@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.seniorease.library

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
import androidx.compose.ui.res.stringResource
import com.seniorease.library.R
import com.seniorease.library.ui.theme.BiblitoheekTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.seniorease.library.data.AppDatabase
import com.seniorease.library.data.Item
import com.seniorease.library.ui.ItemListScreen
import com.seniorease.library.ui.AddItemDialog
import com.seniorease.library.ui.SettingsScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalUriHandler
import com.seniorease.library.BuildConfig
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.io.InputStream
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import android.net.Uri
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import android.content.Intent
import android.content.res.Configuration
import android.content.Context
import androidx.compose.ui.platform.LocalConfiguration
import com.seniorease.library.utils.LanguageHelper
import com.seniorease.library.utils.SettingsHelper
import com.seniorease.library.utils.UnlockHelper
import java.util.Locale
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

class MainViewModel(private val db: AppDatabase, private val context: Context) : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()
    
    // Demo limiet check
    private val isDemo: Boolean = BuildConfig.IS_DEMO
    private val maxItems: Int = BuildConfig.MAX_ITEMS
    
    // Cache unlock status - read synchronously at startup before UI renders
    // This ensures the demo limit never returns after reopening the app
    private val _isUnlocked: Boolean = UnlockHelper.isUnlocked(context)
    
    // Check of app is unlocked (override demo mode)
    // Use cached value for consistency during app session
    private fun isUnlocked(): Boolean {
        return _isUnlocked
    }
    
    // Public method to refresh unlock status (call after successful unlock)
    fun refreshUnlockStatus() {
        // Note: This doesn't update _isUnlocked, but loadItems() will be called
        // which will re-read from SharedPreferences via UnlockHelper
        // For immediate effect, we'll reload items which checks the current status
    }

    init {
        // Read unlock status from SharedPreferences BEFORE loading items
        // This ensures the demo limit is never applied if the app is unlocked
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            // Re-check unlock status from SharedPreferences on each load
            // This ensures we pick up changes (e.g., after unlock via API)
            val currentlyUnlocked = UnlockHelper.isUnlocked(context)
            
            val allItems = db.itemDao().getAllSortedByNewest()
            // In demo mode: beperk tot maxItems (tenzij unlocked)
            // Apply unlock status BEFORE rendering UI to prevent demo limit from showing
            _items.value = if (isDemo && !currentlyUnlocked && maxItems > 0 && allItems.size > maxItems) {
                allItems.take(maxItems)
            } else {
                allItems
            }
        }
    }

    fun addItem(item: Item, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            // Re-check unlock status from SharedPreferences (may have changed via API)
            val currentlyUnlocked = UnlockHelper.isUnlocked(context)
            
            // Check demo limiet (tenzij unlocked)
            if (isDemo && !currentlyUnlocked && maxItems > 0) {
                val currentCount = db.itemDao().getAllSortedByNewest().size
                // Bij 10 items: blokkeer toevoegen
                if (currentCount >= maxItems) {
                    // Error message wordt in MainActivity geformatteerd met context
                    onResult(false, "DEMO_LIMIT") // Special marker voor MainActivity
                    return@launch
                }
            }
            
            db.itemDao().insert(item)
            loadItems()
            // Return special marker voor waarschuwing bij 9 items (tenzij unlocked)
            val newCount = db.itemDao().getAllSortedByNewest().size
            if (isDemo && !currentlyUnlocked && maxItems > 0 && newCount == maxItems - 1) {
                onResult(true, "WARNING_ONE_LEFT")
            } else if (isDemo && !currentlyUnlocked && maxItems > 0 && newCount >= maxItems) {
                onResult(true, "UNLOCK_DIALOG")
            } else {
                onResult(true, null)
            }
        }
    }
    
    fun isDemoVersion(): Boolean {
        // Always check current unlock status from SharedPreferences
        // This ensures UI reflects unlock status even after app restart
        return isDemo && !UnlockHelper.isUnlocked(context)
    }
    fun getMaxItems(): Int = maxItems
    fun getCurrentItemCount(): Int = _items.value.size
    // unlockWithCode removed - using email-based unlock via API only

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

    fun clearAllData(onResult: () -> Unit = {}) {
        viewModelScope.launch {
            db.itemDao().clearAllItems()
            loadItems()
            onResult()
        }
    }
}

class MainViewModelFactory(private val db: AppDatabase, private val context: android.content.Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(db, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivity : ComponentActivity() {
    
    override fun attachBaseContext(newBase: Context) {
        // Stel de taal in op basis van opgeslagen voorkeur VOOR onCreate
        val locale = LanguageHelper.getLocale(newBase)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        val context = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            newBase.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            newBase.resources.updateConfiguration(config, newBase.resources.displayMetrics)
            newBase
        }
        super.attachBaseContext(context)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Read unlock status from SharedPreferences BEFORE rendering UI
        // This ensures the demo limit never returns after reopening the app
        val isUnlocked = UnlockHelper.isUnlocked(applicationContext)
        android.util.Log.d("MainActivity", "App startup - isUnlocked: $isUnlocked")
        
        val db = AppDatabase.getDatabase(applicationContext)
        setContent {
            val context = LocalContext.current
            val uriHandler = LocalUriHandler.current
            // Haal instellingen op uit SharedPreferences
            var isLargeTextEnabled by remember { mutableStateOf(SettingsHelper.isLargeTextEnabled(context)) }
            var isHighContrastEnabled by remember { mutableStateOf(SettingsHelper.isHighContrastEnabled(context)) }
            
            BiblitoheekTheme(
                isLargeTextEnabled = isLargeTextEnabled,
                isHighContrastEnabled = isHighContrastEnabled
            ) {
                val scope = rememberCoroutineScope()
                var menuExpanded by remember { mutableStateOf(false) }
                var showDialog by remember { mutableStateOf(false) }
                var showStatsDialog by remember { mutableStateOf(false) } // Nieuw
                var showPrivacyDialog by remember { mutableStateOf(false) } // Privacy beleid
                var showClearDataDialog by remember { mutableStateOf(false) } // Wis alle data dialoog
                var showSettingsDialog by remember { mutableStateOf(false) } // Settings
                var showWarningOneLeft by remember { mutableStateOf(false) } // Warning: 1 item left
                var showUnlockDialog by remember { mutableStateOf(false) } // Unlock full version popup
                // showPaymentThankYouDialog removed - using email-based unlock flow only
                var showUnlockVerifyDialog by remember { mutableStateOf(false) } // Email-based unlock verification dialog
                var selectedItem by remember { mutableStateOf<Item?>(null) }
                var lastType by remember { mutableStateOf("boek") }
                val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(db, context))
                val items by viewModel.items.collectAsState()
                val allAuthors = items.map { it.authorOrArtist }.distinct()

                // Payment browser launcher - gebruik onResume callback om terugkeer te detecteren
                var paymentOpened by remember { mutableStateOf(false) }

                // File picker launcher voor export
                val exportLauncher = rememberLauncherForActivityResult(CreateDocument("application/json")) { uri ->
                    if (uri != null) {
                        scope.launch {
                            try {
                                val gson = Gson()
                                val json = gson.toJson(items)
                                context.contentResolver.openOutputStream(uri)?.use { it.write(json.toByteArray()) }
                                Toast.makeText(context, context.getString(R.string.backup_saved), Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, context.getString(R.string.backup_error), Toast.LENGTH_LONG).show()
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
                                    Toast.makeText(context, context.getString(R.string.backup_imported, added), Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, context.getString(R.string.backup_import_error), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                val exportCsvLauncher = rememberLauncherForActivityResult(CreateDocument("text/csv")) { uri: Uri? ->
                    if (uri != null) {
                        scope.launch {
                            try {
                                val csvHeader = "${context.getString(R.string.csv_header_title)},${context.getString(R.string.csv_header_author)},${context.getString(R.string.csv_header_type)},${context.getString(R.string.csv_header_code)},${context.getString(R.string.csv_header_status)},${context.getString(R.string.csv_header_medium)},${context.getString(R.string.csv_header_language)}\n"
                                val csvRows = items.joinToString("\n") { item ->
                                    listOf(
                                        item.title,
                                        item.authorOrArtist,
                                        item.type,
                                        item.code,
                                        "${if (item.isReadOrListened) context.getString(R.string.yes) else context.getString(R.string.no)}/${if (item.inPossession) context.getString(R.string.yes) else context.getString(R.string.no)}",
                                        item.medium ?: "",
                                        item.language ?: ""
                                    ).joinToString(",") { it.replace(",", " ") }
                                }
                                val csv = csvHeader + csvRows
                                context.contentResolver.openOutputStream(uri)?.use { it.write(csv.toByteArray()) }
                                Toast.makeText(context, context.getString(R.string.csv_exported), Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, context.getString(R.string.csv_export_error), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                // Helper functie om PDF te genereren (hergebruikt voor export en share)
                fun createPdfDocument(context: android.content.Context, items: List<Item>): PdfDocument {
                    val pdfDocument = PdfDocument()
                    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
                    val page = pdfDocument.startPage(pageInfo)
                    val canvas = page.canvas
                    val paint = android.graphics.Paint()
                    paint.textSize = 14f
                    var y = 40f
                    
                    // Header
                    canvas.drawText(context.getString(R.string.pdf_header_title), 20f, y, paint)
                    canvas.drawText(context.getString(R.string.pdf_header_author), 120f, y, paint)
                    canvas.drawText(context.getString(R.string.pdf_header_type), 300f, y, paint)
                    canvas.drawText(context.getString(R.string.pdf_header_code), 360f, y, paint)
                    canvas.drawText(context.getString(R.string.pdf_header_read), 440f, y, paint)
                    canvas.drawText(context.getString(R.string.pdf_header_possession), 500f, y, paint)
                    y += 24f
                    
                    // Lijn
                    paint.strokeWidth = 1f
                    canvas.drawLine(20f, y, 570f, y, paint)
                    y += 18f
                    
                    // Data
                    items.forEach { item ->
                        canvas.drawText(item.title.take(15), 20f, y, paint)
                        canvas.drawText(item.authorOrArtist.take(15), 120f, y, paint)
                        canvas.drawText(item.type, 300f, y, paint)
                        canvas.drawText(item.code, 360f, y, paint)
                        canvas.drawText(if (item.isReadOrListened) context.getString(R.string.yes) else context.getString(R.string.no), 440f, y, paint)
                        canvas.drawText(if (item.inPossession) context.getString(R.string.yes) else context.getString(R.string.no), 500f, y, paint)
                        y += 20f
                        if (y > 800f) return@forEach // Max 1 pagina
                    }
                    pdfDocument.finishPage(page)
                    return pdfDocument
                }

                val exportPdfLauncher = rememberLauncherForActivityResult(CreateDocument("application/pdf")) { uri ->
                    if (uri != null) {
                        scope.launch {
                            try {
                                val pdfDocument = createPdfDocument(context, items)
                                context.contentResolver.openOutputStream(uri)?.use { out ->
                                    pdfDocument.writeTo(out)
                                }
                                pdfDocument.close()
                                Toast.makeText(context, context.getString(R.string.pdf_exported), Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, context.getString(R.string.pdf_export_error), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                fun sharePdfFile(context: android.content.Context, items: List<Item>) {
                    try {
                        val pdfDocument = createPdfDocument(context, items)
                        val file = File(context.cacheDir, "biblitoheek_export.pdf")
                        file.outputStream().use { pdfDocument.writeTo(it) }
                        pdfDocument.close()
                        val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/pdf"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_pdf_via)))
                    } catch (e: Exception) {
                        Toast.makeText(context, context.getString(R.string.pdf_export_error), Toast.LENGTH_LONG).show()
                    }
                }

                Scaffold(
                    topBar = {
                        val isDemo = viewModel.isDemoVersion()
                        val currentCount = if (isDemo) viewModel.getCurrentItemCount() else 0
                        val maxItems = if (isDemo) viewModel.getMaxItems() else 0
                        
                        TopAppBar(
                            title = { 
                                Column {
                                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                        // SeniorEase logo icoon
                                        Image(
                                            painter = painterResource(R.drawable.icon_192),
                                            contentDescription = stringResource(R.string.app_logo),
                                            modifier = Modifier
                                                .size(32.dp)
                                                .padding(end = 8.dp)
                                        )
                                        Text(stringResource(R.string.library))
                                    }
                                    // Demo tekst
                                    if (isDemo) {
                                        Text(
                                            text = stringResource(R.string.demo_status, currentCount, maxItems),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        text = stringResource(R.string.app_version, BuildConfig.VERSION_NAME),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.menu))
                                }
                                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                                    DropdownMenuItem(text = { Text(stringResource(R.string.backup_create)) }, onClick = {
                                        menuExpanded = false
                                        exportLauncher.launch("biblitoheek_backup.json")
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.backup_restore)) }, onClick = {
                                        menuExpanded = false
                                        importLauncher.launch(arrayOf("application/json"))
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.export_csv)) }, onClick = {
                                        menuExpanded = false
                                        exportCsvLauncher.launch("biblitoheek_export.csv")
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.export_pdf)) }, onClick = {
                                        menuExpanded = false
                                        exportPdfLauncher.launch("biblitoheek_export.pdf")
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.share_pdf)) }, onClick = {
                                        menuExpanded = false
                                        sharePdfFile(context, items)
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.statistics)) }, onClick = {
                                        menuExpanded = false
                                        showStatsDialog = true
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.settings_title)) }, onClick = {
                                        menuExpanded = false
                                        showSettingsDialog = true
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.privacy_policy)) }, onClick = {
                                        menuExpanded = false
                                        showPrivacyDialog = true
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.clear_all_data)) }, onClick = {
                                        menuExpanded = false
                                        showClearDataDialog = true
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
                                },
                                isDemo = viewModel.isDemoVersion(),
                                maxItems = viewModel.getMaxItems(),
                                currentCount = viewModel.getCurrentItemCount(),
                                onUnlockClick = {
                                    // Toon unlock dialog wanneer op Toevoegen wordt geklikt bij limiet
                                    showUnlockDialog = true
                                }
                            )
                            if (showDialog) {
                                AddItemDialog(
                                    onAdd = { item ->
                                        if (selectedItem == null) {
                                            viewModel.addItem(item) { success, errorMessage ->
                                                if (success) {
                                                    showDialog = false
                                                    lastType = item.type
                                                    
                                                    // Check voor waarschuwingen na succesvol toevoegen
                                                    if (errorMessage == "WARNING_ONE_LEFT") {
                                                        // Na 9e item: waarschuwing dat er nog 1 over is
                                                        showWarningOneLeft = true
                                                    } else if (errorMessage == "UNLOCK_DIALOG") {
                                                        // Na 10e item: unlock popup (dit zou niet moeten gebeuren, maar voor de zekerheid)
                                                        showUnlockDialog = true
                                                    }
                                                } else {
                                                    // Toon error message als limiet bereikt
                                                    if (errorMessage == "DEMO_LIMIT") {
                                                        // Toon unlock popup bij limiet bereikt
                                                        showUnlockDialog = true
                                                    } else {
                                                        val msg = errorMessage ?: context.getString(R.string.demo_limit_reached, viewModel.getMaxItems())
                                                        android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show()
                                                    }
                                                }
                                            }
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
                                    allAuthors = allAuthors,
                                    isDemo = viewModel.isDemoVersion(),
                                    maxItems = viewModel.getMaxItems(),
                                    currentItemCount = viewModel.getCurrentItemCount()
                                )
                            }
                            if (showStatsDialog) {
                                val total = items.size
                                val books = items.count { it.type == "boek" }
                                val booksNL = items.count { it.type == "boek" && (it.language == "NL") }
                                val booksEN = items.count { it.type == "boek" && (it.language == "EN") }
                                val booksOther = items.count { it.type == "boek" && (it.language != null && it.language != "NL" && it.language != "EN") }
                                val read = items.count { it.isReadOrListened }
                                val inPossession = items.count { it.inPossession }
                                AlertDialog(
                                    onDismissRequest = { showStatsDialog = false },
                                    title = { Text(stringResource(R.string.statistics_title)) },
                                    text = {
                                        Column {
                                            Text(stringResource(R.string.statistics_total, total))
                                            Text(stringResource(R.string.statistics_books, books, booksNL, booksEN, booksOther))
                                            Text(stringResource(R.string.statistics_read, read))
                                            Text(stringResource(R.string.statistics_possession, inPossession))
                                        }
                                    },
                                    confirmButton = {
                                        Button(onClick = { showStatsDialog = false }) { Text(stringResource(R.string.ok)) }
                                    }
                                )
                            }
                            if (showSettingsDialog) {
                                SettingsScreen(
                                    isLargeTextEnabled = isLargeTextEnabled,
                                    onLargeTextToggle = { enabled ->
                                        isLargeTextEnabled = enabled
                                        SettingsHelper.setLargeTextEnabled(context, enabled)
                                    },
                                    isHighContrastEnabled = isHighContrastEnabled,
                                    onHighContrastToggle = { enabled ->
                                        isHighContrastEnabled = enabled
                                        SettingsHelper.setHighContrastEnabled(context, enabled)
                                    },
                                    onLanguageChange = { language ->
                                        // Taal is al opgeslagen in LanguageHelper.saveLanguage
                                        // Sluit eerst het settings dialoog
                                        showSettingsDialog = false
                                        // Recreate de Activity om de nieuwe taal te laden
                                        // Gebruik een kleine delay om het dialoog eerst te sluiten
                                        scope.launch {
                                            kotlinx.coroutines.delay(100)
                                            recreate()
                                        }
                                    },
                                    onDismiss = { showSettingsDialog = false }
                                )
                            }
                            if (showPrivacyDialog) {
                                PrivacyPolicyDialog(
                                    onDismiss = { showPrivacyDialog = false }
                                )
                            }
                            if (showClearDataDialog) {
                                AlertDialog(
                                    onDismissRequest = { showClearDataDialog = false },
                                    title = { Text(stringResource(R.string.clear_all_title)) },
                                    text = {
                                        Text(stringResource(R.string.clear_all_message))
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                viewModel.clearAllData {
                                                    showClearDataDialog = false
                                                    Toast.makeText(context, context.getString(R.string.all_data_cleared), Toast.LENGTH_LONG).show()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.error
                                            )
                                        ) { 
                                            Text(stringResource(R.string.clear_all_confirm)) 
                                        }
                                    },
                                    dismissButton = {
                                        Button(
                                            onClick = { showClearDataDialog = false },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.surface
                                            )
                                        ) { 
                                            Text(stringResource(R.string.cancel)) 
                                        }
                                    }
                                )
                            }
                            
                            // Waarschuwing: nog 1 item over (na 9e item)
                            if (showWarningOneLeft) {
                                AlertDialog(
                                    onDismissRequest = { showWarningOneLeft = false },
                                    title = { 
                                        Text(
                                            text = stringResource(R.string.demo_warning_one_left),
                                            style = MaterialTheme.typography.titleLarge
                                        ) 
                                    },
                                    text = {
                                        Text(
                                            text = stringResource(R.string.demo_limit_warning_upgrade),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    },
                                    confirmButton = {
                                        Button(onClick = { showWarningOneLeft = false }) {
                                            Text(stringResource(R.string.ok))
                                        }
                                    }
                                )
                            }
                            
                            // Unlock full version popup (na 10e item)
                            if (showUnlockDialog) {
                                AlertDialog(
                                    onDismissRequest = { showUnlockDialog = false },
                                    title = { 
                                        Text(
                                            text = stringResource(R.string.demo_unlock_title),
                                            style = MaterialTheme.typography.headlineMedium
                                        ) 
                                    },
                                    text = {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = stringResource(R.string.demo_unlock_message, viewModel.getMaxItems()),
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(bottom = 16.dp)
                                            )
                                            Text(
                                                text = stringResource(R.string.demo_unlock_payment_note),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            )
                                        }
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = { 
                                                // Open payment link in browser - use UriHandler for better compatibility
                                                val paymentUrl = "https://buy.stripe.com/test_aFaaEQ9X0dGogALgwu6c003"
                                                
                                                try {
                                                    // Use UriHandler (same as other parts of the app) - more reliable
                                                    uriHandler.openUri(paymentUrl)
                                                    paymentOpened = true
                                                    showUnlockDialog = false
                                                } catch (e: Exception) {
                                                    // Fallback: try Intent approach
                                                    try {
                                                        val uri = android.net.Uri.parse(paymentUrl)
                                                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri)
                                                        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                                                        context.startActivity(intent)
                                                        paymentOpened = true
                                                        showUnlockDialog = false
                                                    } catch (e2: Exception) {
                                                        // Show error with URL so user can copy it
                                                        android.widget.Toast.makeText(
                                                            context,
                                                            "Could not open browser. Please copy and open: $paymentUrl",
                                                            android.widget.Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Text(stringResource(R.string.demo_unlock_button))
                                        }
                                    },
                                    dismissButton = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            OutlinedButton(
                                                onClick = { showUnlockVerifyDialog = true },
                                                modifier = Modifier.weight(1f),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                            ) {
                                                Text(stringResource(R.string.unlock_ive_paid_button))
                                            }
                                            OutlinedButton(
                                                onClick = { showUnlockDialog = false },
                                                modifier = Modifier.weight(1f),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.surface,
                                                    contentColor = MaterialTheme.colorScheme.onSurface
                                                )
                                            ) {
                                                Text(
                                                    stringResource(R.string.close),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                            
                            // Email-based unlock verification dialog (I've paid — unlock)
                            if (showUnlockVerifyDialog) {
                                var email by remember { mutableStateOf("") }
                                var isLoading by remember { mutableStateOf(false) }
                                var errorMessage by remember { mutableStateOf<String?>(null) }
                                
                                AlertDialog(
                                    onDismissRequest = { 
                                        if (!isLoading) {
                                            showUnlockVerifyDialog = false
                                            email = ""
                                            errorMessage = null
                                        }
                                    },
                                    title = { 
                                        Text(
                                            text = stringResource(R.string.unlock_verify_title),
                                            style = MaterialTheme.typography.headlineMedium
                                        ) 
                                    },
                                    text = {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            if (isLoading) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.padding(16.dp)
                                                )
                                                Text(
                                                    text = stringResource(R.string.unlock_verify_checking),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    modifier = Modifier.padding(top = 8.dp)
                                                )
                                            } else {
                                                Text(
                                                    text = stringResource(R.string.unlock_verify_message),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.padding(bottom = 16.dp)
                                                )
                                                OutlinedTextField(
                                                    value = email,
                                                    onValueChange = { 
                                                        email = it
                                                        errorMessage = null
                                                    },
                                                    label = { Text(stringResource(R.string.unlock_verify_email_hint)) },
                                                    placeholder = { Text("example@email.com") },
                                                    singleLine = true,
                                                    enabled = !isLoading,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    keyboardOptions = KeyboardOptions(
                                                        keyboardType = KeyboardType.Email
                                                    )
                                                )
                                                if (errorMessage != null) {
                                                    Text(
                                                        text = errorMessage!!,
                                                        color = MaterialTheme.colorScheme.error,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        modifier = Modifier.padding(top = 8.dp)
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                if (email.isBlank()) {
                                                    errorMessage = context.getString(R.string.unlock_verify_email_required)
                                                    return@Button
                                                }
                                                
                                                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                                    errorMessage = context.getString(R.string.unlock_verify_email_invalid)
                                                    return@Button
                                                }
                                                
                                                isLoading = true
                                                errorMessage = null
                                                
                                                scope.launch {
                                                    try {
                                                        // API URL
                                                        val apiUrl = "https://www.seniorease.eu/api/verify-purchase"
                                                        android.util.Log.d("UnlockVerify", "Sending request to: $apiUrl")
                                                        android.util.Log.d("UnlockVerify", "Email: ${email.trim()}")
                                                        
                                                        // Execute ALL network operations on IO dispatcher to avoid NetworkOnMainThreadException
                                                        val response = withContext(Dispatchers.IO) {
                                                            // Create OkHttpClient with timeout
                                                            val client = OkHttpClient.Builder()
                                                                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                                                                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                                                                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                                                                .build()
                                                            
                                                            val json = org.json.JSONObject().apply {
                                                                put("email", email.trim())
                                                            }
                                                            val mediaType = "application/json; charset=utf-8".toMediaType()
                                                            val requestBody = json.toString().toRequestBody(mediaType)
                                                            
                                                            val request = Request.Builder()
                                                                .url(apiUrl)
                                                                .post(requestBody)
                                                                .addHeader("Content-Type", "application/json")
                                                                .build()
                                                            
                                                            client.newCall(request).execute()
                                                        }
                                                        
                                                        val responseBody = withContext(Dispatchers.IO) {
                                                            response.body?.string()
                                                        }
                                                        
                                                        // Debug log: raw HTTP status + response body (debug builds only)
                                                        if (BuildConfig.DEBUG) {
                                                            android.util.Log.d("UnlockVerify", "HTTP Status: ${response.code}")
                                                            android.util.Log.d("UnlockVerify", "Response Body (raw): $responseBody")
                                                        }
                                                        
                                                        android.util.Log.d("UnlockVerify", "Response code: ${response.code}")
                                                        android.util.Log.d("UnlockVerify", "Response body: $responseBody")
                                                        
                                                        if (response.isSuccessful && responseBody != null) {
                                                            try {
                                                                // Parse JSON response: { "paid": true } or { "paid": false }
                                                                val jsonResponse = org.json.JSONObject(responseBody)
                                                                val paid = jsonResponse.optBoolean("paid", false)
                                                                
                                                                if (paid) {
                                                                    // Set isUnlocked=true in SharedPreferences
                                                                    UnlockHelper.unlockDirectly(context)
                                                                    
                                                                    // Update UI immediately (hide demo limit)
                                                                    viewModel.loadItems() // Reload items to reflect unlock
                                                                    isLoading = false
                                                                    showUnlockVerifyDialog = false
                                                                    showUnlockDialog = false
                                                                    
                                                                    // Show success message
                                                                    android.widget.Toast.makeText(
                                                                        context,
                                                                        context.getString(R.string.unlock_verify_success),
                                                                        android.widget.Toast.LENGTH_LONG
                                                                    ).show()
                                                                } else {
                                                                    // Betaling niet gevonden
                                                                    isLoading = false
                                                                    errorMessage = context.getString(R.string.unlock_verify_not_found)
                                                                }
                                                            } catch (e: org.json.JSONException) {
                                                                android.util.Log.e("UnlockVerify", "JSON parsing error", e)
                                                                if (BuildConfig.DEBUG) {
                                                                    android.util.Log.e("UnlockVerify", "Failed to parse response: $responseBody", e)
                                                                }
                                                                isLoading = false
                                                                errorMessage = context.getString(R.string.unlock_verify_error)
                                                            }
                                                        } else {
                                                            // API error
                                                            android.util.Log.e("UnlockVerify", "API error: ${response.code}, body: $responseBody")
                                                            isLoading = false
                                                            errorMessage = context.getString(R.string.unlock_verify_error)
                                                        }
                                                    } catch (e: java.net.UnknownHostException) {
                                                        android.util.Log.e("UnlockVerify", "Network error: No internet", e)
                                                        isLoading = false
                                                        errorMessage = context.getString(R.string.unlock_verify_error)
                                                    } catch (e: java.net.SocketTimeoutException) {
                                                        android.util.Log.e("UnlockVerify", "Network error: Timeout", e)
                                                        isLoading = false
                                                        errorMessage = context.getString(R.string.unlock_verify_error)
                                                    } catch (e: Exception) {
                                                        android.util.Log.e("UnlockVerify", "Error verifying purchase", e)
                                                        android.util.Log.e("UnlockVerify", "Exception type: ${e.javaClass.simpleName}, message: ${e.message}")
                                                        isLoading = false
                                                        errorMessage = context.getString(R.string.unlock_verify_error)
                                                    }
                                                }
                                            },
                                            enabled = !isLoading && email.isNotBlank(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Text(stringResource(R.string.unlock_verify_button))
                                        }
                                    },
                                    dismissButton = {
                                        OutlinedButton(
                                            onClick = { 
                                                if (!isLoading) {
                                                    showUnlockVerifyDialog = false
                                                    email = ""
                                                    errorMessage = null
                                                }
                                            },
                                            enabled = !isLoading,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.surface
                                            )
                                        ) {
                                            Text(
                                                stringResource(R.string.close),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                )
                            }
                            
                            // Detect return from browser after payment
                            LaunchedEffect(paymentOpened) {
                                if (paymentOpened) {
                                    kotlinx.coroutines.delay(500) // Wait a moment for app to become active
                                    // Direct to email-based unlock flow
                                    showUnlockVerifyDialog = true
                                    paymentOpened = false
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PrivacyPolicyDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = stringResource(R.string.privacy_policy_title),
                style = MaterialTheme.typography.headlineMedium
            ) 
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
            ) {
                Text(
                    text = stringResource(R.string.privacy_policy_app_name),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = stringResource(R.string.privacy_policy_last_updated),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                LazyColumn {
                    item {
                        Text(
                            text = stringResource(R.string.privacy_policy_section_1_title),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = stringResource(R.string.privacy_policy_section_1_text),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    
                    item {
                        Text(
                            text = stringResource(R.string.privacy_policy_section_2_title),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = stringResource(R.string.privacy_policy_section_2_text),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    
                    item {
                        Text(
                            text = stringResource(R.string.privacy_policy_section_3_title),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = stringResource(R.string.privacy_policy_section_3_text),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    
                    item {
                        Text(
                            text = stringResource(R.string.privacy_policy_section_4_title),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = stringResource(R.string.privacy_policy_section_4_text),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    
                    item {
                        Text(
                            text = stringResource(R.string.privacy_policy_section_5_title),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = stringResource(R.string.privacy_policy_section_5_text),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    
                    item {
                        Text(
                            text = stringResource(R.string.privacy_policy_section_6_title),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = stringResource(R.string.privacy_policy_section_6_text),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { 
                Text(stringResource(R.string.close)) 
            }
        }
    )
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