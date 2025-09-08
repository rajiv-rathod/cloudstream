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
fun LibraryScreen(
    onVideoClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Your Library",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(0.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(15) { index ->
                MovieCard(
                    title = "Library Item ${index + 1}",
                    imageUrl = "",
                    onClick = { onVideoClick("https://example.com/library${index + 1}") }
                )
            }
        }
    }
}