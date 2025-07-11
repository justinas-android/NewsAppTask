package com.example.newsapp.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY = "65100d7a53da4a4c847b9ec00b31a699"

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url

        val url = originalUrl.newBuilder()
            .addQueryParameter("apiKey", API_KEY)
            .build()

        val request = original.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}
