package org.stkachenko.propertymanagement.core.datastore.auth_tokens

import androidx.datastore.core.DataStore
import com.google.protobuf.ByteString
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.datastore.AuthTokens
import org.stkachenko.propertymanagement.core.datastore.EncryptedToken
import org.stkachenko.propertymanagement.core.datastore.model.TokenData
import javax.inject.Inject

class PmTokensDataStore @Inject constructor(
    private val authTokens: DataStore<AuthTokens>,
) {
    suspend fun setEncryptedTokens(
        encryptedAccessToken: ByteString,
        accessTokenIv: ByteString,
        encryptedRefreshToken: ByteString,
        refreshTokenIv: ByteString,
    ) {
        authTokens.updateData {
            it.toBuilder()
                .setAccessToken(
                    EncryptedToken.newBuilder()
                        .setToken(encryptedAccessToken)
                        .setIv(accessTokenIv)
                )
                .setRefreshToken(
                    EncryptedToken.newBuilder()
                        .setToken(encryptedRefreshToken)
                        .setIv(refreshTokenIv)
                )
                .build()
        }
    }

    suspend fun setEncryptedTokens(tokens: AuthTokens) {
        authTokens.updateData { tokens }
    }

    suspend fun getEncryptedAccessToken() = authTokens.data
        .map {
            TokenData(
                token = it.accessToken.token.toByteArray(),
                iv = it.accessToken.iv.toByteArray(),
            )
        }
        .firstOrNull()


    suspend fun getEncryptedRefreshToken() = authTokens.data
        .map {
            TokenData(
                token = it.refreshToken.token.toByteArray(),
                iv = it.refreshToken.iv.toByteArray(),
            )
        }
        .firstOrNull()

    suspend fun clearEncryptedTokens() {
        authTokens.updateData {
            it.toBuilder()
                .clearAccessToken()
                .clearRefreshToken()
                .build()
        }
    }
}