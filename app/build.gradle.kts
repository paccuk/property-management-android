import org.stkachenko.propertymanagement.convention.PmBuildType

plugins {
    alias(libs.plugins.propertymanagement.android.application)
    alias(libs.plugins.propertymanagement.android.application.compose)
    alias(libs.plugins.propertymanagement.android.application.flavors)
    alias(libs.plugins.propertymanagement.hilt)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "org.stkachenko.propertymanagement"

    defaultConfig {
        applicationId = "org.stkachenko.propertymanagement"
        versionCode = 1
        versionName = "0.1.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = PmBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true
            applicationIdSuffix = PmBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.named("debug").get()
//            baselineProfile.automaticGenerationDuringBuild = true
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    // Feature Modules
    implementation(projects.feature.properties)
    implementation(projects.feature.profile)
    implementation(projects.feature.chats)
    implementation(projects.feature.statistics)
    implementation(projects.feature.notifications)

    // Core Project Modules
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.sync.work)

    // Core Android Libraries
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.window.core)
//    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.coil.kt)

    // Hilt Dependency Injection
    ksp(libs.hilt.compiler)

    // Testing Dependencies
    debugImplementation(libs.androidx.compose.ui.testManifest)
//    debugImplementation(projects.uiTestHiltManifest)

    kspTest(libs.hilt.compiler)

//    testImplementation(projects.core.dataTest)
    testImplementation(libs.hilt.android.testing)
//    testImplementation(projects.sync.syncTest)

//    testDemoImplementation(libs.robolectric)
//    testDemoImplementation(libs.roborazzi)
//    testDemoImplementation(projects.core.screenshotTesting)

    androidTestImplementation(kotlin("test"))
    androidTestImplementation(projects.core.testing)
//    androidTestImplementation(projects.core.dataTest)
//    androidTestImplementation(projects.core.datastoreTest)
    androidTestImplementation(libs.androidx.test.espresso.core)
//    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.android.testing)

//    baselineProfile(projects.benchmarks)

    // Debugging Dependencies
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

//baselineProfile {
//    automaticGenerationDuringBuild = false
//}
//
//dependencyGuard {
//    configuration("prodReleaseRuntimeClasspath")
//}
