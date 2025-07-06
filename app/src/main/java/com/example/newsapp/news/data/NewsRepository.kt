package com.example.newsapp.news.data

import com.example.newsapp.api.ArticleResponse
import com.example.newsapp.api.NewsResponse
import com.example.newsapp.network.NewsService
import com.example.newsapp.news.models.Article
import com.example.newsapp.news.models.News
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
    ).mapNewsResponse()

    private fun NewsResponse.mapNewsResponse(): News {
        return News(
            status = status.orEmpty(),
            articles = articles.orEmpty().mapArticleResponse()
        )
    }

    private fun List<ArticleResponse>.mapArticleResponse(): List<Article> {
        return map { article ->
            Article(
                title = article.title.orEmpty(),
                description = article.description.orEmpty(),
                urlToImage = article.urlToImage.orEmpty(),
                publishedAt = article.publishedAt ?: Date()
            )
        }
    }
}
