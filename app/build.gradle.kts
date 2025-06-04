import com.startup.convention.walkie.Const
import java.util.Properties

plugins {
    alias(libs.plugins.walkie.android.application)
    alias(libs.plugins.kotlin.serialization)
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.startup.walkie"
    defaultConfig {
        applicationId = "com.startup.walkie"
        versionCode = 9
        versionName = "1.0.3"
        targetSdk = Const.TARGET_SDK
        val nativeAppKey = properties.getProperty("NATIVE_APP_KEY")
        buildConfigField("String", "NATIVE_APP_KEY", nativeAppKey)
        manifestPlaceholders["NATIVE_APP_KEY"] = nativeAppKey?.trim('"') ?: ""
    }

    signingConfigs {
        create("release") {
            storeFile = file("$rootDir/app/keystore/walkie_key.jks")
            storePassword = properties.getProperty("RELEASE_STORE_PASSWORD") as String
            keyAlias = properties.getProperty("RELEASE_KEY_ALIAS") as String
            keyPassword = properties.getProperty("RELEASE_KEY_PASSWORD") as String
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "$projectDir/proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        buildConfig = true
    }

    applicationVariants.all {
        val variant = this
        val appName = "walkie"
        val buildTypeName = variant.buildType.name
        val versionName = variant.versionName
        val versionCode = variant.versionCode
        variant.outputs.mapNotNull { it as? com.android.build.gradle.internal.api.BaseVariantOutputImpl }.forEach { output ->
            output.outputFileName = "${appName}_${buildTypeName}_v${versionName}(${versionCode}).apk"
        }
    }
    afterEvaluate {
        tasks.named("bundleRelease").configure {
            doLast {
                val releaseDir = File(rootDir, "app/release")
                val aabFile = releaseDir.walkTopDown()
                    .firstOrNull { it.isFile && it.extension == "aab" }

                if (aabFile != null) {
                    val versionName = android.defaultConfig.versionName
                    val versionCode = android.defaultConfig.versionCode
                    val newFileName = "walkie_release_v${versionName}(${versionCode}).aab"
                    val renamed = aabFile.renameTo(File(aabFile.parentFile, newFileName))
                    if (renamed) {
                        println("✅ AAB 파일 이름 변경 완료: $newFileName")
                    } else {
                        println("⚠️ AAB 파일 이름 변경 실패")
                    }
                } else {
                    println("⚠️ AAB 파일을 찾지 못했습니다. 경로: ${releaseDir.absolutePath}")
                }
            }
        }
    }
}

dependencies {
    implementation(project(":navigation"))
    implementation(project(":feature:home"))
    implementation(project(":feature:login"))
    implementation(project(":feature:spot"))
    implementation(project(":feature:stepcounter"))
    implementation(project(":core:common"))
    implementation(project(":core:design-system"))
    implementation(project(":core:data"))
    implementation(project(":core:resource"))
    implementation(libs.kakao.login)
}