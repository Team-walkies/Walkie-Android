plugins {
    alias(libs.plugins.walkie.android.library)
}

android {
    group = "team.walkie"
    namespace = "com.startup.resource"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}