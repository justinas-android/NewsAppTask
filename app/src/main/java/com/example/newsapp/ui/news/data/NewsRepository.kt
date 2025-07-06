package com.example.newsapp.ui.news.data

import com.example.newsapp.api.ArticleResponse
import com.example.newsapp.api.NewsResponse
import com.example.newsapp.network.NewsService
import com.example.newsapp.ui.news.models.Article
import com.example.newsapp.ui.news.models.News
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val service: NewsService
) {
    suspend fun getTopHeadlines(
        country: String,
        category: String
    ): News = service.getTopHeadlines(
        country = country,
        category = category
    ).mapToNews()

    private fun NewsResponse.mapToNews(): News {
        return News(
            status = status.orEmpty(),
            articles = articles.orEmpty().mapToArticles()
        )
    }

    private fun List<ArticleResponse>.mapToArticles(): List<Article> {
        return map { article ->
            Article(
                title = article.title.orEmpty(),
                author = article.author.orEmpty(),
                description = article.description.orEmpty(),
                url = article.url.orEmpty(),
                urlToImage = article.urlToImage.orEmpty(),
                publishedAt = article.publishedAt ?: Date()
            )
        }
    }
}
