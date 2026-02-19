package org.stkachenko.propertymanagement.core.storage.encryptor

import org.stkachenko.propertymanagement.core.datastore.model.TokenData
import org.stkachenko.propertymanagement.core.storage.TRANSFORMATION
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

private const val T_LEN = 128

class AesEncryptor @Inject constructor() : Encryptor {

    override fun encrypt(data: ByteArray, secretKey: SecretKey): TokenData {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(data)

        return TokenData(
            token = encrypted,
            iv = iv,
        )
    }

    override fun decrypt(token: ByteArray, iv: ByteArray, secretKey: SecretKey): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(T_LEN, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        return cipher.doFinal(token)
    }
}