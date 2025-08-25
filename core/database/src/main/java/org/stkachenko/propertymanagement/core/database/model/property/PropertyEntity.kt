package org.stkachenko.propertymanagement.core.database.model.property

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity

@Entity(
    tableName = "properties",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("ownerId"),
    ],
)
data class PropertyEntity(
    @PrimaryKey
    val id: String,
    val ownerId: String,
    val price: Double,
    val type: String,
    val area: Double,
    val isAvailable: Boolean,
    val address: Map<String, String>,
    val attributes: Map<String, String>,
    val createdAt: Long,
    val updatedAt: Long,
)