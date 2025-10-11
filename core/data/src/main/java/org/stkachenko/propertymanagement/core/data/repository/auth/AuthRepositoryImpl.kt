package org.stkachenko.propertymanagement.core.data.repository.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.stkachenko.propertymanagement.core.model.data.usersession.UserSessionData
import org.stkachenko.propertymanagement.core.network.AuthNetworkDataSource
import org.stkachenko.propertymanagement.core.network.Dispatcher
import org.stkachenko.propertymanagement.core.network.PmDispatchers.IO
import org.stkachenko.propertymanagement.core.storage.TokenStorage
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val network: AuthNetworkDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String,
    ): Boolean = withContext(ioDispatcher) {
        val response = network.login(username, password)
        tokenStorage.saveTokens(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
        )

        return@withContext true
    }


    override suspend fun register(
        username: String,
        password: String,
    ): Boolean = withContext(ioDispatcher) {
        val response = network.register(username, password)
        tokenStorage.saveTokens(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
        )

        return@withContext true
    }

    override suspend fun refreshToken(): Boolean = withContext(ioDispatcher) {
        val refreshToken = tokenStorage.getRefreshToken() ?: throw Exception("No refresh token")

        val response = network.refreshToken(refreshToken)
        tokenStorage.saveTokens(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
        )

        return@withContext true
    }

    override suspend fun logout() {
        tokenStorage.clearTokens()
    }
}