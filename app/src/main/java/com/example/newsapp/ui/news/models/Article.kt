package com.example.newsapp.ui.news.models

import java.util.Date

data class Article(
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: Date
)
