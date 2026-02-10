plugins {
    id("tilly.android.library")
    id("tilly.android.hilt")
}

android {
    namespace = "com.seeho.tilly.core.data"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:database"))
}
