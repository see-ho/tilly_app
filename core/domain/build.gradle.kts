plugins {
    id("tilly.android.library")
    id("tilly.android.hilt")
}

android {
    namespace = "com.seeho.tilly.core.domain"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
