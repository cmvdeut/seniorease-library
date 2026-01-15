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

@Composable
fun SettingsScreen(
    isLargeTextEnabled: Boolean,
    onLargeTextToggle: (Boolean) -> Unit,
    isHighContrastEnabled: Boolean,
    onHighContrastToggle: (Boolean) -> Unit,
    onLanguageChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedLanguage by remember { mutableStateOf(LanguageHelper.getSavedLanguage(context)) }
    var languageMenuExpanded by remember { mutableStateOf(false) }
    
    val languageDisplayText = when (selectedLanguage) {
        LanguageHelper.LANGUAGE_DUTCH -> stringResource(R.string.language_dutch)
        LanguageHelper.LANGUAGE_ENGLISH -> stringResource(R.string.language_english)
        else -> stringResource(R.string.language_system)
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
                
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.app_version, BuildConfig.VERSION_NAME),
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

