package org.stkachenko.propertymanagement.core.data

import kotlinx.coroutines.CoroutineDispatcher
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.network.Dispatcher
import org.stkachenko.propertymanagement.core.network.PmDispatchers.IO

interface Syncable {
    suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
    ): Boolean
}