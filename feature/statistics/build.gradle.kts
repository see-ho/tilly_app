plugins {
    id("tilly.android.feature")
}

android {
    namespace = "com.seeho.tilly.feature.statistics"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.vico.compose.m3)
}
