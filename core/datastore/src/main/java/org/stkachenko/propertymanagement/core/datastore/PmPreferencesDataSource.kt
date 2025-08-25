package org.stkachenko.propertymanagement.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.firstOrNull
import org.stkachenko.propertymanagement.core.model.data.userdata.UserData
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.model.data.userdata.DarkThemeConfig
import java.io.IOException
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

    suspend fun getChangeListVersions() = userPreferences.data
        .map {
            ChangeListVersions(
                userVersion = it.userChangeListVersion,
                profileVersion = it.profileChangeListVersion,
                propertyVersion = it.propertyChangeListVersion,
                chatVersion = it.chatChangeListVersion,
                chatParticipantVersion = it.chatParticipantChangeListVersion,
                messageVersion = it.messageChangeListVersion,
                paymentVersion = it.paymentChangeListVersion,
                invoiceVersion = it.invoiceChangeListVersion,
                paymentScheduleVersion = it.paymentScheduleChangeListVersion,
                rentalAgreementVersion = it.rentalAgreementChangeListVersion,
                rentalInviteVersion = it.rentalInviteChangeListVersion,
                rentalOfferVersion = it.rentalOfferChangeListVersion,
                imageVersion = it.imageChangeListVersion,
            )
        }
        .firstOrNull() ?: ChangeListVersions()

    suspend fun updateChangeListVersions(update: ChangeListVersions.() -> ChangeListVersions) {
        try {
            userPreferences.updateData { currentPreferences ->
                val updatedChangeListVersions = update(
                    ChangeListVersions(
                        userVersion = currentPreferences.userChangeListVersion,
                        profileVersion = currentPreferences.profileChangeListVersion,
                        propertyVersion = currentPreferences.propertyChangeListVersion,
                        chatVersion = currentPreferences.chatChangeListVersion,
                        chatParticipantVersion = currentPreferences.chatParticipantChangeListVersion,
                        messageVersion = currentPreferences.messageChangeListVersion,
                        paymentVersion = currentPreferences.paymentChangeListVersion,
                        invoiceVersion = currentPreferences.invoiceChangeListVersion,
                        paymentScheduleVersion = currentPreferences.paymentScheduleChangeListVersion,
                        rentalAgreementVersion = currentPreferences.rentalAgreementChangeListVersion,
                        rentalInviteVersion = currentPreferences.rentalInviteChangeListVersion,
                        rentalOfferVersion = currentPreferences.rentalOfferChangeListVersion,
                        imageVersion = currentPreferences.imageChangeListVersion,
                    ),
                )

                currentPreferences.copy {
                    userChangeListVersion = updatedChangeListVersions.userVersion
                    profileChangeListVersion = updatedChangeListVersions.profileVersion
                    propertyChangeListVersion = updatedChangeListVersions.propertyVersion
                    chatChangeListVersion = updatedChangeListVersions.chatVersion
                    chatParticipantChangeListVersion = updatedChangeListVersions.chatParticipantVersion
                    messageChangeListVersion = updatedChangeListVersions.messageVersion
                    paymentChangeListVersion = updatedChangeListVersions.paymentVersion
                    invoiceChangeListVersion = updatedChangeListVersions.invoiceVersion
                    paymentScheduleChangeListVersion = updatedChangeListVersions.paymentScheduleVersion
                    rentalAgreementChangeListVersion = updatedChangeListVersions.rentalAgreementVersion
                    rentalInviteChangeListVersion = updatedChangeListVersions.rentalInviteVersion
                    rentalOfferChangeListVersion = updatedChangeListVersions.rentalOfferVersion
                    imageChangeListVersion = updatedChangeListVersions.imageVersion
                }
            }
        } catch (ioException: IOException) {
            Log.e("PmPreferences", "Failed to update user preferences", ioException)
        }
    }
}