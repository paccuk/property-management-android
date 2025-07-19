package org.sandw.core.data.local.repository

import kotlinx.coroutines.flow.Flow
import org.sandw.core.datastore.PmaPreferencesDataSource
import org.sandw.core.model.data.DarkThemeConfig
import org.sandw.core.model.data.UserData
import javax.inject.Inject

internal class UserDataRepositoryImpl @Inject constructor(
    private val pmaPreferencesDataSource: PmaPreferencesDataSource
) : UserDataRepository {

    override val userData: Flow<UserData> =
        pmaPreferencesDataSource.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        pmaPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        pmaPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
    }
}