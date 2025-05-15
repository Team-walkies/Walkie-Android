plugins {
    alias(libs.plugins.walkie.android.library)
    alias(libs.plugins.walkie.compose.library)
}

android {
    namespace = "com.startup.model"
}
dependencies {
    implementation(project(":core:resource"))
    implementation(project(":core:design-system"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
}