package com.example.newsapp.news.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsDetailsViewState())
    val uiState: StateFlow<NewsDetailsViewState> = _uiState

    private val _actions = Channel<NewsDetailsViewAction>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()

    init {

    }
}

data class NewsDetailsViewState(
    val isLoading: Boolean = false,
//    val article: Article
)

sealed class NewsDetailsViewAction {
    data class ShowError(val message: String) : NewsDetailsViewAction()
}
