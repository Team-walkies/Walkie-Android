package com.startup.walkie

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WalkieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initializeLogging()
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
    }
}

fun initializeLogging() {
    if (!BuildConfig.DEBUG) {
        Firebase.crashlytics.isCrashlyticsCollectionEnabled = true
        Firebase.analytics.setAnalyticsCollectionEnabled(true)
    } else {
        Firebase.crashlytics.isCrashlyticsCollectionEnabled = false
        Firebase.analytics.setAnalyticsCollectionEnabled(false)
    }
}