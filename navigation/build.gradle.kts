plugins {
    alias(libs.plugins.walkie.android.library)
}

android {
    namespace = "com.startup.navigation"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

}

dependencies {
    implementation(project(":feature:home"))
    implementation(project(":feature:login"))
}