package com.seeho.tilly.ui

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import com.seeho.tilly.core.navigation.TilDetail
import com.seeho.tilly.feature.home.navigation.Home
import com.seeho.tilly.feature.home.navigation.homeScreen
import com.seeho.tilly.feature.report.navigation.reportScreen
import com.seeho.tilly.feature.statistics.navigation.statisticsScreen
import com.seeho.tilly.feature.shop.navigation.shopScreen
import com.seeho.tilly.feature.editor.navigation.editorScreen
import com.seeho.tilly.feature.report.navigation.ReportRoute
import com.seeho.tilly.feature.statistics.navigation.StatisticsRoute
import com.seeho.tilly.feature.tildetails.navigation.tilDetailScreen
import com.seeho.tilly.feature.shop.navigation.navigateToShop
import com.seeho.tilly.feature.editor.navigation.navigateToEditor
import com.seeho.tilly.navigation.TopLevelDestination

@Composable
fun TillyApp(
    appState: TillyAppState = rememberTillyAppState(),
) {
    Scaffold(
        bottomBar = {
            if (appState.currentTopLevelDestination != null) {
                AppBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NavHost(
                navController = appState.navController,
                startDestination = Home,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                homeScreen(
                    onTilClick = { id ->
                        appState.navController.navigate(TilDetail(id))
                    },
                    onEditorClick = {
                        appState.navController.navigateToEditor()
                    },
                    onShopClick = {
                        appState.navController.navigateToShop()
                    }
                )
                tilDetailScreen()
                reportScreen()
                statisticsScreen()
                shopScreen()
                editorScreen()
            }
        }
    }
}

@Composable
private fun AppBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = if (selected) {
                            destination.selectedIcon
                        } else {
                            destination.unselectedIcon
                        },
                        contentDescription = destination.label
                    )
                },
                label = { Text(destination.label) }
            )
        }
    }
}

@SuppressLint("RestrictedApi")
private fun NavDestination?.isTopLevelDestinationInHierarchy(
    destination: TopLevelDestination
) = this?.hierarchy?.any {
    when (destination) {
        TopLevelDestination.HOME -> it.hasRoute<Home>()
        TopLevelDestination.STATISTICS -> it.hasRoute<StatisticsRoute>()
        TopLevelDestination.REPORT -> it.hasRoute<ReportRoute>()
    }
} ?: false
