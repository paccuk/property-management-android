package org.stkachenko.propertymanagement.core.data.repository.auth

import org.stkachenko.propertymanagement.core.database.dao.user.UserDao
import org.stkachenko.propertymanagement.core.model.data.user.User
import org.stkachenko.propertymanagement.core.network.PmNetworkDataSource
import javax.inject.Inject

internal class OfflineFirstAuthRepository @Inject constructor(
    private val userDao: UserDao,
    private val network: PmNetworkDataSource,
) : AuthRepository {

    override fun authenticateUser(username: String, password: String): User? =
        userDao.getUserEntityByUsername(username)

}