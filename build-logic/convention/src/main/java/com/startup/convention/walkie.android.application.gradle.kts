import com.startup.convention.walkie.configureAndroidCompose
import com.startup.convention.walkie.configureCoroutineAndroid
import com.startup.convention.walkie.configureHiltAndroid
import com.startup.convention.walkie.configureKotlin
import com.startup.convention.walkie.configureKotlinAndroid
import gradle.kotlin.dsl.accessors._2fb5859a04200edaf14b854c40b2e363.implementation


plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureKotlin()
configureHiltAndroid()
configureCoroutineAndroid()
configureAndroidCompose()

dependencies {
    add("implementation", libs.findLibrary("androidx.core.ktx").get())
    add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
    add("androidTestImplementation", libs.findLibrary("androidx.espresso.core").get())
}