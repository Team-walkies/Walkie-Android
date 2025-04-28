plugins {
    alias(libs.plugins.walkie.android.library)
    alias(libs.plugins.walkie.compose.library)
}

android {
    namespace = "com.startup.navigation"

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":core:design-system"))
}