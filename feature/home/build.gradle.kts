plugins {
    id("tilly.android.feature")
}

android {
    namespace = "com.seeho.tilly.feature.home"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:domain"))
}
