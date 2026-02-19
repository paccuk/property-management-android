package org.stkachenko.propertymanagement.core.domain.property

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.stkachenko.propertymanagement.core.model.data.property.Property
import org.stkachenko.propertymanagement.core.testing.repository.TestPropertiesRepository
import kotlin.test.assertEquals

class GetPropertiesByOwnerIdUseCaseTest {

    private lateinit var repository: TestPropertiesRepository
    private lateinit var useCase: GetPropertiesByOwnerIdUseCase

    @Before
    fun setup() {
        repository = TestPropertiesRepository()
        useCase = GetPropertiesByOwnerIdUseCase(repository)

        repository.addProperties(
            createTestProperty(id = "1", price = 300.0, area = 50.0),
            createTestProperty(id = "2", price = 100.0, area = 80.0),
            createTestProperty(id = "3", price = 200.0, area = 60.0),
        )
    }

    @Test
    fun returnsPropertiesWithoutSorting_whenSortByIsNONE() = runTest {
        val result = useCase("owner1", PropertySortField.NONE).first()

        assertEquals(3, result.size)
        assertEquals(listOf("1", "2", "3"), result.map { it.id })
    }

    @Test
    fun returnsPropertiesSortedByPrice_whenSortByIsPRICE() = runTest {
        val result = useCase("owner1", PropertySortField.PRICE).first()

        assertEquals(3, result.size)
        assertEquals(listOf(100.0, 200.0, 300.0), result.map { it.price })
    }

    @Test
    fun returnsPropertiesSortedByArea_whenSortByIsAREA() = runTest {
        val result = useCase("owner1", PropertySortField.AREA).first()

        assertEquals(3, result.size)
        assertEquals(listOf(50.0, 60.0, 80.0), result.map { it.area })
    }

    @Test
    fun returnsEmptyList_forOwnerWithNoProperties() = runTest {
        val result = useCase("nonExistentOwner").first()

        assertEquals(emptyList(), result)
    }

    @Test
    fun usesNONEAsDefaultSortField() = runTest {
        val result = useCase("owner1").first()

        assertEquals(listOf("1", "2", "3"), result.map { it.id })
    }

    private fun createTestProperty(
        id: String,
        price: Double,
        area: Double,
    ) = Property(
        id = id,
        ownerId = "owner1",
        price = price,
        currency = "USD",
        type = "apartment",
        area = area,
        isAvailable = true,
        address = emptyMap(),
        attributes = emptyMap(),
        images = emptyList(),
        createdAt = 0L,
        updatedAt = 0L,
    )
}