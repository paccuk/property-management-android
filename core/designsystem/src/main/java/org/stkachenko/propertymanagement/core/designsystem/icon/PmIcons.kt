package org.stkachenko.propertymanagement.core.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import org.stkachenko.propertymanagement.core.designsystem.R

object PmIcons {

    val HomeSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_home_selected)

    val HomeUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_home_unselected)

    val PropertiesSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_properties_selected)

    val PropertiesUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_properties_unselected)

    val ProfileSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_profile_selected)

    val ProfileUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_profile_unselected)

    val ChatsSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_chats_selected)

    val ChatsUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_chats_unselected)

    val BillsSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_bills_selected)

    val BillsUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_bills_unselected)

    val StatisticsSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_statistics_selected)

    val StatisticsUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.core_designsystem_statistics_unselected)
}