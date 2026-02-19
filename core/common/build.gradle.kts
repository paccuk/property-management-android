plugins {
    alias(libs.plugins.propertymanagement.jvm.library)
    alias(libs.plugins.propertymanagement.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}