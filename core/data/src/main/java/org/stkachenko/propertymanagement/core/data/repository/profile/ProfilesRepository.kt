package org.stkachenko.propertymanagement.core.data.repository.profile

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.profile.Profile

interface ProfilesRepository: Syncable {
    fun getProfileByUserId(id: String): Flow<Profile>
}