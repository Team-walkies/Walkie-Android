plugins {
    alias(libs.plugins.walkie.android.library)
    alias(libs.plugins.walkie.compose.library)
}

android {
    namespace = "com.startup.design_system"
}

dependencies {
    implementation(project(":core:resource"))
    implementation(libs.lottie)
}