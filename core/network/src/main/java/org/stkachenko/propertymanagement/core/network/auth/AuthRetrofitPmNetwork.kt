package org.stkachenko.propertymanagement.core.network.auth

import androidx.tracing.trace
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import org.stkachenko.propertymanagement.core.network.AuthPmNetwork
import org.stkachenko.propertymanagement.core.network.BuildConfig
import org.stkachenko.propertymanagement.core.network.model.auth.AuthResponse
import org.stkachenko.propertymanagement.core.network.model.auth.LoginRequest
import org.stkachenko.propertymanagement.core.network.model.auth.RefreshRequest
import org.stkachenko.propertymanagement.core.network.model.auth.RegisterRequest
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

private interface AuthRetrofitPmNetworkApi {

    @POST(value = "auth/login")
    suspend fun login(
        @Body request: LoginRequest,
    ): AuthResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest,
    ): AuthResponse

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshRequest,
    ): AuthResponse
}

private const val AUTH_BASE_URL = BuildConfig.AUTH_BACKEND_URL

@Singleton
internal class AuthRetrofitPmNetwork @Inject constructor(
    networkJson: Json,
    okHttpFactory: dagger.Lazy<Call.Factory>,
) : AuthPmNetwork {

    private val networkApi = trace("AuthNetwork") {
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .callFactory { okHttpFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(AuthRetrofitPmNetworkApi::class.java)
    }

    override suspend fun login(username: String, password: String): AuthResponse =
        networkApi.login(LoginRequest(username, password))

    override suspend fun register(username: String, password: String): AuthResponse =
        networkApi.register(RegisterRequest(username, password))

    override suspend fun refreshToken(refreshToken: String): AuthResponse =
        networkApi.refreshToken(RefreshRequest(refreshToken))

}