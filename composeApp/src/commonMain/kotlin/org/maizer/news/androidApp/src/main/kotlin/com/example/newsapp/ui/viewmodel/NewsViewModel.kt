package com.example.newsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}



class NewsViewModel(private val apiClient: ApiClient) : ViewModel() {

    private val _state = MutableStateFlow(NewsState())
    val state: StateFlow<NewsState> = _state

    init {
        fetchNews()
    }

    private fun fetchNews() {
        _state.value = NewsState(isLoading = true)

        viewModelScope.launch {
            try {
                val response = apiClient.fetchNews()
                print(" ${response}\"")
                _state.value = NewsState(articles = response.articles)
            } catch (e: Exception) {
                print(" ${e.message}\"")
                logger.error(e) { "Failed to load news: ${e.message}" }

                _state.value = NewsState(error = "Failed to load news: ${e.message}")
            }
        }
    }
}

data class NewsState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)




class ApiClient {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { // Use ContentNegotiation plugin
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    private val apiKey = "be46ef0efea24cd584947a3ecc84a6c6" // Use your actual API key here
    private val baseUrl = "https://newsapi.org/v2/top-headlines?country=us&apiKey=$apiKey"

    // Correct way to fetch and deserialize the response
    suspend fun fetchNews(): NewsResponse {
        val response: HttpResponse = client.get(baseUrl)
        return response.body() // Deserialize into NewsResponse
    }
}



@Serializable
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

@Serializable
data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

@Serializable
data class Source(
    val id: String?,
    val name: String
)
