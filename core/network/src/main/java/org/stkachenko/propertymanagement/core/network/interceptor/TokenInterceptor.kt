package org.stkachenko.propertymanagement.core.network.interceptor

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.stkachenko.propertymanagement.core.storage.TokenStorage

class TokenInterceptor(
    private val tokenStorage: TokenStorage,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { tokenStorage.getAccessToken() }
        val request = if (accessToken != null) {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            chain.request()
        }


        return chain.proceed(request)
    }
}