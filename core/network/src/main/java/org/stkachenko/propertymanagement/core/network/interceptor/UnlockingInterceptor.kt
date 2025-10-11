package org.stkachenko.propertymanagement.core.network.interceptor

import kotlinx.coroutines.sync.Mutex
import okhttp3.Interceptor
import okhttp3.Response
import org.stkachenko.propertymanagement.core.network.authenticator.REQUEST_ID_HEADER

class UnlockingInterceptor(
    private val mutex: Mutex,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestId =
            chain.request().header(REQUEST_ID_HEADER) ?: return chain.proceed(chain.request())

        return try {
            chain.proceed(chain.request())
        } finally {
            if (mutex.holdsLock(requestId)) {
                mutex.unlock(requestId)
            }
        }
    }
}