plugins {
    alias(libs.plugins.walkie.android.library)
    alias(libs.plugins.walkie.compose.library)
}

android {
    buildFeatures.buildConfig = true
    namespace = "com.startup.common"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:design-system"))
    implementation(project(":core:resource"))
    implementation(libs.lottie)
}