plugins {
    id("tilly.android.library")
    id("tilly.android.compose")
}

android {
    namespace = "com.seeho.tilly.core.designsystem"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
}