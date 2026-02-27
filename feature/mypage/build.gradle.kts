plugins {
    id("tilly.android.feature")
}

android {
    namespace = "com.seeho.tilly.feature.mypage"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:datastore"))
    implementation(project(":core:common"))
}
