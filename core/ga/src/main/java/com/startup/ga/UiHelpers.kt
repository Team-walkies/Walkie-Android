package com.startup.ga

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    object : AnalyticsHelper {
        override fun logEvent(event: AnalyticsEvent) {
            Log.d("LMH", "FAKE EVENT! $event")
        }
    }
}

/**
 * 화면 진입에 대한 이벤트를 트래킹합니다.
 */
@Composable
fun TrackScreenViewEvent(
    eventName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logEvent(eventName)
    onDispose {}
}