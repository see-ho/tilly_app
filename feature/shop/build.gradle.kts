plugins {
    id("tilly.android.feature")
}

android {
    namespace = "com.seeho.tilly.feature.shop"
}

dependencies {
    implementation(project(":core:domain"))
}
