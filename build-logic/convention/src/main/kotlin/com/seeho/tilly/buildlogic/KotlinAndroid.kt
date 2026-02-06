package com.seeho.tilly.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = 36

        defaultConfig {
            minSdk = 26
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(block: org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions.() -> Unit) {
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kotlinOptions", block)
}
