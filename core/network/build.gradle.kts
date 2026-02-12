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
            localPropertiesFile.inputStream().use { properties.load(it) }
        }

        val apiKey = properties.getProperty("OPENAI_API_KEY") ?: ""
        if (apiKey.isBlank()) {
            logger.warn("OPENAI_API_KEY가 local.properties에 설정되지 않았습니다. AI 분석 기능이 동작하지 않습니다.")
        }

        buildConfigField(
            "String",
            "OPENAI_API_KEY",
            "\"${apiKey}\""
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
