package com.lagradost.cloudstream3.desktop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lagradost.cloudstream3.desktop.ui.components.MovieCard
import com.lagradost.cloudstream3.desktop.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collectAsState

@Composable
fun SearchScreen(
    query: String,
    viewModel: MainViewModel,
    onVideoClick: (String) -> Unit
) {
    val searchState by viewModel.searchState.collectAsState()
    
    // Trigger search when query changes
    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            viewModel.search(query)
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (query.isNotEmpty()) {
            Text(
                text = "Search results for: \"$query\"",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    searchState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    searchState.error != null -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error: ${searchState.error}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.search(query) }) {
                                Text("Retry")
                            }
                        }
                    }
                    searchState.results.isNotEmpty() -> {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(160.dp),
                            contentPadding = PaddingValues(0.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(searchState.results) { searchResponse ->
                                MovieCard(
                                    title = searchResponse.name,
                                    imageUrl = searchResponse.posterUrl ?: "",
                                    onClick = { 
                                        viewModel.loadContent(searchResponse.url) { loadResponse ->
                                            if (loadResponse != null) {
                                                // For now, just pass the data URL
                                                val dataUrl = when (loadResponse) {
                                                    is com.lagradost.cloudstream3.MovieLoadResponse -> loadResponse.dataUrl
                                                    is com.lagradost.cloudstream3.TvSeriesLoadResponse -> loadResponse.episodes.firstOrNull()?.data
                                                    else -> null
                                                }
                                                dataUrl?.let { onVideoClick(it) }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                    else -> {
                        Text(
                            text = "No results found",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Start typing to search for movies and TV shows",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}