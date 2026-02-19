plugins {
    alias(libs.plugins.propertymanagement.android.library)
    alias(libs.plugins.propertymanagement.android.room)
    alias(libs.plugins.propertymanagement.hilt)
}

android {
    namespace = "org.stkachenko.propertymanagement.core.database"
}

dependencies {
    api(projects.core.model)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}