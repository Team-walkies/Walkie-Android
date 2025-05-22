import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.walkie.android.feature)
}

android {
    namespace = "com.startup.spot"
    buildFeatures.buildConfig = true

    defaultConfig {
        buildConfigField("String", "BASE_SPOT_URL", getProperty("BASE_SPOT_URL"))
        buildConfigField("String", "BASE_SPOT_MODIFY_URL", getProperty("BASE_SPOT_MODIFY_URL"))
    }
}

dependencies {
    implementation(project(":core:ga"))
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}