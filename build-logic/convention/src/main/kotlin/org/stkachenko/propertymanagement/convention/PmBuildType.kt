package org.stkachenko.propertymanagement.convention

enum class PmBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}