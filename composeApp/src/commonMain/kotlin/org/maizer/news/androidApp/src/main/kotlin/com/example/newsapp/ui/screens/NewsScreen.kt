package com.example.newsapp.ui.screens

import ApiClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.example.newsapp.ui.viewmodel.NewsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import shared.models.Article
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally ) // Align in the center (this should work correctly now)
                    .size(64.dp),
                color = MaterialTheme.colors.primary,
                strokeWidth = 6.dp
            )
        }

        state.error?.let {
            Text(text = it, color = MaterialTheme.colors.error)
        }

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
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { uriHandler.openUri(article.url) },
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = article.title,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            Spacer(Modifier.height(4.dp))

            Text(
                text = article.description ?: "No description available",
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3
            )
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = article.source.name,
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = article.publishedAt,
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewNewsScreen() {
    NewsScreen(viewModel = NewsViewModel(ApiClient()))
}
