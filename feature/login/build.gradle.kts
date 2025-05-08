plugins {
    alias(libs.plugins.walkie.android.feature)
}

android {
    namespace = "com.startup.login"
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kakao.login)
}