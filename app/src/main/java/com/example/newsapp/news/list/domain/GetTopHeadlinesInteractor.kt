package com.example.newsapp.news.list.domain

import com.example.newsapp.news.data.NewsRepository
import com.example.newsapp.news.models.News
import com.example.newsapp.utils.BaseResult
import com.example.newsapp.utils.Interactor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlinesInteractor @Inject constructor(
    private val newsRepository: NewsRepository
) : Interactor() {

    operator fun invoke(
        country: String,
        category: String
    ): Flow<BaseResult<News>> = wrapCoroutineFlowResult {
        newsRepository.getTopHeadlines(
            country = country,
            category = category
        )
    }
}
