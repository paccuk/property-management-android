package org.stkachenko.propertymanagement.core.database.dao.payment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.payment.InvoiceEntity

@Dao
interface InvoiceDao {

    @Query(
        value = """
            SELECT * FROM invoices
            WHERE id IN (:ids)
        """,
    )
    fun getInvoiceEntities(ids: Set<String>): Flow<List<InvoiceEntity>>

    @Query(
        value = """
            SELECT * FROM invoices
            WHERE id = :invoiceId
        """,
    )
    fun getInvoiceEntity(invoiceId: String): Flow<InvoiceEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreInvoices(entities: List<InvoiceEntity>): List<Long>

    @Upsert
    suspend fun upsertInvoices(entities: List<InvoiceEntity>)

    @Query(
        value = """
            DELETE FROM invoices
            WHERE id IN (:ids)
        """,
    )
    suspend fun deleteInvoices(ids: List<String>)

    @Query("SELECT * FROM invoices WHERE updatedAt > :timestamp")
    suspend fun getInvoicesUpdatedAfter(timestamp: Long): List<InvoiceEntity>
}