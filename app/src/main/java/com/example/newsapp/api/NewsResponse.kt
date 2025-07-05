package com.example.newsapp.api

import com.example.newsapp.news.models.Article
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsResponse(
    val status: String,
    val articles: List<Article>
)
