import java.util.Properties

plugins {
    id("tilly.android.library")
    id("tilly.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.seeho.tilly.core.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val properties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
        }

        buildConfigField(
            "String",
            "OPENAI_API_KEY",
            "\"${properties.getProperty("OPENAI_API_KEY") ?: ""}\""
        )
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    
    // Networking
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
}
