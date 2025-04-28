import com.startup.convention.walkie.configureCoroutineAndroid
import com.startup.convention.walkie.configureHiltAndroid
import com.startup.convention.walkie.configureKotlin
import com.startup.convention.walkie.configureKotlinAndroid


plugins {
    id("com.android.library")
}

android {
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    defaultConfig {
        consumerProguardFiles("$rootDir/app/proguard-rules.pro")
    }
}

configureKotlinAndroid()
configureKotlin()
configureHiltAndroid()
configureCoroutineAndroid()

dependencies {
    add("implementation", libs.findLibrary("androidx.core.ktx").get())
}