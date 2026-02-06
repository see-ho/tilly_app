plugins {
    id("tilly.android.library")
    id("tilly.android.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.seeho.tilly.core.navigation"
}

dependencies {
    // Navigation을 api로 노출 → feature 모듈들이 core:navigation만 의존하면 navigation 사용 가능
    api(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}
