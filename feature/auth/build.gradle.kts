plugins {
    alias(libs.plugins.propertymanagement.android.feature)
    alias(libs.plugins.propertymanagement.android.library.compose)
}

android {
    namespace = "org.stkachenko.propertymanagement.feature.auth"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)

    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
