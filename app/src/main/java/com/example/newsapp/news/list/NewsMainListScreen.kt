package com.example.newsapp.news.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.newsapp.news.models.Article

@Composable
fun NewsMainListScreen(
    viewModel: NewsMainListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is NewsUiAction.ShowError -> {
                    print("Ui action ShowError ${action.message}")
                }
            }
        }
    }

    Layout(state)
}

@Composable
fun Layout(state: NewsUiState) {
    LazyColumn {
        items(state.articles) { article ->
            ArticleItem(article)
        }
    }
}

@Composable
fun ArticleItem(article: Article) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(100.dp)
                    .width(130.dp),
                model = article.urlToImage,
                contentDescription = "Top Headlines Image"
            )

            Column(
                modifier = Modifier.height(100.dp)
            ) {
                Text(
                    text = article.title ?: "",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.description ?: "",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.publishedAt?.time.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}
