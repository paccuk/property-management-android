package org.stkachenko.propertymanagement.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.tracing.traceAsync
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.repository.chat.ChatsRepository
import org.stkachenko.propertymanagement.core.data.repository.user.UserRepository
import org.stkachenko.propertymanagement.core.data.repository.property.PropertiesRepository
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.network.Dispatcher
import org.stkachenko.propertymanagement.core.network.PmDispatchers.IO
import org.stkachenko.propertymanagement.sync.initializers.SyncConstraints
import org.stkachenko.propertymanagement.sync.initializers.syncForegroundInfo
import org.stkachenko.propertymanagement.sync.status.SyncSubscriber

@HiltWorker
internal class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val pmPreference: PmPreferencesDataSource,
    private val propertiesRepository: PropertiesRepository,
    private val chatsRepository: ChatsRepository,
    private val profileRepository: UserRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val syncSubscriber: SyncSubscriber,
) : CoroutineWorker(appContext, workerParams), Synchronizer {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        traceAsync("Sync", 0) {
            syncSubscriber.subscribe()

            val syncedSuccessfully = awaitAll(
                async { propertiesRepository.sync() },
                async { chatsRepository.sync() },
                async { profileRepository.sync() },
            ).all { it }

            if (syncedSuccessfully) {
                Result.success()
            } else {
                Result.failure()
            }
        }
    }

    override suspend fun getChangeListVersions(): ChangeListVersions =
        pmPreference.getChangeListVersions()

    override suspend fun updateChangeListVersions(
        update: ChangeListVersions.() -> ChangeListVersions
    ) = pmPreference.updateChangeListVersions(update)

    companion object {
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}