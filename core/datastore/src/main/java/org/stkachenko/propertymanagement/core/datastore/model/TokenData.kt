package org.stkachenko.propertymanagement.core.datastore.model

data class TokenData(
    val token: ByteArray,
    val iv: ByteArray,
)