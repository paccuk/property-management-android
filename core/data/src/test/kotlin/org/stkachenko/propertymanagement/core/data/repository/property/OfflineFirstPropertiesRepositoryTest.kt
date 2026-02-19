package org.stkachenko.propertymanagement.core.data.repository.property

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.datastore.UserPreferences
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.network.model.property.NetworkProperty
import org.stkachenko.propertymanagement.core.testing.datastore.InMemoryDataStore
import org.stkachenko.propertymanagement.core.testing.database.TestPropertyDao
import org.stkachenko.propertymanagement.core.testing.network.TestProtectedNetworkDataSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OfflineFirstPropertiesRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var pmPreferencesDataSource: PmPreferencesDataSource
    private lateinit var propertyDao: TestPropertyDao
    private lateinit var networkDataSource: TestProtectedNetworkDataSource
    private lateinit var pmPreferences: PmPreferencesDataSource
    private lateinit var repository: OfflineFirstPropertiesRepository

    @Before
    fun setup() {
        propertyDao = TestPropertyDao()
        networkDataSource = TestProtectedNetworkDataSource()
        pmPreferences = PmPreferencesDataSource(InMemoryDataStore(UserPreferences.getDefaultInstance()))
        repository = OfflineFirstPropertiesRepository(propertyDao, networkDataSource)
    }

    @Test
    fun getPropertiesByOwnerId_returnsPropertiesFromDao() = runTest {
        propertyDao.setProperties(
            createPropertyEntity(id = "1", ownerId = "owner1"),
            createPropertyEntity(id = "2", ownerId = "owner1"),
            createPropertyEntity(id = "3", ownerId = "owner2"),
        )

        val result = repository.getPropertiesByOwnerId("owner1").first()

        assertEquals(2, result.size)
        assertEquals(listOf("1", "2"), result.map { it.id })
    }

    @Test
    fun getPropertyById_returnsPropertyFromDao() = runTest {
        propertyDao.setProperties(
            createPropertyEntity(id = "1", ownerId = "owner1"),
        )

        val result = repository.getPropertyById("1").first()

        assertEquals("1", result?.id)
        assertEquals("owner1", result?.ownerId)
    }

    @Test
    fun getPropertyById_returnsNullWhenNotFound() = runTest {
        val result = repository.getPropertyById("nonexistent").first()

        assertEquals(null, result)
    }

    @Test
    fun syncWith_uploadsLocalChangesToNetwork() = runTest(testDispatcher) {
        val localProperty = createPropertyEntity(id = "1", ownerId = "owner1", updatedAt = 200L)
        propertyDao.setProperties(localProperty)
        pmPreferences.setLastPropertySyncTime(100L)

        repository.syncWith(pmPreferences, testDispatcher)

        assertEquals(1, networkDataSource.updatedProperties.size)
        assertEquals("1", networkDataSource.updatedProperties.first().id)
    }

    @Test
    fun syncWith_downloadsRemoteChangesToDao() = runTest(testDispatcher) {
        val networkProperty = createNetworkProperty(id = "2", ownerId = "owner2", updatedAt = 200L)
        networkDataSource.setProperties(networkProperty)
        pmPreferences.setLastPropertySyncTime(100L)

        repository.syncWith(pmPreferences, testDispatcher)

        val storedProperties = propertyDao.getAll()
        assertEquals(1, storedProperties.size)
        assertEquals("2", storedProperties.first().id)
    }

    @Test
    fun syncWith_updatesLastSyncTime() = runTest(testDispatcher) {
        val networkProperty = createNetworkProperty(id = "1", updatedAt = 300L)
        networkDataSource.setProperties(networkProperty)
        pmPreferences.setLastPropertySyncTime(100L)

        repository.syncWith(pmPreferences, testDispatcher)

        assertEquals(300L, pmPreferences.getLastPropertySyncTime())
    }

    @Test
    fun syncWith_returnsTrue() = runTest(testDispatcher) {
        val result = repository.syncWith(pmPreferences, testDispatcher)

        assertTrue(result)
    }

    @Test
    fun syncWith_doesNotUpdateSyncTime_whenNoChanges() = runTest(testDispatcher) {
        pmPreferences.setLastPropertySyncTime(100L)

        repository.syncWith(pmPreferences, testDispatcher)

        assertEquals(100L, pmPreferences.getLastPropertySyncTime())
    }

    private fun createPropertyEntity(
        id: String,
        ownerId: String = "owner1",
        updatedAt: Long = 0L,
    ) = PropertyEntity(
        id = id,
        ownerId = ownerId,
        price = 100.0,
        currency = "USD",
        type = "apartment",
        area = 50.0,
        isAvailable = true,
        address = emptyMap(),
        attributes = emptyMap(),
        images = emptyList(),
        createdAt = 0L,
        updatedAt = updatedAt,
    )

    private fun createNetworkProperty(
        id: String,
        ownerId: String = "owner1",
        updatedAt: Long = 0L,
    ) = NetworkProperty(
        id = id,
        ownerId = ownerId,
        price = 100.0,
        currency = "USD",
        type = "apartment",
        area = 50.0,
        isAvailable = true,
        address = emptyMap(),
        attributes = emptyMap(),
        images = emptyList(),
        createdAt = 0L,
        updatedAt = updatedAt,
    )
}