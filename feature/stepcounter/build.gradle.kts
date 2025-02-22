plugins {
    alias(libs.plugins.walkie.android.feature)
}

android {
    namespace = "com.startup.stepcounter"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
}