package org.stkachenko.propertymanagement.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.stkachenko.propertymanagement.core.database.PmDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesPmDatabase(
        @ApplicationContext context: Context,
    ): PmDatabase = Room.databaseBuilder(
        context,
        PmDatabase::class.java,
        "pm-database",
    ).build()
}