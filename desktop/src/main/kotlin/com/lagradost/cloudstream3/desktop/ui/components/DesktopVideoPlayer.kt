package com.lagradost.cloudstream3.desktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.net.URI

@Composable
fun DesktopVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentTime by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }
    val scope = rememberCoroutineScope()
    
    Column(
        modifier = modifier
    ) {
        // Video display area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            // For now, show a placeholder with option to open in external player
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircleOutline,
                    contentDescription = "Play Video",
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Desktop Video Player",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Click to open in external player",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        scope.launch {
                            openInExternalPlayer(videoUrl)
                        }
                    }
                ) {
                    Icon(Icons.Default.Launch, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open Externally")
                }
            }
        }
        
        // Control bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 8.dp, bottomEnd = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Progress bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatTime(currentTime),
                        style = MaterialTheme.typography.bodySmall
                    )
                    
                    Slider(
                        value = if (duration > 0) currentTime.toFloat() / duration else 0f,
                        onValueChange = { /* TODO: Implement seek */ },
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    )
                    
                    Text(
                        text = formatTime(duration),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Control buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* TODO: Previous */ }) {
                        Icon(Icons.Default.SkipPrevious, contentDescription = "Previous")
                    }
                    
                    IconButton(
                        onClick = { 
                            isPlaying = !isPlaying
                            // TODO: Implement play/pause
                        }
                    ) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play"
                        )
                    }
                    
                    IconButton(onClick = { /* TODO: Next */ }) {
                        Icon(Icons.Default.SkipNext, contentDescription = "Next")
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    IconButton(onClick = { /* TODO: Volume */ }) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Volume")
                    }
                    
                    IconButton(onClick = { /* TODO: Fullscreen */ }) {
                        Icon(Icons.Default.Fullscreen, contentDescription = "Fullscreen")
                    }
                }
            }
        }
    }
}

private fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

private suspend fun openInExternalPlayer(url: String) {
    try {
        withContext(Dispatchers.IO) {
            when {
                Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE) -> {
                    Desktop.getDesktop().browse(URI(url))
                }
                System.getProperty("os.name").lowercase().contains("win") -> {
                    Runtime.getRuntime().exec("cmd /c start \"\" \"$url\"")
                }
                System.getProperty("os.name").lowercase().contains("mac") -> {
                    Runtime.getRuntime().exec("open \"$url\"")
                }
                else -> {
                    Runtime.getRuntime().exec("xdg-open \"$url\"")
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}