package org.stkachenko.propertymanagement.core.network.auth

import okhttp3.Interceptor
import okhttp3.Response
import org.stkachenko.propertymanagement.core.storage.TokenStorage
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            tokenStorage.getAccessToken()?.let { token ->
                header("Authorization", "Bearer $token")
            }
        }.build()

        return chain.proceed(request)
    }
}