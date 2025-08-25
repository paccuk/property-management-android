package org.stkachenko.propertymanagement.core.database.dao.payment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.payment.PaymentEntity

@Dao
interface PaymentDao {

    @Query(
        value = """
            SELECT * FROM payments
            WHERE id IN (:ids)
        """,
    )
    fun getPaymentEntities(ids: Set<String>): Flow<List<PaymentEntity>>

    @Query(
        value = """
            SELECT * FROM payments
            WHERE id = :paymentId
        """,
    )
    fun getPaymentEntity(paymentId: String): Flow<PaymentEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertOrIgnorePayments(entities: List<PaymentEntity>): List<Long>

    @Upsert
    suspend fun upsertPayments(entities: List<PaymentEntity>)

    @Query(
        value = """
            DELETE FROM payments
            WHERE id IN (:ids)
        """,
    )
    suspend fun deletePayments(ids: List<String>)
}