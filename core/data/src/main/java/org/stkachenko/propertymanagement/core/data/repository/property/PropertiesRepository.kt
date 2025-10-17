package org.stkachenko.propertymanagement.core.data.repository.property

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.property.Property

interface PropertiesRepository : Syncable {
    fun getPropertiesByOwnerId(ownerId: String): Flow<List<Property>>

    fun getPropertyById(id: String): Flow<Property?>

}