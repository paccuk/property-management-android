package org.stkachenko.propertymanagement.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.stkachenko.propertymanagement.core.network.authenticator.REQUEST_ID_HEADER
import java.util.UUID

class UuidInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithId = chain.request().newBuilder()
            .header(REQUEST_ID_HEADER, UUID.randomUUID().toString())
            .build()
        return chain.proceed(requestWithId)
    }
}