package com.maureen.biblitoheek.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    isLargeTextEnabled: Boolean,
    onLargeTextToggle: (Boolean) -> Unit,
    isHighContrastEnabled: Boolean,
    onHighContrastToggle: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = "Instellingen",
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
                            text = "Grote tekst",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Vergroot alle tekst in de app voor betere leesbaarheid",
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
                            text = "Hoog contrast",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Verhoogt contrast voor betere leesbaarheid (WCAG AAA)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Switch(
                        checked = isHighContrastEnabled,
                        onCheckedChange = onHighContrastToggle
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Toegankelijkheidsinfo
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Toegankelijkheid",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Minimale tekstgrootte: 18sp\n" +
                                  "• Respecteert Android lettergrootte instellingen\n" +
                                  "• Grote tekst toggle: +25% vergroting\n" +
                                  "• Hoog contrast: WCAG AAA compliant (21:1)\n" +
                                  "• Geen tekst op foto's zonder achtergrond",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Sluiten")
            }
        }
    )
}

