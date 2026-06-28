@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.seniorease.library

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
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
import com.seniorease.library.ui.LocaleProvider
import com.seniorease.library.ui.OnboardingScreen
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
import android.app.Activity
import android.content.Context
import androidx.compose.ui.platform.LocalConfiguration
import com.seniorease.library.utils.ExportFileNames
import com.seniorease.library.utils.AppMaintenanceHelper
import com.seniorease.library.utils.BillingManager
import com.seniorease.library.utils.LanguageHelper
import com.seniorease.library.utils.SettingsHelper
import java.util.Locale
import androidx.compose.foundation.layout.Arrangement
import com.google.android.play.core.review.ReviewManagerFactory
import androidx.compose.foundation.isSystemInDarkTheme

class MainViewModel(private val db: AppDatabase, private val context: Context) : ViewModel() {
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

    fun addItem(item: Item, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            db.itemDao().insert(item)
            loadItems()
            onResult(true, null)
        }
    }
    
    fun getCurrentItemCount(): Int = _items.value.size

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

    private lateinit var billingManager: BillingManager

    override fun attachBaseContext(newBase: Context) {
        // Stel de taal in op basis van opgeslagen voorkeur VOOR onCreate
        val locale = LanguageHelper.getLocale(newBase)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
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
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.attributes = window.attributes.apply {
                layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
            }
        }
        super.onCreate(savedInstanceState)

        billingManager = BillingManager(applicationContext)
        billingManager.startConnection()

        val db = AppDatabase.getDatabase(applicationContext)
        setContent {
            val context = LocalContext.current
            // Haal instellingen op uit SharedPreferences
            var isLargeTextEnabled by remember { mutableStateOf(SettingsHelper.isLargeTextEnabled(context)) }
            var isHighContrastEnabled by remember { mutableStateOf(SettingsHelper.isHighContrastEnabled(context)) }
            var themeMode by remember { mutableStateOf(SettingsHelper.getThemeMode(context)) }
            var showOnboarding by remember { mutableStateOf(!SettingsHelper.isOnboardingDone(context)) }
            val isDarkTheme = when (themeMode) {
                SettingsHelper.THEME_LIGHT -> false
                SettingsHelper.THEME_DARK -> true
                else -> isSystemInDarkTheme()
            }

            val activity = this@MainActivity
            val scope = rememberCoroutineScope()
            val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(db, context))
            val items by viewModel.items.collectAsState()

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

            fun createPdfDocument(ctx: Context, itemList: List<Item>): PdfDocument {
                val pdfDocument = PdfDocument()
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas
                val paint = android.graphics.Paint()
                paint.textSize = 14f
                var y = 40f
                canvas.drawText(ctx.getString(R.string.pdf_header_title), 20f, y, paint)
                canvas.drawText(ctx.getString(R.string.pdf_header_author), 120f, y, paint)
                canvas.drawText(ctx.getString(R.string.pdf_header_type), 300f, y, paint)
                canvas.drawText(ctx.getString(R.string.pdf_header_code), 360f, y, paint)
                canvas.drawText(ctx.getString(R.string.pdf_header_read), 440f, y, paint)
                canvas.drawText(ctx.getString(R.string.pdf_header_possession), 500f, y, paint)
                y += 24f
                paint.strokeWidth = 1f
                canvas.drawLine(20f, y, 570f, y, paint)
                y += 18f
                itemList.forEach { item ->
                    canvas.drawText(item.title.take(15), 20f, y, paint)
                    canvas.drawText(item.authorOrArtist.take(15), 120f, y, paint)
                    canvas.drawText(item.type, 300f, y, paint)
                    canvas.drawText(item.code, 360f, y, paint)
                    canvas.drawText(if (item.isReadOrListened) ctx.getString(R.string.yes) else ctx.getString(R.string.no), 440f, y, paint)
                    canvas.drawText(if (item.inPossession) ctx.getString(R.string.yes) else ctx.getString(R.string.no), 500f, y, paint)
                    y += 20f
                    if (y > 800f) return@forEach
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

            fun sharePdfFile(ctx: Context, itemList: List<Item>) {
                try {
                    val pdfDocument = createPdfDocument(ctx, itemList)
                    val file = File(ctx.cacheDir, ExportFileNames.PDF_CACHE)
                    file.outputStream().use { pdfDocument.writeTo(it) }
                    pdfDocument.close()
                    val uri = FileProvider.getUriForFile(ctx, ctx.packageName + ".provider", file)
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    ctx.startActivity(Intent.createChooser(shareIntent, ctx.getString(R.string.share_pdf_via)))
                } catch (e: Exception) {
                    Toast.makeText(ctx, ctx.getString(R.string.pdf_export_error), Toast.LENGTH_LONG).show()
                }
            }

            LocaleProvider {
            BiblitoheekTheme(
                darkTheme = isDarkTheme,
                isLargeTextEnabled = isLargeTextEnabled,
                isHighContrastEnabled = isHighContrastEnabled,
                dynamicColor = false
            ) {
                val isPremium by billingManager.isPremiumFlow.collectAsState()

                var menuExpanded by remember { mutableStateOf(false) }
                var showDialog by remember { mutableStateOf(false) }
                var showUnlockDialog by remember { mutableStateOf(false) }

                LaunchedEffect(isPremium) {
                    if (isPremium) showUnlockDialog = false
                }
                var showStatsDialog by remember { mutableStateOf(false) } // Nieuw
                var showPrivacyDialog by remember { mutableStateOf(false) } // Privacy beleid
                var showClearDataDialog by remember { mutableStateOf(false) } // Wis alle data dialoog
                var showClearCacheDialog by remember { mutableStateOf(false) }
                var showAboutDialog by remember { mutableStateOf(false) } // Over de app dialoog
                var showSettingsDialog by remember { mutableStateOf(false) } // Settings
                var selectedItem by remember { mutableStateOf<Item?>(null) }
                var lastType by remember { mutableStateOf("boek") }
                val allAuthors = items.map { it.authorOrArtist }.distinct()

                if (showOnboarding) {
                    OnboardingScreen(
                        onDone = {
                            SettingsHelper.markOnboardingDone(applicationContext)
                            showOnboarding = false
                        },
                        onLanguageSelected = { language ->
                            SettingsHelper.markOnboardingLanguageChosen(applicationContext)
                            LanguageHelper.applyLanguage(applicationContext, language)
                            activity.recreate()
                        },
                    )
                    return@BiblitoheekTheme
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(R.drawable.icon_192),
                                        contentDescription = stringResource(R.string.app_logo),
                                        modifier = Modifier
                                            .size(32.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(stringResource(R.string.library))
                                }
                            },
                            actions = {
                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.menu))
                                }
                                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                                    DropdownMenuItem(text = { Text(stringResource(R.string.backup_create)) }, onClick = {
                                        menuExpanded = false
                                        exportLauncher.launch(ExportFileNames.BACKUP_JSON)
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.backup_restore)) }, onClick = {
                                        menuExpanded = false
                                        importLauncher.launch(arrayOf("application/json"))
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.export_csv)) }, onClick = {
                                        menuExpanded = false
                                        exportCsvLauncher.launch(ExportFileNames.EXPORT_CSV)
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.export_pdf)) }, onClick = {
                                        menuExpanded = false
                                        exportPdfLauncher.launch(ExportFileNames.EXPORT_PDF)
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
                                    DropdownMenuItem(text = { Text(stringResource(R.string.check_for_updates)) }, onClick = {
                                        menuExpanded = false
                                        if (!AppMaintenanceHelper.openPlayStoreListing(context)) {
                                            Toast.makeText(context, context.getString(R.string.play_store_not_found), Toast.LENGTH_LONG).show()
                                        }
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.clear_cache)) }, onClick = {
                                        menuExpanded = false
                                        showClearCacheDialog = true
                                    })
                                    DropdownMenuItem(text = { Text(stringResource(R.string.about_title)) }, onClick = {
                                        menuExpanded = false
                                        showAboutDialog = true
                                    })
                                }
                            }
                        )
                    },
                    content = { padding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                        ) {
                            ItemListScreen(
                                items = items,
                                onAddClick = { type ->
                                    if (!isPremium && viewModel.getCurrentItemCount() >= BillingManager.FREE_ITEM_LIMIT) {
                                        showUnlockDialog = true
                                    } else {
                                        selectedItem = null
                                        lastType = type
                                        showDialog = true
                                    }
                                },
                                onUpdateItem = { item -> viewModel.updateItem(item) },
                                onItemClick = { item ->
                                    selectedItem = item
                                    showDialog = true
                                },
                            )
                            if (showDialog) {
                                AddItemDialog(
                                    onAdd = { item ->
                                        if (selectedItem == null) {
                                            viewModel.addItem(item) { _, _ ->
                                                showDialog = false
                                                lastType = item.type
                                                // In-app review: trigger na 3e item, maximaal 1 keer
                                                val addedCount = SettingsHelper.incrementItemsAdded(context)
                                                if (addedCount >= 3 && !SettingsHelper.hasReviewBeenRequested(context)) {
                                                    val reviewManager = ReviewManagerFactory.create(context)
                                                    reviewManager.requestReviewFlow().addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            SettingsHelper.markReviewRequested(context)
                                                            val activity = context as? android.app.Activity
                                                            if (activity != null) {
                                                                reviewManager.launchReviewFlow(activity, task.result)
                                                            }
                                                        }
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
                                    allAuthors = allAuthors
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
                                        showSettingsDialog = false
                                        scope.launch {
                                            kotlinx.coroutines.delay(100)
                                            recreate()
                                        }
                                    },
                                    onThemeChange = { mode ->
                                        themeMode = mode
                                    },
                                    onDismiss = { showSettingsDialog = false },
                                    isPremium = isPremium,
                                    itemCount = viewModel.getCurrentItemCount(),
                                    onRestorePurchase = { billingManager.queryExistingPurchases() }
                                )
                            }
                            if (showUnlockDialog) {
                                AlertDialog(
                                    onDismissRequest = { showUnlockDialog = false },
                                    title = {
                                        Text(
                                            text = stringResource(R.string.premium_unlock_title),
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                    },
                                    text = {
                                        Text(
                                            text = stringResource(R.string.premium_unlock_message),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    },
                                    confirmButton = {
                                        Button(onClick = {
                                            billingManager.launchPurchaseFlow(activity)
                                        }) {
                                            Text(stringResource(R.string.premium_buy_button))
                                        }
                                    },
                                    dismissButton = {
                                        Column {
                                            OutlinedButton(
                                                onClick = {
                                                    billingManager.queryExistingPurchases()
                                                    showUnlockDialog = false
                                                },
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(stringResource(R.string.premium_restore_button))
                                            }
                                            Spacer(Modifier.height(4.dp))
                                            OutlinedButton(
                                                onClick = { showUnlockDialog = false },
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(stringResource(R.string.cancel))
                                            }
                                        }
                                    }
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

                            if (showClearCacheDialog) {
                                AlertDialog(
                                    onDismissRequest = { showClearCacheDialog = false },
                                    title = { Text(stringResource(R.string.clear_cache_title)) },
                                    text = { Text(stringResource(R.string.clear_cache_message)) },
                                    confirmButton = {
                                        Button(onClick = {
                                            showClearCacheDialog = false
                                            val ok = AppMaintenanceHelper.clearAppCache(context)
                                            Toast.makeText(
                                                context,
                                                context.getString(if (ok) R.string.clear_cache_success else R.string.clear_cache_error),
                                                Toast.LENGTH_LONG
                                            ).show()
                                            if (ok) activity.recreate()
                                        }) {
                                            Text(stringResource(R.string.clear_cache_confirm))
                                        }
                                    },
                                    dismissButton = {
                                        Button(onClick = { showClearCacheDialog = false }) {
                                            Text(stringResource(R.string.cancel))
                                        }
                                    }
                                )
                            }
                            
                            // Over de app dialog
                            if (showAboutDialog) {
                                val versionName = BuildConfig.VERSION_NAME.removeSuffix("-demo")
                                AlertDialog(
                                    onDismissRequest = { showAboutDialog = false },
                                    title = { Text(stringResource(R.string.about_title), style = MaterialTheme.typography.headlineMedium) },
                                    text = {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(stringResource(R.string.about_description), style = MaterialTheme.typography.bodyMedium)
                                            Spacer(Modifier.height(4.dp))
                                            Text(stringResource(R.string.about_version, versionName), style = MaterialTheme.typography.bodyMedium)
                                            Text(stringResource(R.string.about_website), style = MaterialTheme.typography.bodyMedium)
                                            Text(stringResource(R.string.about_contact), style = MaterialTheme.typography.bodyMedium)
                                        }
                                    },
                                    confirmButton = {
                                        Button(onClick = { showAboutDialog = false }) { Text(stringResource(R.string.ok)) }
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

    override fun onDestroy() {
        super.onDestroy()
        billingManager.disconnect()
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