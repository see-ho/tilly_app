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
 * 
 * api: 외부 모듈(app)에서도 접근 가능해야 하는 의존성
 * implementation: feature 내부에서만 사용하는 의존성
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
                // API: 외부에 노출 (app 모듈이 Route, Model 등에 접근)
                "api"(project(":core:navigation"))
                "api"(project(":core:model"))
                
                // Implementation: 내부에서만 사용
                "implementation"(project(":core:designsystem"))
                "implementation"(project(":core:common"))
                "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
                "implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
            }
        }
    }
}
