package com.lagradost.cloudstream3.desktop.providers

import com.lagradost.cloudstream3.*
import kotlinx.coroutines.delay

/**
 * Mock provider for demonstrating desktop functionality
 * Replace with actual providers from the library
 */
class MockStreamProvider : MainAPI() {
    override var name = "Mock Provider"
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)
    override val hasMainPage = true
    
    private val mockMovies = listOf(
        "The Matrix", "Inception", "Interstellar", "The Dark Knight", "Pulp Fiction",
        "The Godfather", "Schindler's List", "The Shawshank Redemption", "Forrest Gump",
        "Fight Club", "Goodfellas", "The Departed", "Casino", "Scarface"
    )
    
    private val mockTvShows = listOf(
        "Breaking Bad", "Game of Thrones", "The Sopranos", "The Wire", "Better Call Saul",
        "Stranger Things", "The Office", "Friends", "House", "Lost"
    )
    
    override val mainPage = mainPageOf(
        "movies" to "Movies",
        "tvshows" to "TV Shows",
        "trending" to "Trending"
    )
    
    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        delay(100) // Simulate network delay
        
        val items = when (request.data) {
            "movies" -> mockMovies.take(10).mapIndexed { index, title ->
                MovieSearchResponse(
                    name = title,
                    url = "movie_${index}",
                    apiName = this.name,
                    type = TvType.Movie,
                    posterUrl = "",
                    year = 2020 + (index % 4)
                )
            }
            "tvshows" -> mockTvShows.take(10).mapIndexed { index, title ->
                TvSeriesSearchResponse(
                    name = title,
                    url = "tv_${index}",
                    apiName = this.name,
                    type = TvType.TvSeries,
                    posterUrl = "",
                    year = 2018 + (index % 6)
                )
            }
            "trending" -> (mockMovies + mockTvShows).shuffled().take(15).mapIndexed { index, title ->
                MovieSearchResponse(
                    name = title,
                    url = "trending_${index}",
                    apiName = this.name,
                    type = if (index % 2 == 0) TvType.Movie else TvType.TvSeries,
                    posterUrl = "",
                    year = 2019 + (index % 5)
                )
            }
            else -> emptyList()
        }
        
        return HomePageResponse(
            items = listOf(HomePageList(request.name, items)),
            hasNext = false
        )
    }
    
    override suspend fun search(query: String): List<SearchResponse> {
        delay(200) // Simulate network delay
        
        val allContent = mockMovies + mockTvShows
        val filtered = allContent.filter { 
            it.lowercase().contains(query.lowercase()) 
        }
        
        return filtered.mapIndexed { index, title ->
            MovieSearchResponse(
                name = title,
                url = "search_${index}",
                apiName = this.name,
                type = if (mockMovies.contains(title)) TvType.Movie else TvType.TvSeries,
                posterUrl = "",
                year = 2015 + (index % 9)
            )
        }
    }
    
    override suspend fun load(url: String): LoadResponse? {
        delay(150) // Simulate network delay
        
        return when {
            url.startsWith("movie_") -> {
                val index = url.removePrefix("movie_").toIntOrNull() ?: 0
                val title = mockMovies.getOrNull(index) ?: "Unknown Movie"
                
                MovieLoadResponse(
                    name = title,
                    url = url,
                    apiName = this.name,
                    type = TvType.Movie,
                    dataUrl = "stream_$url",
                    posterUrl = "",
                    year = 2020 + (index % 4),
                    plot = "A gripping tale of $title with amazing visuals and storytelling.",
                    duration = 120 + (index * 10)
                )
            }
            url.startsWith("tv_") -> {
                val index = url.removePrefix("tv_").toIntOrNull() ?: 0
                val title = mockTvShows.getOrNull(index) ?: "Unknown TV Show"
                
                TvSeriesLoadResponse(
                    name = title,
                    url = url,
                    apiName = this.name,
                    type = TvType.TvSeries,
                    episodes = listOf(
                        Episode(
                            data = "episode_${url}_1",
                            name = "Episode 1",
                            episode = 1,
                            season = 1
                        ),
                        Episode(
                            data = "episode_${url}_2", 
                            name = "Episode 2",
                            episode = 2,
                            season = 1
                        )
                    ),
                    posterUrl = "",
                    year = 2018 + (index % 6),
                    plot = "$title is an acclaimed series with compelling characters and storylines."
                )
            }
            else -> null
        }
    }
    
    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        delay(300) // Simulate extraction delay
        
        // Mock video links - in real implementation, these would be actual streaming URLs
        callback.invoke(
            ExtractorLink(
                source = this.name,
                name = "1080p",
                url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                referer = "",
                quality = 1080
            )
        )
        
        callback.invoke(
            ExtractorLink(
                source = this.name,
                name = "720p", 
                url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                referer = "",
                quality = 720
            )
        )
        
        return true
    }
}