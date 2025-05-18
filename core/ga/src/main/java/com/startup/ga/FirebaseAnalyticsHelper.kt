package com.startup.ga

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

internal class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsHelper {

    override fun logEvent(event: AnalyticsEvent) {
        Log.d("LMH", "REAL EVENT! $event")
        firebaseAnalytics.logEvent(event.eventName) {
            for (extra in event.extras) {
                // Key 최대 40자, Value 최대 100 자 (String 기준)
                this.param(
                    key = extra.key.take(40),
                    value = extra.value.take(100),
                )
            }
        }
    }
}