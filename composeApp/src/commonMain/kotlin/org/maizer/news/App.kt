package org.maizer.news


import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import com.example.newsapp.ui.screens.NewsScreen
import com.example.newsapp.ui.viewmodel.ApiClient
import com.example.newsapp.ui.viewmodel.NewsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    // Instantiate the ViewModel
    val viewModel = remember { NewsViewModel(ApiClient()) }

    MaterialTheme {
        // Pass the viewModel to NewsScreen
        NewsScreen(viewModel = viewModel)
    }
}
