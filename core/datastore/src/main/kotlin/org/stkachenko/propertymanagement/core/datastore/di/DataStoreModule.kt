package org.stkachenko.propertymanagement.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.stkachenko.propertymanagement.core.datastore.AuthTokens
import org.stkachenko.propertymanagement.core.datastore.UserPreferences
import org.stkachenko.propertymanagement.core.datastore.auth_tokens.AuthTokensSerializer
import org.stkachenko.propertymanagement.core.datastore.user_preferences.UserPreferencesSerializer
import org.stkachenko.propertymanagement.core.common.network.Dispatcher
import org.stkachenko.propertymanagement.core.common.network.PmDispatchers.IO
import org.stkachenko.propertymanagement.core.common.network.di.ApplicationScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    internal fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        userPreferencesSerializer: UserPreferencesSerializer,
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher)
        ) {
            context.dataStoreFile("user_preferences.pb")
        }

    @Provides
    @Singleton
    internal fun providesAuthTokensDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        authTokensSerializer: AuthTokensSerializer,
    ): DataStore<AuthTokens> =
        DataStoreFactory.create(
            serializer = authTokensSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher)
        ) {
            context.dataStoreFile("auth_tokens.pb")
        }

}