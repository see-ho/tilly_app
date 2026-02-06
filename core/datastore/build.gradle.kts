plugins {
    id("tilly.android.library")
    id("tilly.android.hilt")
}

android {
    namespace = "com.seeho.tilly.core.datastore"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
