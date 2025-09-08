package com.lagradost.cloudstream3.desktop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lagradost.cloudstream3.desktop.ui.components.MovieCard
import com.lagradost.cloudstream3.desktop.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collectAsState

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onVideoClick: (String) -> Unit
) {
    val homeState by viewModel.homeState.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            homeState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            homeState.error != null -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: ${homeState.error}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadHomePage() }) {
                        Text("Retry")
                    }
                }
            }
            homeState.homePages.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(homeState.homePages) { homePageList ->
                        Column {
                            Text(
                                text = homePageList.name,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(homePageList.list) { searchResponse ->
                                    MovieCard(
                                        title = searchResponse.name,
                                        imageUrl = searchResponse.posterUrl ?: "",
                                        onClick = { 
                                            viewModel.loadContent(searchResponse.url) { loadResponse ->
                                                if (loadResponse != null) {
                                                    // For now, just pass the data URL
                                                    val dataUrl = when (loadResponse) {
                                                        is MovieLoadResponse -> loadResponse.dataUrl
                                                        is TvSeriesLoadResponse -> loadResponse.episodes.firstOrNull()?.data
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
                    }
                }
            }
            else -> {
                Text(
                    text = "No content available",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}