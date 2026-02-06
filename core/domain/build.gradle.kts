plugins {
    id("java-library")
    kotlin("jvm")
}

dependencies {
    implementation(libs.javax.inject)

    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
