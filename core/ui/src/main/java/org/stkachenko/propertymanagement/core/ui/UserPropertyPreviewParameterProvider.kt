package org.stkachenko.propertymanagement.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.stkachenko.propertymanagement.core.model.data.image.Image
import org.stkachenko.propertymanagement.core.ui.PreviewParameterData.properties
import org.stkachenko.propertymanagement.core.model.data.userdata.DarkThemeConfig
import org.stkachenko.propertymanagement.core.model.data.userdata.UserData
import org.stkachenko.propertymanagement.core.model.data.property.Property

class UserPropertyPreviewParameterProvider : PreviewParameterProvider<List<Property>> {
    override val values: Sequence<List<Property>>
        get() = sequenceOf(properties)

}

object PreviewParameterData {

    val properties = listOf(
        Property(
            id = "1",
            ownerId = "owner1",
            price = 2300.0,
            type = "Apartment",
            area = 54.0,
            isAvailable = true,
            address = mapOf(
                "street" to "123 Main St",
                "city" to "Anytown",
            ),
            attributes = mapOf(
                "floors" to "2",
            ),
            images = listOf(
                Image(
                    "1",
                    "https://images.unsplash.com/photo-1612637968894-660373e23b03?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    0
                )
            ),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        ),
        Property(
            id = "2",
            ownerId = "owner2",
            price = 1500.0,
            type = "Condo",
            area = 75.0,
            isAvailable = false,
            address = mapOf(
                "street" to "456 Side St",
                "city" to "Othertown",
            ),
            attributes = mapOf(
                "hasGarage" to "true",
            ),
            images = listOf(
                Image(
                    "1",
                    "https://images.unsplash.com/photo-1612637968894-660373e23b03?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    0
                )
            ),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        ),
        Property(
            id = "3",
            ownerId = "owner3",
            price = 3200.0,
            type = "House",
            area = 120.0,
            isAvailable = true,
            address = mapOf(
                "street" to "789 High St",
                "city" to "Sometown",
            ),
            attributes = mapOf(
                "numBedrooms" to "4",
            ),
            images = listOf(
                Image(
                    "2",
                    "https://images.unsplash.com/photo-1501183638714-8f3c5a6c5d7e?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    0
                )
            ),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        ),
    )
}