plugins {
    id("tilly.android.library")
    id("tilly.android.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.seeho.tilly.core.navigation"
}

dependencies {
    api(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}
