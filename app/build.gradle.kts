import com.startup.convention.walkie.Const
import java.util.Properties

plugins {
    alias(libs.plugins.walkie.android.application)
    alias(libs.plugins.kotlin.serialization)
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.startup.walkie"
    defaultConfig {
        applicationId = "com.startup.walkie"
        versionCode = 1
        versionName = "1.0"
        targetSdk = Const.TARGET_SDK
        buildConfigField("String", "NATIVE_APP_KEY", properties.getProperty("NATIVE_APP_KEY"))
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":navigation"))
    implementation(project(":feature:home"))
    implementation(project(":feature:login"))
    implementation(project(":feature:spot"))
    implementation(project(":feature:stepcounter"))
    implementation(project(":core:design-system"))
    implementation(project(":core:data"))
    implementation(project(":core:resource"))
    implementation(libs.kakao.login)
}