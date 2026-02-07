package com.seeho.tilly.feature.statistics.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.seeho.tilly.feature.statistics.StatisticsScreen
import kotlinx.serialization.Serializable

@Serializable
data object StatisticsRoute

fun NavController.navigateToStatistics() = navigate(StatisticsRoute)

fun NavGraphBuilder.statisticsScreen() {
    composable<StatisticsRoute> {
        StatisticsScreen()
    }
}