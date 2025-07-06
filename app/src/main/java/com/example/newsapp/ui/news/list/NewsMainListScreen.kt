package com.example.newsapp.ui.news.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.newsapp.ui.designsystem.widgets.LoadingIndicator
import com.example.newsapp.ui.news.models.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsMainListScreen(
    viewModel: NewsMainListViewModel = hiltViewModel(),
    onArticleClicked: (Article) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val refreshState = rememberPullToRefreshState()

    LaunchedEffect(key1 = Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is NewsMainListViewAction.ShowError -> {
                    print("Ui action ShowError ${action.message}")
                }
                is NewsMainListViewAction.ShowArticleDetails -> {
                    onArticleClicked(action.article)
                }
            }
        }
    }

    if (state.isLoading) {
        LoadingIndicator()
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.onRefresh() },
        state = refreshState
    ) {
        Layout(
            state = state,
            onArticleClicked = viewModel::onArticleClicked
        )
    }
}

@Composable
fun Layout(
    state: NewsMainListViewState,
    onArticleClicked: (Article) -> Unit
) {
    LazyColumn {
        items(state.articles) { article ->
            ArticleItem(
                article = article,
                onArticleClicked = { article ->
                    onArticleClicked(article)
                }
            )
        }
    }
}

@Composable
fun ArticleItem(
    article: Article,
    onArticleClicked: (Article) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onArticleClicked(article) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp),
                model = article.urlToImage,
                contentDescription = "Top Headlines Image"
            )

            Column(
                modifier = Modifier.height(100.dp)
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.publishedAt.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
