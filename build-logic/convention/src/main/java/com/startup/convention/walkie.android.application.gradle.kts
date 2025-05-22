import com.startup.convention.walkie.configureAndroidCompose
import com.startup.convention.walkie.configureCoroutineAndroid
import com.startup.convention.walkie.configureFirebaseApp
import com.startup.convention.walkie.configureHiltAndroid
import com.startup.convention.walkie.configureKotlin
import com.startup.convention.walkie.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureKotlin()
configureHiltAndroid()
configureCoroutineAndroid()
configureAndroidCompose()
configureFirebaseApp()

dependencies {
    add("implementation", libs.findLibrary("androidx.core.ktx").get())
    add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
    add("androidTestImplementation", libs.findLibrary("androidx.espresso.core").get())
}