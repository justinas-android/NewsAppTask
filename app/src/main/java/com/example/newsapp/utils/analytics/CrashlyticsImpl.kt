package com.example.newsapp.utils.analytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class CrashlyticsImpl @Inject constructor(
    private val crashlytics: FirebaseCrashlytics
) : Crashlytics {
    override fun initCrashlytics() {
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
    }

    override fun logException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun logException(throwable: Throwable, message: String) {
        crashlytics.log(message)
        crashlytics.recordException(throwable)
    }

    override fun logMessage(message: String) {
        crashlytics.log(message)
    }
}
