plugins {
    id("tilly.android.feature")
}

android {
    namespace = "com.seeho.tilly.feature.editor"
}

dependencies {
    implementation(project(":core:domain"))
}
