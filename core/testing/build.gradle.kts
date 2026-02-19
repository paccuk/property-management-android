plugins {
    alias(libs.plugins.propertymanagement.android.library)
    alias(libs.plugins.propertymanagement.hilt)
}

android {
    namespace = "org.stkachenko.propertymanagement.core.testing"
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(projects.core.data)
    api(projects.core.model)

    implementation(projects.core.database)
    implementation(projects.core.datastoreProto)
    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.datetime)
}