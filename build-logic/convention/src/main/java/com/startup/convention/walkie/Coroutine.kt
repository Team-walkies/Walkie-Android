package com.startup.convention.walkie

import libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureCoroutineAndroid() {
    configureCoroutineKotlin()

    dependencies {
        add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
    }
}

internal fun Project.configureCoroutineKotlin() {
    dependencies {
        add("implementation", libs.findLibrary("kotlinx.coroutines.core").get())
        add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
    }
}