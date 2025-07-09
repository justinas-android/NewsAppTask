package com.example.newsapp.utils.analytics

interface AnalyticsTracker {
    fun logEvent(category: String, action: String, label: String)
}
