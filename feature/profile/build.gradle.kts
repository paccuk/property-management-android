plugins {
    alias(libs.plugins.propertymanagement.android.feature)
    alias(libs.plugins.propertymanagement.android.library.compose)
}

android {
    namespace = "org.stkachenko.propertymanagement.feature.profile"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    api(projects.core.navigationContract)
    api(projects.core.localization)

    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}