plugins {
    alias(libs.plugins.propertymanagement.android.library)
    alias(libs.plugins.propertymanagement.hilt)
}

android {
    namespace = "org.stkachenko.propertymanagement.core.datastore"

    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

dependencies {
    api(libs.androidx.datastore)
    api(projects.core.datastoreProto)
    api(projects.core.model)

    implementation(projects.core.common)
}