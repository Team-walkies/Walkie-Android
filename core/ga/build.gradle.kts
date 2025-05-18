plugins {
    alias(libs.plugins.walkie.android.library)
    alias(libs.plugins.walkie.firebase)
    alias(libs.plugins.walkie.compose.library)
    alias(libs.plugins.walkie.hilt)
}

android {
    namespace = "com.startup.ga"
    buildFeatures {
        buildConfig = true
    }
}