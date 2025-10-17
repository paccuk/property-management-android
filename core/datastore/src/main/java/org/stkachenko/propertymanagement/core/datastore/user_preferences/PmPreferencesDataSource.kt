package org.stkachenko.propertymanagement.core.datastore.user_preferences

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.datastore.DarkThemeConfigProto
import org.stkachenko.propertymanagement.core.datastore.UserPreferences
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.model.data.userdata.DarkThemeConfig
import org.stkachenko.propertymanagement.core.model.data.userdata.UserData
import org.stkachenko.propertymanagement.core.model.data.usersession.UserSessionData
import javax.inject.Inject

class PmPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .map {
            UserData(
                darkThemeConfig = when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                        -> DarkThemeConfig.FOLLOW_SYSTEM

                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> DarkThemeConfig.LIGHT
                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
                },
                useDynamicColor = it.useDynamicColor,
            )
        }

    val userSessionData = userPreferences.data
        .map {
            UserSessionData(
                userRole = UserRole.valueOf(it.userRole.ifEmpty { UserRole.TENANT.name }), // TODO("Обробити не знайдену роль")
                isLoggedIn = it.isLoggedIn,
                userId = it.userId,
            )
        }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.updateData {
            it.toBuilder().setUseDynamicColor(useDynamicColor).build()
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData {
            it.toBuilder().setDarkThemeConfig(
                when (darkThemeConfig) {
                    DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
                    DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                    DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                }
            ).build()
        }
    }

    suspend fun updateUserSessionData(userSessionData: UserSessionData) {
        userPreferences.updateData {
            it.toBuilder()
                .setUserRole(userSessionData.userRole.name)
                .setIsLoggedIn(userSessionData.isLoggedIn)
                .setUserId(userSessionData.userId)
                .build()
        }
    }

    suspend fun setUserRole(userRole: UserRole) {
        userPreferences.updateData {
            it.toBuilder().setUserRole(userRole.name).build()
        }
    }

    suspend fun setIsLoggedIn(isLoggedIn: Boolean) {
        userPreferences.updateData {
            it.toBuilder().setIsLoggedIn(isLoggedIn).build()
        }
    }

    suspend fun setUserId(userId: String) {
        userPreferences.updateData {
            it.toBuilder().setUserId(userId).build()
        }
    }

    suspend fun getLastPropertySyncTime(): Long {
        return userPreferences.data.map { it.lastPropertySyncTime }.firstOrNull() ?: 0L
    }

    suspend fun setLastPropertySyncTime(timestamp: Long) {
        userPreferences.updateData {
            it.toBuilder().setLastPropertySyncTime(timestamp).build()
        }
    }

    suspend fun getLastUserSyncTime(): Long {
        return userPreferences.data.map { it.lastUserSyncTime }.firstOrNull() ?: 0L
    }

    suspend fun setLastUserSyncTime(timestamp: Long) {
        userPreferences.updateData {
            it.toBuilder().setLastUserSyncTime(timestamp).build()
        }
    }

    suspend fun getLastRentalSyncTime(): Long {
        return userPreferences.data.map { it.lastRentalSyncTime }.firstOrNull() ?: 0L
    }

    suspend fun setLastRentalSyncTime(timestamp: Long) {
        userPreferences.updateData {
            it.toBuilder().setLastRentalSyncTime(timestamp).build()
        }
    }

    suspend fun getLastPaymentSyncTime(): Long {
        return userPreferences.data.map { it.lastPaymentSyncTime }.firstOrNull() ?: 0L
    }

    suspend fun setLastPaymentSyncTime(timestamp: Long) {
        userPreferences.updateData {
            it.toBuilder().setLastPaymentSyncTime(timestamp).build()
        }
    }

    suspend fun getLastRentalInviteSyncTime(): Long {
        return userPreferences.data.map { it.lastRentalInviteSyncTime }.firstOrNull() ?: 0L
    }

    suspend fun setLastRentalInviteSyncTime(timestamp: Long) {
        userPreferences.updateData {
            it.toBuilder().setLastRentalInviteSyncTime(timestamp).build()
        }
    }

    suspend fun getLastRentalOfferSyncTime(): Long {
        return userPreferences.data.map { it.lastRentalOfferSyncTime }.firstOrNull() ?: 0L
    }

    suspend fun setLastRentalOfferSyncTime(timestamp: Long) {
        userPreferences.updateData {
            it.toBuilder().setLastRentalOfferSyncTime(timestamp).build()
        }
    }

    suspend fun getLastInvoiceSyncTime(): Long {
        return userPreferences.data.map { it.lastInvoiceSyncTime }.firstOrNull() ?: 0L
    }

    suspend fun setLastInvoiceSyncTime(timestamp: Long) {
        userPreferences.updateData {
            it.toBuilder().setLastInvoiceSyncTime(timestamp).build()
        }
    }

    suspend fun getLastPaymentScheduleSyncTime(): Long {
        return userPreferences.data.map { it.lastPaymentScheduleSyncTime }.firstOrNull() ?: 0L
    }

    suspend fun setLastPaymentScheduleSyncTime(timestamp: Long) {
        userPreferences.updateData {
            it.toBuilder().setLastPaymentScheduleSyncTime(timestamp).build()
        }
    }
}