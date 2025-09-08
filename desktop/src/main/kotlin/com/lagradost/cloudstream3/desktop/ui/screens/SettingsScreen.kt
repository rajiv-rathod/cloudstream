package com.lagradost.cloudstream3.desktop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        item {
            SettingsCategory(title = "General")
        }
        
        item {
            SettingsItem(
                icon = Icons.Default.Language,
                title = "Language",
                subtitle = "English",
                onClick = { /* TODO: Implement language selection */ }
            )
        }
        
        item {
            SettingsItem(
                icon = Icons.Default.DarkMode,
                title = "Theme",
                subtitle = "Dark",
                onClick = { /* TODO: Implement theme selection */ }
            )
        }
        
        item {
            SettingsCategory(title = "Video")
        }
        
        item {
            SettingsItem(
                icon = Icons.Default.HighQuality,
                title = "Default Quality",
                subtitle = "1080p",
                onClick = { /* TODO: Implement quality selection */ }
            )
        }
        
        item {
            SettingsItem(
                icon = Icons.Default.Subtitles,
                title = "Subtitles",
                subtitle = "Enabled",
                onClick = { /* TODO: Implement subtitle settings */ }
            )
        }
        
        item {
            SettingsCategory(title = "About")
        }
        
        item {
            SettingsItem(
                icon = Icons.Default.Info,
                title = "Version",
                subtitle = "4.5.5 Desktop",
                onClick = { /* TODO: Show version info */ }
            )
        }
        
        item {
            SettingsItem(
                icon = Icons.Default.Update,
                title = "Check for Updates",
                subtitle = "Check for app updates",
                onClick = { /* TODO: Implement update check */ }
            )
        }
    }
}

@Composable
private fun SettingsCategory(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open"
            )
        }
    }
}