package org.stkachenko.propertymanagement.core.network.authenticator

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.stkachenko.propertymanagement.core.network.AuthNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.auth.JwtPayload
import org.stkachenko.propertymanagement.core.storage.TokenStorage
import java.util.Base64

private const val MAX_RETRY_COUNT = 3
const val REQUEST_ID_HEADER = "Request-ID"
const val RETRY_COUNT_HEADER = "Retry-Count"

class TokenAuthenticator(
    private val tokenStorage: TokenStorage,
    private val authNetwork: AuthNetworkDataSource,
    private val ioDispatcher: CoroutineDispatcher,
    private val refreshMutex: Mutex,
    private val isLogoutStarted: () -> Boolean,
    private val startLogout: suspend () -> Unit,
) : Authenticator {
    private val json = Json { ignoreUnknownKeys = true }

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        if (isLogoutStarted()) return@runBlocking null

        val requestId = response.request.header(REQUEST_ID_HEADER) ?: return@runBlocking null
        val retryCount = response.request.header(RETRY_COUNT_HEADER)?.toIntOrNull() ?: 0

        if (retryCount >= MAX_RETRY_COUNT) {
            startLogout()
            return@runBlocking null
        }

        return@runBlocking if (refreshMutex.holdsLock(requestId)) {
            handleTokenRefresh(response, retryCount)
        } else {
            refreshMutex.lock(requestId)
            handleTokenRefresh(response, retryCount)
        }
    }

    private suspend fun handleTokenRefresh(response: Response, retryCount: Int): Request? {
        val currentToken = withContext(ioDispatcher) {
            tokenStorage.getAccessToken()
        }

        if (currentToken != null) {
            if (!isTokenExpired(currentToken)) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }
        }

        val newAccessToken = withContext(ioDispatcher) {
            refreshToken()
        }

        return if (newAccessToken != null) {
            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .header(RETRY_COUNT_HEADER, (retryCount + 1).toString())
                .build()
        } else {
            null
        }
    }

    private suspend fun refreshToken(): String? {
        return try {
            val refreshToken = tokenStorage.getRefreshToken()
            if (refreshToken != null) {
                val authResponse = authNetwork.refreshToken(refreshToken)
                tokenStorage.saveTokens(authResponse.accessToken, authResponse.refreshToken)
                authResponse.accessToken
            } else {
                tokenStorage.clearTokens()
                null
            }
        } catch (e: Exception) {
            tokenStorage.clearTokens()
            e.printStackTrace()
            null
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return true

            val decoded = Base64.getUrlDecoder().decode(parts[1])
            val payload = json.decodeFromString<JwtPayload>(decoded.decodeToString())

            val exp = payload.exp ?: return true
            val now = System.currentTimeMillis() / 1000

            now >= exp - 30
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }
}