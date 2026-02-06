plugins {
    `kotlin-dsl`
}

group = "com.seeho.tilly.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "tilly.android.library"
            implementationClass = "com.seeho.tilly.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "tilly.android.hilt"
            implementationClass = "com.seeho.tilly.buildlogic.AndroidHiltConventionPlugin"
        }
        register("androidCompose") {
            id = "tilly.android.compose"
            implementationClass = "com.seeho.tilly.buildlogic.AndroidComposeConventionPlugin"
        }
    }
}
