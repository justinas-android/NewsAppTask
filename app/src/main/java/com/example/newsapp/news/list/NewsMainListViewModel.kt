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

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState

    private val _actions = Channel<NewsUiAction>(Channel.BUFFERED)
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
                        _actions.send(NewsUiAction.ShowError(error.message))
                    },
                )
        }
    }
}

data class NewsUiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList()
)

sealed class NewsUiAction {
    data class ShowError(val message: String) : NewsUiAction()
}
