package com.example.newsapp.ui.viewmodel

import ApiClient
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
import shared.models.Article
import shared.models.NewsResponse

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






