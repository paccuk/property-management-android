package org.stkachenko.propertymanagement.core.database.model.image

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "images",
)
data class ImageEntity(
    @PrimaryKey
    val id: String,
    val url: String,
)