plugins {
    alias(libs.plugins.walkie.android.library)
    alias(libs.plugins.walkie.firebase)
    alias(libs.plugins.walkie.compose.library)
}

android {
    namespace = "com.startup.ga"
    buildFeatures {
        buildConfig = true
    }
}