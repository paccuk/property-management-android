package org.stkachenko.propertymanagement.core.data.repository.property

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.property.Property

interface PropertiesRepository : Syncable {
    fun getPropertiesByOwnerId(id: String): Flow<List<Property>>

}