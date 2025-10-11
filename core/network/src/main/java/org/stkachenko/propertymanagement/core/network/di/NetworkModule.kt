package org.stkachenko.propertymanagement.core.network.di

import android.content.Context
import androidx.tracing.trace
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.stkachenko.propertymanagement.core.network.BuildConfig
import org.stkachenko.propertymanagement.core.network.AuthNetworkDataSource
import org.stkachenko.propertymanagement.core.network.Dispatcher
import org.stkachenko.propertymanagement.core.network.PmDispatchers.IO
import org.stkachenko.propertymanagement.core.network.authenticator.TokenAuthenticator
import org.stkachenko.propertymanagement.core.network.interceptor.TokenInterceptor
import org.stkachenko.propertymanagement.core.network.interceptor.UnlockingInterceptor
import org.stkachenko.propertymanagement.core.network.interceptor.UuidInterceptor
import org.stkachenko.propertymanagement.core.storage.KeystoreTokenStorage
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    @Named("public")
    fun publicOkHttpCallFactory(): Call.Factory =
        trace("AuthOkHttpClient") {
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .apply {
                            if (BuildConfig.DEBUG) {
                                setLevel(HttpLoggingInterceptor.Level.BODY)
                            }
                        },
                )
                .addInterceptor(UuidInterceptor())
                .build()
        }

    @Provides
    @Singleton
    @Named("protected")
    fun protectedOkHttpCallFactory(
        tokenStorage: KeystoreTokenStorage,
        authNetwork: dagger.Lazy<AuthNetworkDataSource>,
        isLogoutStarted: () -> Boolean,
        startLogout: () -> Unit,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
    ): Call.Factory = trace("ProtectedOkHttpClient") {
        val mutex = Mutex()

        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    },
            )
            .addInterceptor(UuidInterceptor())
            .addInterceptor(TokenInterceptor(tokenStorage))
            .authenticator(
                TokenAuthenticator(
                    tokenStorage = tokenStorage,
                    authNetwork = authNetwork.get(),
                    isLogoutStarted = isLogoutStarted,
                    startLogout = startLogout,
                    refreshMutex = mutex,
                    ioDispatcher = ioDispatcher,
                )
            )
            .addInterceptor(UnlockingInterceptor(mutex))
            .build()
    }

    @Provides
    @Singleton
    fun imageLoader(
        okHttpCallFactory: dagger.Lazy<Call.Factory>,
        @ApplicationContext application: Context,
    ): ImageLoader = trace("PmImageLoader") {
        ImageLoader.Builder(application)
            .callFactory(okHttpCallFactory.get())
            .components { add(SvgDecoder.Factory()) }
            .respectCacheHeaders(false)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }
}