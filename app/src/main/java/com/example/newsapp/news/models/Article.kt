package com.example.newsapp.news.models

import java.util.Date

data class Article(
    val title: String,
    val description: String,
    val urlToImage: String,
    val publishedAt: Date
)
