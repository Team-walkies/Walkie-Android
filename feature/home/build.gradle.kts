plugins {
    alias(libs.plugins.walkie.android.feature)
}

android {
    namespace = "com.startup.home"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

}

dependencies {
    implementation(project(":feature:stepcounter"))
}