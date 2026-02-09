package com.seeho.tilly.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            implementation(platform(bom))
            androidTestImplementation(platform(bom))

            implementation(libs.findLibrary("androidx-activity-compose").get())
            implementation(libs.findLibrary("androidx-compose-ui").get())
            implementation(libs.findLibrary("androidx-compose-ui-graphics").get())
            implementation(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            implementation(libs.findLibrary("androidx-compose-material3").get())
            implementation(libs.findLibrary("androidx-compose-material-icons").get())

            implementation(libs.findLibrary("coil-compose").get())
            implementation(libs.findLibrary("coil-gif").get())

            debugImplementation(libs.findLibrary("androidx-compose-ui-tooling").get())
            debugImplementation(libs.findLibrary("androidx-compose-ui-test-manifest").get())
            
            androidTestImplementation(libs.findLibrary("androidx-compose-ui-test-junit4").get())
        }
    }
}

private fun org.gradle.api.artifacts.dsl.DependencyHandler.implementation(dependencyNotation: Any) =
    add("implementation", dependencyNotation)

private fun org.gradle.api.artifacts.dsl.DependencyHandler.androidTestImplementation(dependencyNotation: Any) =
    add("androidTestImplementation", dependencyNotation)

private fun org.gradle.api.artifacts.dsl.DependencyHandler.debugImplementation(dependencyNotation: Any) =
    add("debugImplementation", dependencyNotation)
