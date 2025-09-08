package com.lagradost.cloudstream3.desktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lagradost.cloudstream3.desktop.ui.Screen

@Composable
fun NavigationBar(
    currentScreen: Screen,
    onScreenChange: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // App Title
        Text(
            text = "CloudStream",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Navigation Items
        NavigationItem(
            icon = Icons.Default.Home,
            label = "Home",
            selected = currentScreen == Screen.HOME,
            onClick = { onScreenChange(Screen.HOME) }
        )
        
        NavigationItem(
            icon = Icons.Default.Search,
            label = "Search",
            selected = currentScreen == Screen.SEARCH,
            onClick = { onScreenChange(Screen.SEARCH) }
        )
        
        NavigationItem(
            icon = Icons.Default.VideoLibrary,
            label = "Library",
            selected = currentScreen == Screen.LIBRARY,
            onClick = { onScreenChange(Screen.LIBRARY) }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        NavigationItem(
            icon = Icons.Default.Settings,
            label = "Settings",
            selected = currentScreen == Screen.SETTINGS,
            onClick = { onScreenChange(Screen.SETTINGS) }
        )
    }
}

@Composable
private fun NavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
                   else MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = label,
            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface
        )
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}