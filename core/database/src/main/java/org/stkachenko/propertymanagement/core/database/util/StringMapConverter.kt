package org.stkachenko.propertymanagement.core.database.util

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

internal class StringMapConverter {
    private val json = Json { encodeDefaults = true }

    @TypeConverter
    fun stringMapToJsonString(map: Map<String, String>?): String? =
        map?.let { json.encodeToString(it) }

    @TypeConverter
    fun jsonStringToStringMap(value: String?): Map<String, String>? =
        value?.let { json.decodeFromString(it) }
}
