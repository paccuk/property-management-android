package org.stkachenko.propertymanagement.core.network.di

import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.storage.TokenStorage
import javax.inject.Inject


class LogoutState @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val pmPreferencesDataSource: PmPreferencesDataSource,
) {
    @Volatile
    private var _isLogoutStarted = false

    val isLogoutStarted: Boolean
        get() = _isLogoutStarted

    suspend fun startLogout() {
        _isLogoutStarted = true
        tokenStorage.clearTokens()
        pmPreferencesDataSource.clearUserSessionData()
    }

    fun resetLogoutState() {
        _isLogoutStarted = false
    }
}
