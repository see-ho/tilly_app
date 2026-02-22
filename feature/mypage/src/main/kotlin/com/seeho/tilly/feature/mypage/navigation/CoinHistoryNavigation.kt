package com.seeho.tilly.feature.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.seeho.tilly.feature.mypage.coinhistory.CoinHistoryScreen
import kotlinx.serialization.Serializable

@Serializable
data object CoinHistoryRoute

fun NavController.navigateToCoinHistory() = navigate(CoinHistoryRoute)

fun NavGraphBuilder.coinHistoryScreen(
    onBackClick: () -> Unit = {},
) {
    composable<CoinHistoryRoute> {
        CoinHistoryScreen(
            onBackClick = onBackClick,
        )
    }
}
