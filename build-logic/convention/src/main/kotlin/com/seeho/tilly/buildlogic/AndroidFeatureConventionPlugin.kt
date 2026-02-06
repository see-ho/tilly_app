package com.seeho.tilly.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Feature 모듈을 위한 통합 컨벤션 플러그인
 * AndroidLibrary + Compose + Hilt + Serialization 조합
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("tilly.android.library")
                apply("tilly.android.compose")
                apply("tilly.android.hilt")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                // Serialization (Type-Safe Navigation)
                "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
                
                // Navigation Compose
                "implementation"(libs.findLibrary("androidx.navigation.compose").get())
                
                // Common feature dependencies
                "implementation"(project(":core:model"))
                "implementation"(project(":core:designsystem"))
                "implementation"(project(":core:common"))
            }
        }
    }
}
