package org.stkachenko.propertymanagement.core.common.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.stkachenko.propertymanagement.core.common.network.Dispatcher
import org.stkachenko.propertymanagement.core.common.network.PmDispatchers.Default
import org.stkachenko.propertymanagement.core.common.network.PmDispatchers.IO

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(Default)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}