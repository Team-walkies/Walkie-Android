plugins {
    alias(libs.plugins.walkie.android.library)
    alias(libs.plugins.walkie.compose.library)
}

android {
    namespace = "com.startup.stepcounter"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":core:design-system"))
}