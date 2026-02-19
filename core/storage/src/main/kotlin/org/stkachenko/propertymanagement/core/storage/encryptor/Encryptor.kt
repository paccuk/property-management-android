package org.stkachenko.propertymanagement.core.storage.encryptor

import org.stkachenko.propertymanagement.core.datastore.model.TokenData
import javax.crypto.SecretKey

interface Encryptor {
    fun encrypt(data: ByteArray, secretKey: SecretKey): TokenData
    fun decrypt(token: ByteArray, iv: ByteArray, secretKey: SecretKey): ByteArray
}