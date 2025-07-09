package com.example.newsapp.ui.news.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.utils.analytics.AnalyticsTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsDetailsViewState())
    val uiState: StateFlow<NewsDetailsViewState> = _uiState

    private val _actions = Channel<NewsDetailsViewAction>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()

    fun onLoad(
        author: String,
        title: String,
        description: String,
        url: String,
        urlToImage: String,
        publishedAt: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                author = author,
                title = title,
                description = description,
                url = url,
                urlToImage = urlToImage,
                publishedAt = publishedAt
            )
        }
    }

    fun onReadFullArticleClicked(url: String) {
        analyticsTracker.logEvent(
            category = "article_details",
            action = "click_read_more",
            label = uiState.value.title
        )
        viewModelScope.launch {
            _actions.send(NewsDetailsViewAction.ShowFullArticle(url))
        }
    }
}

data class NewsDetailsViewState(
    val author: String = "",
    val title: String = "",
    val description: String = "",
    val url: String = "",
    val urlToImage: String = "",
    val publishedAt: String = ""
)

sealed class NewsDetailsViewAction {
    data class ShowError(val message: String) : NewsDetailsViewAction()
    data class ShowFullArticle(val url: String) : NewsDetailsViewAction()
}
