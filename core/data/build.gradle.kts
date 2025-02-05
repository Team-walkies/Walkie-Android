plugins {
    alias(libs.plugins.walkie.android.library)
}

android {
    namespace = "com.startup.data"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    project(":core:domain")
}