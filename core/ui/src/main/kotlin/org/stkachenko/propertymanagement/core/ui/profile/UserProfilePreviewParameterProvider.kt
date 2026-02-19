package org.stkachenko.propertymanagement.core.ui.profile

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.stkachenko.propertymanagement.core.model.data.user.User
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.ui.profile.PreviewParameterData.owners
import org.stkachenko.propertymanagement.core.ui.profile.PreviewParameterData.tenants

class OwnerProfilePreviewParameterProvider : PreviewParameterProvider<List<User>> {
    override val values: Sequence<List<User>>
        get() = sequenceOf(owners)
}

class TenantProfilePreviewParameterProvider : PreviewParameterProvider<List<User>> {
    override val values: Sequence<List<User>>
        get() = sequenceOf(tenants)
}

private object PreviewParameterData {
    val owners = listOf(
        User(
            id = "user1",
            firstName = "John",
            lastName = "Doe",
            email = "jdoe@example.com",
            phone = "+1234567890",
            role = UserRole.OWNER,
            avatarImageUrl = "https://randomuser.me/api/portraits/men/1.jpg",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        ),
        User(
            id = "user3",
            firstName = "Alice",
            lastName = "Johnson",
            email = "ajhonson@example.com",
            phone = "+1122334455",
            role = UserRole.OWNER,
            avatarImageUrl = "https://randomuser.me/api/portraits/women/3.jpg",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        ),
    )

    val tenants = listOf(
        User(
            id = "user2",
            firstName = "Jane",
            lastName = "Smith",
            email = "jsmith@example.com",
            phone = "+0987654321",
            role = UserRole.TENANT,
            avatarImageUrl = "https://randomuser.me/api/portraits/women/2.jpg",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        ),
        User(
            id = "user4",
            firstName = "Bob",
            lastName = "Brown",
            email = "bbrown@example.com",
            phone = "+5566778899",
            role = UserRole.TENANT,
            avatarImageUrl = "https://randomuser.me/api/portraits/men/4.jpg",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        ),
    )
}