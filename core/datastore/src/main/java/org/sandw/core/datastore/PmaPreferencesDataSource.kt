package org.sandw.core.datastore

import androidx.datastore.core.DataStore
import org.sandw.core.model.data.UserData
import kotlinx.coroutines.flow.map
import org.sandw.core.model.data.DarkThemeConfig
import javax.inject.Inject


class PmaPreferencesDataSource @Inject constructor(
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
}