package com.example.newsapp.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsapp.ui.viewmodel.ApiClient
import com.example.newsapp.ui.viewmodel.NewsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import shared.models.Article

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val state by viewModel.state.collectAsState() // Collect state from ViewModel

    Column(modifier = Modifier.padding(16.dp)) {
        // Show loading indicator if data is loading
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // Show error message if there's any
        state.error?.let {
            Text(text = it, color = MaterialTheme.colors.error)
        }

        // Display articles if available
        if (state.articles.isNotEmpty()) {
            LazyColumn {
                items(state.articles) { article ->
                    NewsItem(article)
                }
            }
        }
    }
}


@Composable
fun NewsItem(article: Article) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = article.title, style = MaterialTheme.typography.h6)
        Text(text = article.description ?: "No description available", style = MaterialTheme.typography.body2)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = article.url, style = MaterialTheme.typography.body2, color = MaterialTheme.colors.primary)
    }
}

@Preview
@Composable
fun PreviewNewsScreen() {
    // Preview requires a mock ViewModel
    NewsScreen(viewModel = NewsViewModel(ApiClient()))
}
