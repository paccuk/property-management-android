package org.stkachenko.propertymanagement.core.database.util

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

internal class ListConverter {
    private val json = Json { encodeDefaults = true }

    @TypeConverter
    fun listToString(list: List<String>?): String? =
        list?.let { json.encodeToString(it) }

    @TypeConverter
    fun stringToList(value: String?): List<String>? =
        value?.let { json.decodeFromString<List<String>>(it) }
}