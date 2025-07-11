package com.example.newsapp.network

import com.example.newsapp.api.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("category") category: String
    ): NewsResponse
}
