plugins {
    alias(libs.plugins.walkie.kotlin.library)
    id("com.google.devtools.ksp")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}