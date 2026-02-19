package org.stkachenko.propertymanagement.core.database.util

import androidx.room.TypeConverter
import java.util.Date

internal class DateConverter {

    @TypeConverter
    fun dateToLong(date: Date?): Long? =
        date?.time

    @TypeConverter
    fun longToDate(value: Long?): Date? =
        value?.let { Date(it) }
}