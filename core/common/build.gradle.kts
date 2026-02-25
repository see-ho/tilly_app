plugins {
    id("tilly.android.library")
    id("tilly.android.hilt")
}

android {
    namespace = "com.seeho.tilly.core.common"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.work.runtime.ktx)
}
