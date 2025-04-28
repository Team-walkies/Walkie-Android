plugins {
    alias(libs.plugins.walkie.android.feature)
}

android {
    namespace = "com.startup.home"
}

dependencies {
    implementation(project(":feature:stepcounter"))
}