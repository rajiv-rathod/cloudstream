package com.lagradost.cloudstream3.desktop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lagradost.cloudstream3.desktop.ui.components.MovieCard

@Composable
fun SearchScreen(
    query: String,
    onVideoClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (query.isNotEmpty()) {
            Text(
                text = "Search results for: \"$query\"",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                contentPadding = PaddingValues(0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(20) { index ->
                    MovieCard(
                        title = "Search Result ${index + 1}",
                        imageUrl = "",
                        onClick = { onVideoClick("https://example.com/search${index + 1}") }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Start typing to search for movies and TV shows",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}