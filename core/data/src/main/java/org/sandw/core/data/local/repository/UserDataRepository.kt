package org.sandw.core.data.local.repository

import kotlinx.coroutines.flow.Flow
import org.sandw.core.model.data.DarkThemeConfig
import org.sandw.core.model.data.UserData

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
}