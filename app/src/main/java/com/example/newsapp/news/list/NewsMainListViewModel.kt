package com.example.newsapp.news.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.news.list.domain.GetTopHeadlinesInteractor
import com.example.newsapp.news.models.Article
import com.example.newsapp.utils.collectLatestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsMainListViewModel @Inject constructor(
    private val getTopHeadlinesInteractor: GetTopHeadlinesInteractor
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
        viewModelScope.launch {
            getTopHeadlinesInteractor(
                country = HARDCODED_COUNTRY,
                category = HARDCODED_CATEGORY
            )
                .collectLatestResult(
                    onSuccess = { result ->
                        _uiState.value = _uiState.value.copy(
                            articles = result.articles
                        )
                    },
                    onError = { error ->
                        _actions.send(NewsMainListViewAction.ShowError(error.message))
                    },
                )
        }
    }

    fun onArticleClicked(article: Article) {
        viewModelScope.launch {
            _actions.send(NewsMainListViewAction.ShowArticleDetails(article))
        }
    }
}

data class NewsMainListViewState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList()
)

sealed class NewsMainListViewAction {
    data class ShowError(val message: String) : NewsMainListViewAction()
    data class ShowArticleDetails(val article: Article) : NewsMainListViewAction()
}
