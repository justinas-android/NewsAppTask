package com.example.newsapp.ui.news.list

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.example.newsapp.R
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
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is NewsMainListViewAction.ShowError -> {
                    Toast.makeText(context, action.message, Toast.LENGTH_LONG).show()
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
private fun Layout(
    state: NewsMainListViewState,
    onArticleClicked: (Article) -> Unit
) {
    LazyColumn {
        items(state.articles) { article ->
            ArticleCard(
                article = article,
                onArticleClicked = { article ->
                    onArticleClicked(article)
                }
            )
        }
    }
}

@Composable
private fun ArticleCard(
    article: Article,
    onArticleClicked: (Article) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .clickable { onArticleClicked(article) }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.3f)
                    .width(120.dp)
            ) {
                val imageData = article.urlToImage.ifEmpty {
                    R.drawable.ic_empty_blank_file_200dp
                }

                AsyncImage(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(imageData)
                        .scale(Scale.FILL)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Top Headlines Image",
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
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
