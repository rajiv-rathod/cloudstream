package com.lagradost.cloudstream3.desktop.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lagradost.cloudstream3.desktop.ui.components.NavigationBar
import com.lagradost.cloudstream3.desktop.ui.components.SearchBar
import com.lagradost.cloudstream3.desktop.ui.components.VideoPlayerDialog
import com.lagradost.cloudstream3.desktop.ui.screens.*
import com.lagradost.cloudstream3.desktop.viewmodels.MainViewModel

enum class Screen {
    HOME, SEARCH, LIBRARY, SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesktopApp() {
    val viewModel = remember { MainViewModel() }
    
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var searchQuery by remember { mutableStateOf("") }
    var showVideoPlayer by remember { mutableStateOf(false) }
    var currentVideoData by remember { mutableStateOf("") }
    
    DisposableEffect(Unit) {
        onDispose {
            viewModel.dispose()
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Side Navigation
            NavigationBar(
                currentScreen = currentScreen,
                onScreenChange = { currentScreen = it },
                modifier = Modifier.width(240.dp)
            )
            
            // Main Content
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {
                // Top Bar with Search
                TopAppBar(
                    title = { 
                        Text(
                            text = when(currentScreen) {
                                Screen.HOME -> "Home"
                                Screen.SEARCH -> "Search"
                                Screen.LIBRARY -> "Library"
                                Screen.SETTINGS -> "Settings"
                            }
                        )
                    },
                    actions = {
                        if (currentScreen == Screen.HOME || currentScreen == Screen.SEARCH) {
                            SearchBar(
                                query = searchQuery,
                                onQueryChange = { searchQuery = it },
                                onSearch = { 
                                    currentScreen = Screen.SEARCH
                                },
                                modifier = Modifier.width(300.dp)
                            )
                        }
                    }
                )
                
                // Screen Content
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)
                ) {
                    when (currentScreen) {
                        Screen.HOME -> HomeScreen(
                            viewModel = viewModel,
                            onVideoClick = { data ->
                                // Load video links and play
                                viewModel.loadVideoLinks(data) { links ->
                                    if (links.isNotEmpty()) {
                                        currentVideoData = links.first().url
                                        showVideoPlayer = true
                                    }
                                }
                            }
                        )
                        Screen.SEARCH -> SearchScreen(
                            query = searchQuery,
                            viewModel = viewModel,
                            onVideoClick = { data ->
                                // Load video links and play
                                viewModel.loadVideoLinks(data) { links ->
                                    if (links.isNotEmpty()) {
                                        currentVideoData = links.first().url
                                        showVideoPlayer = true
                                    }
                                }
                            }
                        )
                        Screen.LIBRARY -> LibraryScreen(
                            onVideoClick = { data ->
                                currentVideoData = data
                                showVideoPlayer = true
                            }
                        )
                        Screen.SETTINGS -> SettingsScreen()
                    }
                }
            }
        }
        
        // Video Player Dialog
        if (showVideoPlayer) {
            VideoPlayerDialog(
                videoUrl = currentVideoData,
                onDismiss = { 
                    showVideoPlayer = false
                    currentVideoData = ""
                }
            )
        }
    }
}