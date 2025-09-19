package org.stkachenko.propertymanagement.core.domain.auth

import org.stkachenko.propertymanagement.core.data.repository.auth.AuthRepository
import org.stkachenko.propertymanagement.core.model.data.user.User
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(username: String, password: String): User {

        val user = authRepository.authenticateUser(username, password)

        if (user == null) {
            throw IllegalArgumentException(ERROR_MESSAGE)
        }

        return user
    }
}

const val ERROR_MESSAGE = "Invalid email or password."

//    private fun validateEmail(email: String): Boolean =
//        Patterns.EMAIL_ADDRESS.matcher(email).matches()
//
//    private fun validatePassword(password: String): Boolean =
//        Regex(PASSWORD_REGEX_STRING).matches(password)

//const val PASSWORD_REGEX_STRING =
//    "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"
//
//const val WARNING_MESSAGE = """Password must be at least 8 characters long and include:
//- Uppercase letter
//- Lowercase letter
//- Digit
//- Special character (@$!%*#?&)
//"""

