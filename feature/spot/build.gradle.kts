plugins {
    alias(libs.plugins.walkie.android.feature)
}

android {
    namespace = "com.startup.spot"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}