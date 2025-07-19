package org.sandw.core.model.data

data class UserProperty internal constructor(
    val id: String,
    val street: String,
    val city: String,
    val headerImageUrl: String?,
) {
    constructor(property: Property, userData: UserData) : this(
        id = property.id,
        street = property.street,
        city = property.city,
        headerImageUrl = property.headerImageUrl,
    )
}