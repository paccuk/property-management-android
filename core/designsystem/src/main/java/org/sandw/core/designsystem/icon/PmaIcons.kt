package org.sandw.core.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import org.sandw.core.designsystem.R

object PmaIcons {
    @Composable
    fun homeSelected(): Painter = painterResource(R.drawable.home_selected)

    @Composable
    fun homeUnselected(): Painter = painterResource(R.drawable.home_unselected)

    @Composable
    fun propertiesSelected(): Painter = painterResource(R.drawable.properties_selected)

    @Composable
    fun propertiesUnselected(): Painter = painterResource(R.drawable.properties_unselected)

    @Composable
    fun profileSelected(): Painter = painterResource(R.drawable.profile_selected)

    @Composable
    fun profileUnselected(): Painter = painterResource(R.drawable.profile_unselected)

    @Composable
    fun chatsSelected(): Painter = painterResource(R.drawable.chats_selected)

    @Composable
    fun chatsUnselected(): Painter = painterResource(R.drawable.chats_unselected)

    @Composable
    fun billsSelected(): Painter = painterResource(R.drawable.bills_selected)

    @Composable
    fun billsUnselected(): Painter = painterResource(R.drawable.bills_unselected)

    @Composable
    fun statisticsSelected(): Painter = painterResource(R.drawable.statistics_selected)

    @Composable
    fun statisticsUnselected(): Painter = painterResource(R.drawable.statistics_unselected)
}