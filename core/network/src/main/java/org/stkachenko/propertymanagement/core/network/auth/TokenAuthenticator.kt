package org.stkachenko.propertymanagement.core.network.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.stkachenko.propertymanagement.core.network.AuthPmNetwork
import org.stkachenko.propertymanagement.core.storage.TokenStorage
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val authApi: AuthPmNetwork,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            tokenStorage.clearTokens()
            return null
        }

        val refreshToken = tokenStorage.getRefreshToken() ?: return null

        val newTokens = runBlocking {
            try {
                authApi.refreshToken(refreshToken)
            } catch (e: Exception) {
                null
            }
        }

        return if (newTokens != null) {
            tokenStorage.saveTokens(newTokens.accessToken, newTokens.refreshToken)

            // Будуємо новий запит із свіжим accessToken
            response.request.newBuilder()
                .header("Authorization", "Bearer ${newTokens.accessToken}")
                .build()
        } else {
            tokenStorage.clearTokens()
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            result++
            priorResponse = priorResponse.priorResponse
        }
        return result
    }
}