package com.example.newsapp.utils.analytics

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class AnalyticsTrackerImpl @Inject constructor(
    private val analytics: FirebaseAnalytics
) : AnalyticsTracker {

    companion object {
        private const val LABEL = "label"
        private const val ACTION = "action"
    }

    override fun logEvent(category: String, action: String, label: String) {
        synchronized(AnalyticsTracker::class.java) {
            logFirebaseAnalyticsEvent(category, action, label)
        }
    }

    private fun logFirebaseAnalyticsEvent(category: String, action: String, label: String) {
        val bundle = Bundle().apply {
            putString(ACTION, action)
            putString(LABEL, label)
        }

        analytics.logEvent(category, bundle)
        Log.d("AnalyticsTracker", "$category: $bundle ")
    }
}
