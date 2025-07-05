package com.example.newsapp.news.models

import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Article(
    val title: String? = null,
    val description: String? = null,
    val urlToImage: String? = null,
    val publishedAt: Date? = null
)
