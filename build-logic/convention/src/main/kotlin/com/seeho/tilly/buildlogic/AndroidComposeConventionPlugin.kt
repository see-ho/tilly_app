package com.seeho.tilly.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            
            val extension = extensions.findByType<LibraryExtension>()
                ?: extensions.findByType<ApplicationExtension>()
                ?: extensions.findByType<CommonExtension<*, *, *, *, *, *>>()

            extension?.let { 
                configureAndroidCompose(it)
            }
        }
    }
}
