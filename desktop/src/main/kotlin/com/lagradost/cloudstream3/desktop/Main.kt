package com.lagradost.cloudstream3.desktop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.*
import com.lagradost.cloudstream3.desktop.ui.DesktopApp
import com.lagradost.cloudstream3.desktop.utils.DesktopPreferences
import java.awt.Dimension

fun main() {
    // Initialize desktop-specific components
    DesktopPreferences.init()
    
    application {
        var isVisible by remember { mutableStateOf(true) }
        
        Window(
            onCloseRequest = { 
                isVisible = false
                exitApplication()
            },
            title = "CloudStream Desktop",
            state = rememberWindowState(
                size = DpSize(1280.dp, 720.dp)
            ),
            visible = isVisible
        ) {
            // Set minimum window size
            window.minimumSize = Dimension(800, 600)
            
            MaterialTheme(
                colorScheme = darkColorScheme()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DesktopApp()
                }
            }
        }
    }
}