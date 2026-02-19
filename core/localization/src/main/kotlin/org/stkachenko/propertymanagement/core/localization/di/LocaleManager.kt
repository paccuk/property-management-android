package org.stkachenko.propertymanagement.core.localization.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.stkachenko.propertymanagement.core.localization.AppLocaleManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocaleManager {
    @Provides
    @Singleton
    fun provideAppLocaleManager(
        @ApplicationContext context: Context
    ): AppLocaleManager = AppLocaleManager(context)
}