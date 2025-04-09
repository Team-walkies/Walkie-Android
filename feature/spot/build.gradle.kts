import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.walkie.android.feature)
}

android {
    namespace = "com.startup.spot"
    buildFeatures.buildConfig = true

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "BASE_SPOT_URL", getProperty("BASE_SPOT_URL"))
    }
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}