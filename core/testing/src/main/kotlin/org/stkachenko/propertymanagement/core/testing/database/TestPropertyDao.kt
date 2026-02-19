package org.stkachenko.propertymanagement.core.testing.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.stkachenko.propertymanagement.core.database.dao.property.PropertyDao
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity

class TestPropertyDao : PropertyDao {

    private val properties = MutableStateFlow<List<PropertyEntity>>(emptyList())

    override fun getPropertyEntitiesByOwnerId(ownerId: String): Flow<List<PropertyEntity>> =
        properties.map { list -> list.filter { it.ownerId == ownerId } }

    override fun getPropertyEntitiesByIds(ids: List<String>): Flow<List<PropertyEntity>> =
        properties.map { list -> list.filter { it.id in ids } }

    override fun getPropertyEntityById(id: String): Flow<PropertyEntity?> =
        properties.map { list -> list.find { it.id == id } }

    override suspend fun getPropertiesUpdatedAfter(timestamp: Long): List<PropertyEntity> =
        properties.value.filter { it.updatedAt > timestamp }

    override suspend fun insertOrIgnoreProperties(entities: List<PropertyEntity>): List<Long> {
        val existingIds = properties.value.map { it.id }.toSet()
        val newEntities = entities.filter { it.id !in existingIds }
        properties.update { it + newEntities }
        return entities.map { if (it.id in existingIds) -1L else 1L }
    }

    override suspend fun upsertProperties(entities: List<PropertyEntity>) {
        properties.update { current ->
            val updatedIds = entities.map { it.id }.toSet()
            current.filter { it.id !in updatedIds } + entities
        }
    }

    override suspend fun deleteProperties(ids: List<String>) {
        properties.update { list -> list.filter { it.id !in ids } }
    }

    fun setProperties(vararg property: PropertyEntity) {
        properties.value = property.toList()
    }

    fun getAll(): List<PropertyEntity> = properties.value
}