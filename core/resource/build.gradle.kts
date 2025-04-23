plugins {
    alias(libs.plugins.walkie.android.library)
}

android {
    namespace = "com.startup.resource"
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
}