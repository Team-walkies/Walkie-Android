package com.startup.convention.walkie

import libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFirebase() {
    with(pluginManager) {
        apply("com.google.gms.google-services")
        apply("com.google.firebase.crashlytics")
    }

    dependencies {
        add("implementation", platform(libs.findLibrary("firebase.bom").get()))
        add("implementation", libs.findLibrary("firebase.analytics").get())
        add("implementation", libs.findLibrary("firebase.crashlytics").get())
        add("implementation", libs.findLibrary("firebase.config").get())
    }
}