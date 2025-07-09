package com.example.newsapp.ui.news.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.ui.news.list.domain.GetTopHeadlinesInteractor
import com.example.newsapp.ui.news.models.Article
import com.example.newsapp.utils.analytics.AnalyticsTracker
import com.example.newsapp.utils.collectResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsMainListViewModel @Inject constructor(
    private val getTopHeadlinesInteractor: GetTopHeadlinesInteractor,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    companion object {
        private const val HARDCODED_COUNTRY = "us"
        private const val HARDCODED_CATEGORY = "business"
    }

    private val _uiState = MutableStateFlow(NewsMainListViewState())
    val uiState: StateFlow<NewsMainListViewState> = _uiState

    private val _actions = Channel<NewsMainListViewAction>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()

    init {
        onLoad()
    }

    fun onRefresh() {
        _uiState.value = _uiState.value.copy(isRefreshing = true)
        onLoad()
    }

    fun onArticleClicked(article: Article) {
        analyticsTracker.logEvent(
            category = "articles",
            action = "click",
            label = article.title
        )
        viewModelScope.launch {
            _actions.send(NewsMainListViewAction.ShowArticleDetails(article))
        }
    }

    private fun onLoad() {
        viewModelScope.launch {
            getTopHeadlinesInteractor(
                country = HARDCODED_COUNTRY,
                category = HARDCODED_CATEGORY
            )
                .collectResult(
                    onSuccess = { result ->
                        _uiState.value = _uiState.value.copy(
                            articles = result.articles
                        )
                    },
                    onError = { error ->
                        _actions.send(NewsMainListViewAction.ShowError(error.message))
                    },
                    finally = {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                )
        }
    }
}

data class NewsMainListViewState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = true
)

sealed class NewsMainListViewAction {
    data class ShowError(val message: String) : NewsMainListViewAction()
    data class ShowArticleDetails(val article: Article) : NewsMainListViewAction()
}
