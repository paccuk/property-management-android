package org.stkachenko.propertymanagement.core.data.repository.userdata

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.repository.userdata.UserDataRepository
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.userdata.DarkThemeConfig
import org.stkachenko.propertymanagement.core.model.data.userdata.UserData
import javax.inject.Inject

internal class OfflineFirstUserDataRepository @Inject constructor(
    private val pmPreferencesDataSource: PmPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        pmPreferencesDataSource.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        pmPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        pmPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
    }
}