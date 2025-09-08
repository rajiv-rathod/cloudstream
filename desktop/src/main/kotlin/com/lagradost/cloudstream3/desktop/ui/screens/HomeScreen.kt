package com.lagradost.cloudstream3.desktop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lagradost.cloudstream3.desktop.ui.components.MovieCard

@Composable
fun HomeScreen(
    onVideoClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Recently Added",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(10) { index ->
                    MovieCard(
                        title = "Movie ${index + 1}",
                        imageUrl = "",
                        onClick = { onVideoClick("https://example.com/movie${index + 1}") }
                    )
                }
            }
        }
        
        item {
            Text(
                text = "Trending Movies",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(10) { index ->
                    MovieCard(
                        title = "Trending ${index + 1}",
                        imageUrl = "",
                        onClick = { onVideoClick("https://example.com/trending${index + 1}") }
                    )
                }
            }
        }
        
        item {
            Text(
                text = "TV Shows",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(10) { index ->
                    MovieCard(
                        title = "TV Show ${index + 1}",
                        imageUrl = "",
                        onClick = { onVideoClick("https://example.com/tvshow${index + 1}") }
                    )
                }
            }
        }
    }
}