package org.stkachenko.propertymanagement.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.stkachenko.propertymanagement.core.network.AuthNetworkDataSource
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.retrofit.AuthRetrofitNetwork
import org.stkachenko.propertymanagement.core.network.retrofit.ProtectedRetrofitNetwork

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    fun bindsProtectedRetrofitNetwork(impl: ProtectedRetrofitNetwork): ProtectedNetworkDataSource

    @Binds
    fun bindsAuthRetrofitNetwork(impl: AuthRetrofitNetwork): AuthNetworkDataSource
}