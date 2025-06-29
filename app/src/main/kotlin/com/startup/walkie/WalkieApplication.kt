package com.startup.walkie

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WalkieApplication : Application() {

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initializeLogging()
        initialRemoteConfigSetting()
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
//        fetchFirebaseMessagingToken()
    }

    private fun initializeLogging() {
        if (!BuildConfig.DEBUG) {
            Firebase.crashlytics.isCrashlyticsCollectionEnabled = true
            Firebase.analytics.setAnalyticsCollectionEnabled(true)
        } else {
            Firebase.crashlytics.isCrashlyticsCollectionEnabled = false
            Firebase.analytics.setAnalyticsCollectionEnabled(false)
        }
    }

    private fun initialRemoteConfigSetting() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }

        runCatching {
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }

//    private fun fetchFirebaseMessagingToken(){
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Printer.d("LMH", "Fetching FCM registration token failed ${task.exception}")
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            Printer.d("LMH", token)
//        })
//    }
}
