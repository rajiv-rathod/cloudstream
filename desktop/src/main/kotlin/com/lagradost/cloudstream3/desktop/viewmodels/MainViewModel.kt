package com.lagradost.cloudstream3.desktop.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.desktop.providers.MockStreamProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeState(
    val isLoading: Boolean = false,
    val homePages: List<HomePageList> = emptyList(),
    val error: String? = null
)

data class SearchState(
    val isLoading: Boolean = false,
    val results: List<SearchResponse> = emptyList(),
    val error: String? = null
)

class MainViewModel {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    // Initialize providers
    private val providers = listOf<MainAPI>(
        MockStreamProvider()
    )
    
    init {
        // Initialize all providers
        providers.forEach { it.init() }
    }
    
    // Home state
    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()
    
    // Search state
    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()
    
    // Current provider
    var currentProvider: MainAPI by mutableStateOf(providers.first())
        private set
    
    init {
        loadHomePage()
    }
    
    fun loadHomePage() {
        scope.launch {
            _homeState.value = _homeState.value.copy(isLoading = true, error = null)
            
            try {
                val homePages = mutableListOf<HomePageList>()
                
                // Load content from main page requests
                for (request in currentProvider.mainPage) {
                    try {
                        val response = currentProvider.getMainPage(1, request)
                        homePages.addAll(response.items)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    homePages = homePages,
                    error = null
                )
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
    
    fun search(query: String) {
        if (query.isBlank()) {
            _searchState.value = SearchState()
            return
        }
        
        scope.launch {
            _searchState.value = _searchState.value.copy(isLoading = true, error = null)
            
            try {
                val results = currentProvider.search(query)
                _searchState.value = _searchState.value.copy(
                    isLoading = false,
                    results = results,
                    error = null
                )
            } catch (e: Exception) {
                _searchState.value = _searchState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Search failed"
                )
            }
        }
    }
    
    fun loadContent(url: String, callback: (LoadResponse?) -> Unit) {
        scope.launch {
            try {
                val result = currentProvider.load(url)
                callback(result)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }
    
    fun loadVideoLinks(data: String, callback: (List<ExtractorLink>) -> Unit) {
        scope.launch {
            try {
                val links = mutableListOf<ExtractorLink>()
                
                currentProvider.loadLinks(
                    data = data,
                    isCasting = false,
                    subtitleCallback = { /* Handle subtitles */ },
                    callback = { link ->
                        links.add(link)
                    }
                )
                
                callback(links)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(emptyList())
            }
        }
    }
    
    fun changeProvider(provider: MainAPI) {
        currentProvider = provider
        loadHomePage()
    }
    
    fun getProviders(): List<MainAPI> = providers
    
    fun dispose() {
        scope.cancel()
    }
}