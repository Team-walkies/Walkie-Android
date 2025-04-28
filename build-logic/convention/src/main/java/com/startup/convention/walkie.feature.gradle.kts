import com.startup.convention.walkie.configureAndroidCompose

plugins {
    id("walkie.android.library")
}
configureAndroidCompose()

android {
    defaultConfig {
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }

        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}
dependencies {
    add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
    add("androidTestImplementation", libs.findLibrary("androidx.espresso.core").get())
    add("implementation", project(":core:domain"))
    add("implementation", project(":core:common"))
    add("implementation", project(":core:design-system"))
    add("implementation", project(":core:resource"))
    add("implementation", project(":navigation"))
}