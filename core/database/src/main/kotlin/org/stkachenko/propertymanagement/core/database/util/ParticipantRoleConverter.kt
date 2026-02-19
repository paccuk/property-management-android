package org.stkachenko.propertymanagement.core.database.util

import androidx.room.TypeConverter
import org.stkachenko.propertymanagement.core.model.data.chat.ParticipantRole

internal class ParticipantRoleConverter {

    @TypeConverter
    fun participantRoleToString(role: ParticipantRole?): String? =
        role?.let {
            when (it) {
                ParticipantRole.ADMIN -> "admin"
                ParticipantRole.MEMBER -> "member"
            }
        }

    @TypeConverter
    fun stringToParticipantRole(value: String?): ParticipantRole? =
        value?.let {
            when (it) {
                "admin" -> ParticipantRole.ADMIN
                "member" -> ParticipantRole.MEMBER
                else -> null
            }
        }
}