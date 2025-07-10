package com.example.newsapp.ui.news.details

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.newsapp.ui.news.models.Article

@Composable
fun NewsDetailsScreen(
    viewModel: NewsDetailsViewModel = hiltViewModel(),
    article: Article,
    onReadFullArticleClicked: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onLoad(
            article
        )
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is NewsDetailsViewAction.ShowError -> {
                    Toast.makeText(context, action.message, Toast.LENGTH_LONG).show()
                }
                is NewsDetailsViewAction.ShowFullArticle -> {
                    onReadFullArticleClicked(action.url)
                }
            }
        }
    }

    Layout(
        state = state,
        onReadFullArticleClicked = { url ->
            viewModel.onReadFullArticleClicked(url)
        }
    )
}

@Composable
private fun Layout(
    state: NewsDetailsViewState,
    onReadFullArticleClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            model = state.urlToImage,
            contentScale = ContentScale.Crop,
            contentDescription = "Top Headlines Image"
        )

        Content(
            author = state.author,
            title = state.title,
            description = state.description,
            publishedAt = state.publishedAt,
            url = state.url,
            onReadFullArticleClicked = onReadFullArticleClicked
        )
    }
}

@Composable
private fun Content(
    author: String,
    title: String,
    description: String,
    publishedAt: String,
    url: String,
    onReadFullArticleClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            modifier = Modifier.align(Alignment.End),
            text = author,
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic
        )

        Text(
            modifier = Modifier.align(Alignment.End),
            text = publishedAt,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onReadFullArticleClicked(url) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Read Full Article".uppercase(),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
