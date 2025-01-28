package com.startup.convention.walkie

import androidExtension
import libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose() {
    pluginManager.apply {
        apply("org.jetbrains.kotlin.plugin.compose")
    }
    androidExtension.apply {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.1"
        }

        dependencies {
            val bom = libs.findLibrary("androidx.compose.bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("implementation",libs.findLibrary("androidx.activity.compose").get())
            add("implementation",libs.findLibrary("androidx.ui").get())
            add("implementation",libs.findLibrary("androidx.ui.graphics").get())
            add("implementation",libs.findLibrary("androidx.ui.tooling.preview").get())
            add("implementation",libs.findLibrary("androidx.material3").get())
            add("implementation",libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
            add("implementation",libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
            add("implementation",libs.findLibrary("coil.kt.compose").get())
            add("implementation",libs.findLibrary("androidx.navigation.compose").get())
            add("implementation",libs.findLibrary("androidx.hilt.navigation.compose").get())
            add("debugImplementation", libs.findLibrary("androidx.ui.tooling").get())
        }
    }
}