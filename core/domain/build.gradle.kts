plugins {
    alias(libs.plugins.propertymanagement.android.library)
    id("com.google.devtools.ksp")
}

android {
    namespace = "org.stkachenko.propertymanagement.core.domain"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)

    implementation(libs.javax.inject)
    
    testImplementation(projects.core.testing)
}