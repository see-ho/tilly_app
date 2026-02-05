plugins {
    `kotlin-dsl`
}

group = "com.seeho.tilly.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "tilly.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}
