package com.seeho.tilly.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.seeho.tilly.feature.home.navigation.Home
import com.seeho.tilly.feature.report.navigation.ReportRoute
import com.seeho.tilly.feature.statistics.navigation.StatisticsRoute
import com.seeho.tilly.navigation.TopLevelDestination
import com.seeho.tilly.navigation.TopLevelDestination.HOME
import com.seeho.tilly.navigation.TopLevelDestination.REPORT
import com.seeho.tilly.navigation.TopLevelDestination.STATISTICS
import kotlinx.coroutines.CoroutineScope

@Stable
class TillyAppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when {
            currentDestination?.hasRoute<Home>() == true -> HOME
            currentDestination?.hasRoute<ReportRoute>() == true -> REPORT
            currentDestination?.hasRoute<StatisticsRoute>() == true -> STATISTICS
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = androidx.navigation.navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            HOME -> navController.navigate(Home, topLevelNavOptions)
            STATISTICS -> navController.navigate(StatisticsRoute, topLevelNavOptions)
            REPORT -> navController.navigate(ReportRoute, topLevelNavOptions)
        }
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberTillyAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): TillyAppState {
    return remember(
        navController,
        coroutineScope,
    ) {
        TillyAppState(
            navController,
        )
    }
}