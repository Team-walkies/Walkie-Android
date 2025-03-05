plugins {
    alias(libs.plugins.walkie.android.feature)
}

android {
    namespace = "com.startup.login"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
}