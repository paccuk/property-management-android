package org.stkachenko.propertymanagement.core.storage.mappers

import com.google.protobuf.ByteString
import org.stkachenko.propertymanagement.core.datastore.AuthTokens
import org.stkachenko.propertymanagement.core.datastore.EncryptedToken
import org.stkachenko.propertymanagement.core.datastore.model.TokenData
import org.stkachenko.propertymanagement.core.model.data.auth.Tokens
import org.stkachenko.propertymanagement.core.storage.encryptor.Encryptor
import javax.crypto.SecretKey

fun Tokens.encrypt(encryptor: Encryptor, secretKey: SecretKey): AuthTokens {
    val encryptedAccessToken = encryptor.encrypt(accessToken.toByteArray(), secretKey)
    val encryptedRefreshToken = encryptor.encrypt(refreshToken.toByteArray(), secretKey)

    return AuthTokens.newBuilder()
        .setAccessToken(
            EncryptedToken.newBuilder()
                .setToken(ByteString.copyFrom(encryptedAccessToken.token))
                .setIv(ByteString.copyFrom(encryptedAccessToken.iv))
        )
        .setRefreshToken(
            EncryptedToken.newBuilder()
                .setToken(ByteString.copyFrom(encryptedRefreshToken.token))
                .setIv(ByteString.copyFrom(encryptedRefreshToken.iv))
        )
        .build()
}

fun TokenData.decrypt(encryptor: Encryptor, secretKey: SecretKey): String? {
    val decryptedToken = encryptor.decrypt(
        token = token,
        iv = iv,
        secretKey = secretKey
    )

    return String(decryptedToken, Charsets.UTF_8)
}