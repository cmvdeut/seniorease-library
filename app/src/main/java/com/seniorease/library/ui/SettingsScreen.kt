package com.seniorease.library.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import com.seniorease.library.BuildConfig
import com.seniorease.library.R
import com.seniorease.library.utils.LanguageHelper
import com.seniorease.library.utils.SettingsHelper

@Composable
fun SettingsScreen(
    isLargeTextEnabled: Boolean,
    onLargeTextToggle: (Boolean) -> Unit,
    isHighContrastEnabled: Boolean,
    onHighContrastToggle: (Boolean) -> Unit,
    isDemo: Boolean,
    onLanguageChange: (String) -> Unit,
    onThemeChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedLanguage by remember { mutableStateOf(LanguageHelper.getSavedLanguage(context)) }
    var languageMenuExpanded by remember { mutableStateOf(false) }
    var selectedTheme by remember { mutableStateOf(SettingsHelper.getThemeMode(context)) }
    var themeMenuExpanded by remember { mutableStateOf(false) }
    
    val languageDisplayText = when (selectedLanguage) {
        LanguageHelper.LANGUAGE_DUTCH -> stringResource(R.string.language_dutch)
        LanguageHelper.LANGUAGE_ENGLISH -> stringResource(R.string.language_english)
        else -> stringResource(R.string.language_system)
    }

    val themeDisplayText = when (selectedTheme) {
        SettingsHelper.THEME_LIGHT -> stringResource(R.string.theme_light)
        SettingsHelper.THEME_DARK -> stringResource(R.string.theme_dark)
        else -> stringResource(R.string.theme_system)
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.headlineMedium
            ) 
        },
        text = {
            Column {
                // Grote tekst toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.settings_large_text),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(R.string.settings_large_text_description),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Switch(
                        checked = isLargeTextEnabled,
                        onCheckedChange = onLargeTextToggle
                    )
                }
                
                // Hoog contrast toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.settings_high_contrast),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(R.string.settings_high_contrast_description),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Switch(
                        checked = isHighContrastEnabled,
                        onCheckedChange = onHighContrastToggle
                    )
                }
                
                // Taal selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.settings_language),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(R.string.settings_language_description),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Box {
                        OutlinedButton(
                            onClick = { languageMenuExpanded = true },
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(
                                text = languageDisplayText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        DropdownMenu(
                            expanded = languageMenuExpanded,
                            onDismissRequest = { languageMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.language_system), style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    selectedLanguage = LanguageHelper.LANGUAGE_SYSTEM
                                    LanguageHelper.saveLanguage(context, LanguageHelper.LANGUAGE_SYSTEM)
                                    onLanguageChange(LanguageHelper.LANGUAGE_SYSTEM)
                                    languageMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.language_dutch), style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    selectedLanguage = LanguageHelper.LANGUAGE_DUTCH
                                    LanguageHelper.saveLanguage(context, LanguageHelper.LANGUAGE_DUTCH)
                                    onLanguageChange(LanguageHelper.LANGUAGE_DUTCH)
                                    languageMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.language_english), style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    selectedLanguage = LanguageHelper.LANGUAGE_ENGLISH
                                    LanguageHelper.saveLanguage(context, LanguageHelper.LANGUAGE_ENGLISH)
                                    onLanguageChange(LanguageHelper.LANGUAGE_ENGLISH)
                                    languageMenuExpanded = false
                                }
                            )
                        }
                    }
                }
                
                // Thema selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.settings_theme),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(R.string.settings_theme_description),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Box {
                        OutlinedButton(
                            onClick = { themeMenuExpanded = true },
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(
                                text = themeDisplayText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        DropdownMenu(
                            expanded = themeMenuExpanded,
                            onDismissRequest = { themeMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.theme_system), style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    selectedTheme = SettingsHelper.THEME_SYSTEM
                                    SettingsHelper.setThemeMode(context, SettingsHelper.THEME_SYSTEM)
                                    onThemeChange(SettingsHelper.THEME_SYSTEM)
                                    themeMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.theme_light), style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    selectedTheme = SettingsHelper.THEME_LIGHT
                                    SettingsHelper.setThemeMode(context, SettingsHelper.THEME_LIGHT)
                                    onThemeChange(SettingsHelper.THEME_LIGHT)
                                    themeMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.theme_dark), style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    selectedTheme = SettingsHelper.THEME_DARK
                                    SettingsHelper.setThemeMode(context, SettingsHelper.THEME_DARK)
                                    onThemeChange(SettingsHelper.THEME_DARK)
                                    themeMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                val versionName = run {
                    val raw = BuildConfig.VERSION_NAME
                    if (isDemo) raw else if (raw.endsWith("-demo", ignoreCase = true)) {
                        raw.dropLast(5)
                    } else {
                        raw
                    }
                }
                Text(
                    text = stringResource(R.string.app_version, versionName),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}

