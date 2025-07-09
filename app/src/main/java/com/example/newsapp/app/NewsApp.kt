package com.example.newsapp.app

import android.app.Application
import com.example.newsapp.utils.analytics.AnalyticsTracker
import com.example.newsapp.utils.analytics.Crashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NewsApp: Application() {
    @Inject
    lateinit var crashlytics: Crashlytics

    @Inject
    lateinit var analyticsTracker: AnalyticsTracker

    override fun onCreate() {
        super.onCreate()
        crashlytics.initCrashlytics()
        analyticsTracker
    }
}
