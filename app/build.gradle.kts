import com.startup.convention.walkie.Const

plugins {
    alias(libs.plugins.walkie.android.application)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.startup.walkie"
    defaultConfig {
        applicationId = "com.startup.walkie"
        versionCode = 1
        versionName = "1.0"
        targetSdk = Const.TARGET_SDK
    }

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":feature:home"))
    implementation(project(":feature:login"))
    implementation(project(":feature:stepcounter"))
    implementation(project(":core:design-system"))
    implementation(project(":core:data"))
}