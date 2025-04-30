package com.startup.convention.walkie

import androidExtension
import com.android.build.api.dsl.ApplicationBuildType
import libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureKotlinAndroid() {
    with(pluginManager) {
        apply("org.jetbrains.kotlin.android")
    }

    androidExtension.apply {
        compileSdk = Const.COMPILE_SDK
        compileOptions {
            sourceCompatibility = Const.JAVA_VERSION
            targetCompatibility = Const.JAVA_VERSION
        }

        defaultConfig {
            minSdk = Const.MIN_SDK

            vectorDrawables.useSupportLibrary = true
            testInstrumentationRunner = "androidx.test.runner.AndroidJunitRunner"
            vectorDrawables {
                useSupportLibrary = true
            }
        }
        buildTypes {
            getByName("release") {
                if (this is ApplicationBuildType) {
                    isDebuggable = false
                }
            }
        }
    }
    dependencies {
        add("implementation", libs.findLibrary("androidx.core.ktx").get())
        add("implementation", libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
        add("implementation", libs.findLibrary("material").get())
        add("testImplementation", libs.findLibrary("junit").get())
    }
}