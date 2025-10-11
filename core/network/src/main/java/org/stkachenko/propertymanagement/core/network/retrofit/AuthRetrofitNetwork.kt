package org.stkachenko.propertymanagement.core.network.retrofit

import androidx.tracing.trace
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import org.stkachenko.propertymanagement.core.network.model.auth.NetworkAuthTokens
import org.stkachenko.propertymanagement.core.network.model.auth.LoginRequest
import org.stkachenko.propertymanagement.core.network.model.auth.RefreshRequest
import org.stkachenko.propertymanagement.core.network.model.auth.RegisterRequest
import org.stkachenko.propertymanagement.core.network.AuthNetworkDataSource
import org.stkachenko.propertymanagement.core.network.BuildConfig
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private interface AuthRetrofitPmNetworkApi {

    @POST(value = "auth/signin")
    suspend fun login(
        @Body request: LoginRequest,
    ): NetworkAuthTokens

    @POST("auth/signup")
    suspend fun register(
        @Body request: RegisterRequest,
    ): NetworkAuthTokens

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshRequest,
    ): NetworkAuthTokens
}

private const val AUTH_BASE_URL = BuildConfig.AUTH_BACKEND_URL

@Singleton
internal class AuthRetrofitNetwork @Inject constructor(
    networkJson: Json,
    @Named("public") okHttpFactory: dagger.Lazy<Call.Factory>,
) : AuthNetworkDataSource {

    private val networkApi = trace("AuthRetrofitNetwork") {
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .callFactory { okHttpFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(AuthRetrofitPmNetworkApi::class.java)
    }

    override suspend fun login(username: String, password: String): NetworkAuthTokens =
        networkApi.login(LoginRequest(username, password))

    override suspend fun register(username: String, password: String): NetworkAuthTokens =
        networkApi.register(RegisterRequest(username, password))

    override suspend fun refreshToken(refreshToken: String): NetworkAuthTokens =
        networkApi.refreshToken(RefreshRequest(refreshToken))

}