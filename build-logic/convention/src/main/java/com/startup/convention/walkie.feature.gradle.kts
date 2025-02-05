import com.startup.convention.walkie.configureAndroidCompose

plugins {
    id("walkie.android.library")
}
configureAndroidCompose()

dependencies {
    add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
    add("androidTestImplementation", libs.findLibrary("androidx.espresso.core").get())
    add("implementation", project(":core:domain"))
    add("implementation", project(":core:design-system"))
}