import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.walkie.android.library)
    alias(libs.plugins.walkie.firebase)
}

android {
    namespace = "com.startup.data"
    buildFeatures.buildConfig = true

    defaultConfig {
        buildConfigField("String", "BASE_URL", getProperty("BASE_URL"))
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:resource"))
    implementation(project(":core:common"))
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