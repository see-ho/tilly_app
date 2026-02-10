plugins {
    id("tilly.android.feature")
}

android {
    namespace = "com.seeho.tilly.feature.home"
}

dependencies {
    implementation(project(":core:domain"))
}
