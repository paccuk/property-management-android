package org.stkachenko.propertymanagement.core.datastore.user_preferences

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import org.stkachenko.propertymanagement.core.datastore.UserPreferences
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): UserPreferences =
        try {
            UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) =
        t.writeTo(output)
}