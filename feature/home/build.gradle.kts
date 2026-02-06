plugins {
    id("tilly.android.library")
    id("tilly.android.hilt")
    id("tilly.android.compose")
}

android {
    namespace = "com.seeho.tilly.feature.home"
}

dependencies {
    implementation(project(":core:model"))
}
