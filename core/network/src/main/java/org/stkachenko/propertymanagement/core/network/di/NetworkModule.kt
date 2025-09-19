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
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.stkachenko.propertymanagement.core.network.BuildConfig
import org.stkachenko.propertymanagement.core.network.auth.AuthInterceptor
import org.stkachenko.propertymanagement.core.network.AuthPmNetwork
import org.stkachenko.propertymanagement.core.network.auth.TokenAuthenticator
import org.stkachenko.propertymanagement.core.storage.TokenStorage
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
    fun publicOkHttpCallFactory(): Call.Factory =
        trace("PmPublicOkHttpClient") {
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .apply {
                            if (BuildConfig.DEBUG) {
                                setLevel(HttpLoggingInterceptor.Level.BODY)
                            }
                        },
                )
                .build()
        }

    @Provides
    @Singleton
    fun protectedOkHttpCallFactory(
        tokenStorage: TokenStorage,
        authNetwork: dagger.Lazy<AuthPmNetwork>,
    ): Call.Factory = trace("PmProtectedOkHttpClient") {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    },
            )
            .addInterceptor(AuthInterceptor(tokenStorage))
            .authenticator(TokenAuthenticator(tokenStorage, authNetwork.get()))
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