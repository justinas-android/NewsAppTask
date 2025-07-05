package com.example.newsapp.news.data

import com.example.newsapp.api.NewsResponse
import com.example.newsapp.network.NewsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val service: NewsService
) {
    suspend fun getTopHeadlines(
         country: String ,
         category: String
    ): NewsResponse = service.getTopHeadlines(
        country = country,
        category = category
    )
}
