package org.sandw.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.sandw.core.ui.PreviewParameterData.properties
import org.sandw.core.model.data.DarkThemeConfig
import org.sandw.core.model.data.Property
import org.sandw.core.model.data.UserData
import org.sandw.core.model.data.UserProperty

class UserPropertyPreviewParameterProvider : PreviewParameterProvider<List<UserProperty>> {
    override val values: Sequence<List<UserProperty>>
        get() = sequenceOf(properties)

}

object PreviewParameterData {
    private val userData: UserData = UserData(
        darkThemeConfig = DarkThemeConfig.DARK,
        useDynamicColor = false,
    )

    val properties = listOf(
        UserProperty(
            property = Property(
                id = "1",
                street = "123 Main St",
                city = "Anytown",
                headerImageUrl = "https://images.unsplash.com/photo-1612637968894-660373e23b03?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            ),
            userData = userData,
        ),
        UserProperty(
            property = Property(
                id = "2",
                street = "456 Middle St",
                city = "Sometown",
                headerImageUrl = "https://example.com/header_image.jpg",
            ),
            userData = userData,
        ),
        UserProperty(
            property = Property(
                id = "3",
                street = "789 Last St",
                city = "Thattown",
                headerImageUrl = "https://example.com/header_image.jpg",
            ),
            userData = userData,
        ),
    )
}