package org.stkachenko.propertymanagement.core.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import org.stkachenko.propertymanagement.core.designsystem.R

object PmIcons {

    val HomeSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.home_selected)

    val HomeUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.home_unselected)

    val PropertiesSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.properties_selected)

    val PropertiesUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.properties_unselected)

    val ProfileSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.profile_selected)

    val ProfileUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.profile_unselected)

    val ChatsSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.chats_selected)

    val ChatsUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.chats_unselected)

    val BillsSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.bills_selected)

    val BillsUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.bills_unselected)

    val StatisticsSelected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.statistics_selected)

    val StatisticsUnselected: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.statistics_unselected)
}