import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.walkie.android.library)
}

android {
    namespace = "com.startup.data"
    buildFeatures.buildConfig = true

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "BASE_URL", getProperty("BASE_URL"))
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:resource"))
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(libs.okhttp)
    implementation(libs.gson)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kakao.login)
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}