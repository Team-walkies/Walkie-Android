package com.startup.convention.walkie

import libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureHiltAndroid() {

    with(pluginManager) {
        apply("com.google.devtools.ksp")
        apply("dagger.hilt.android.plugin")
    }

    dependencies {
        add("implementation", libs.findLibrary("hilt.android").get())
        add("ksp", libs.findLibrary("hilt.compiler").get())
    }
}

internal fun Project.configureHiltKotlin() {
    pluginManager.apply {
        apply("com.google.devtools.ksp")
    }

    dependencies {
        add("implementation", libs.findLibrary("hilt.core").get())
        add("ksp", libs.findLibrary("hilt.compiler").get())
    }
}