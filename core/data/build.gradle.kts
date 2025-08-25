plugins {
    alias(libs.plugins.propertymanagement.android.library)
    alias(libs.plugins.propertymanagement.hilt)
}

android {
    namespace = "org.stkachenko.propertymanagement.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    api(projects.core.database)
    api(projects.core.datastore)
    api(projects.core.network)

    testImplementation(projects.core.testing)

}