plugins {
    alias(libs.plugins.walkie.android.library)
}

android {
    namespace = "com.startup.common"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:design-system"))
}