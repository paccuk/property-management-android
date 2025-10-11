package org.stkachenko.propertymanagement.core.storage.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.stkachenko.propertymanagement.core.datastore.auth_tokens.PmTokensDataStore
import org.stkachenko.propertymanagement.core.storage.KeystoreTokenStorage
import org.stkachenko.propertymanagement.core.storage.TokenStorage
import org.stkachenko.propertymanagement.core.storage.encryptor.AesEncryptor
import org.stkachenko.propertymanagement.core.storage.encryptor.Encryptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    @Singleton
    abstract fun bindsAesEncryptor(
        encryptor: AesEncryptor,
    ): Encryptor

    @Binds
    @Singleton
    abstract fun bindsKeystoreTokenStorage(
        tokenStorage: KeystoreTokenStorage,
    ): TokenStorage
}