plugins {
    id("tilly.android.library")
    id("tilly.android.hilt")
    id("tilly.android.compose")
}

android {
    namespace = "com.seeho.tilly.feature.report"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
}
