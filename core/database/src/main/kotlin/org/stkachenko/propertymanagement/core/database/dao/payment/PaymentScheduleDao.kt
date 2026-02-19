package org.stkachenko.propertymanagement.core.database.dao.payment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.payment.PaymentScheduleEntity

@Dao
interface PaymentScheduleDao {

    @Query(
        value = """
            SELECT * FROM payment_schedules
            WHERE agreementId = :agreementId
            ORDER BY dueDate ASC
        """,
    )
    fun getPaymentScheduleEntitiesByAgreementId(agreementId: String): Flow<List<PaymentScheduleEntity>>

    @Query(
        value = """
            SELECT * FROM payment_schedules
            WHERE id IN (:ids)
        """,
    )
    fun getPaymentScheduleEntities(ids: Set<String>): Flow<List<PaymentScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertOrIgnorePaymentSchedules(entities: List<PaymentScheduleEntity>): List<Long>

    @Upsert
    suspend fun upsertPaymentSchedules(entities: List<PaymentScheduleEntity>)

    @Query(
        value = """
            DELETE FROM payment_schedules
            WHERE id IN (:ids)
        """,
    )
    suspend fun deletePaymentSchedules(ids: List<String>)

    @Query("SELECT * FROM payment_schedules WHERE updatedAt > :timestamp")
    suspend fun getPaymentSchedulesUpdatedAfter(timestamp: Long): List<PaymentScheduleEntity>
}