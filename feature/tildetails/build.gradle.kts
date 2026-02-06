plugins {
    id("tilly.android.library")
    id("tilly.android.hilt")
    id("tilly.android.compose")
}

android {
    namespace = "com.seeho.tilly.feature.tildetails"
}

dependencies {
    implementation(project(":core:model"))
}
