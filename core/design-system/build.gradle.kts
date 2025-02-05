plugins {
    alias(libs.plugins.walkie.android.library)
}

android {
    namespace = "com.startup.design_system"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
}