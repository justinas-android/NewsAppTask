package com.example.newsapp.utils.analytics

interface Crashlytics {
    fun initCrashlytics()

    fun logException(throwable: Throwable)

    fun logException(throwable: Throwable, message: String)

    fun logMessage(message: String)
}
