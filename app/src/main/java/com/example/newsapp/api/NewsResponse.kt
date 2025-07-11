package com.example.newsapp.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsResponse(
    val status: String? = null,
    val articles: List<ArticleResponse>? = null
)
