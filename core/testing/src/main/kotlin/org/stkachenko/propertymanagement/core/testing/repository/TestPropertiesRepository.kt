package org.stkachenko.propertymanagement.core.testing.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.stkachenko.propertymanagement.core.data.repository.property.PropertiesRepository
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.property.Property

class TestPropertiesRepository : PropertiesRepository {

    private val properties = mutableListOf<Property>()

    override fun getPropertiesByOwnerId(ownerId: String): Flow<List<Property>> =
        flowOf(properties.filter { it.ownerId == ownerId })

    override fun getPropertyById(id: String): Flow<Property?> =
        flowOf(properties.find { it.id == id })

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean = true

    fun addProperties(vararg property: Property) {
        properties.addAll(property)
    }

    fun clearProperties() {
        properties.clear()
    }
}

