pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "propertymanagement"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:designsystem")
include(":core:domain")
include(":core:localization")
include(":core:model")
include(":core:navigation-contract")
include(":core:network")
include(":core:notifications")
include(":core:storage")
include(":core:testing")
include(":core:ui")

include(":feature:auth")
include(":feature:chats")
include(":feature:notifications")
include(":feature:profile")
include(":feature:properties")
include(":feature:statistics")
