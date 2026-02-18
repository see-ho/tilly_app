package com.seeho.tilly.ui

import android.annotation.SuppressLint
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import com.seeho.tilly.core.designsystem.component.TillyFab
import com.seeho.tilly.core.designsystem.component.TillyTopAppBar
import com.seeho.tilly.feature.tildetails.navigation.TilDetail
import com.seeho.tilly.feature.home.navigation.Home
import com.seeho.tilly.feature.home.navigation.homeScreen
import com.seeho.tilly.feature.mypage.navigation.MyPageRoute
import com.seeho.tilly.feature.mypage.navigation.myPageScreen
import com.seeho.tilly.feature.shop.navigation.ShopRoute
import com.seeho.tilly.feature.shop.navigation.shopScreen
import com.seeho.tilly.feature.statistics.navigation.statisticsScreen
import com.seeho.tilly.feature.editor.navigation.editorScreen
import com.seeho.tilly.feature.editor.navigation.EditorRoute
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
        topBar = {
            val topLevelDestination = appState.currentTopLevelDestination
            if (topLevelDestination != null) {
                TillyTopAppBar(
                    titleText = when (topLevelDestination) {
                        TopLevelDestination.HOME -> "TILLY"
                        TopLevelDestination.SHOP -> "Tilly's Desk"
                        TopLevelDestination.STATISTICS -> "Statistics"
                        TopLevelDestination.MY -> "My"
                    },
                )
            }
        },
        bottomBar = {
            if (appState.currentTopLevelDestination != null) {
                AppBottomBarWithFab(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    onFabClick = {
                        appState.navController.navigateToEditor()
                    },
                )
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NavHost(
                navController = appState.navController,
                startDestination = Home,
                enterTransition = { fadeIn(animationSpec = androidx.compose.animation.core.tween(200)) },
                exitTransition = { fadeOut(animationSpec = androidx.compose.animation.core.tween(200)) },
                popEnterTransition = { fadeIn(animationSpec = androidx.compose.animation.core.tween(200)) },
                popExitTransition = { fadeOut(animationSpec = androidx.compose.animation.core.tween(200)) },
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
                tilDetailScreen(
                    onBackClick = { appState.navController.popBackStack() },
                    onDeleteClick = {
                        appState.navController.popBackStack()
                    },
                    onEditClick = { tilId ->
                        appState.navController.navigateToEditor(tilId)
                    }
                )
                statisticsScreen()
                shopScreen()
                myPageScreen()
                editorScreen(
                    onBackClick = { appState.navController.popBackStack() },
                    onShowDetail = { tilId ->
                        appState.navController.navigate(TilDetail(tilId, fromSave = true)) {
                            popUpTo<EditorRoute> { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

/**
 * 바텀 네비게이션 + 중앙 FAB
 * 피그마 기준: 홈 | 상점 | (+FAB) | 통계 | 마이
 */
@Composable
private fun AppBottomBarWithFab(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navBarColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
    )

    Box(modifier = modifier) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 0.dp,
        ) {
            destinations.forEachIndexed { index, destination ->
                // 상점(1)과 통계(2) 사이에 FAB 공간
                if (index == 2) {
                    Spacer(modifier = Modifier.width(48.dp))
                }

                val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigateToDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    colors = navBarColors,
                )
            }
        }

        // 중앙 FAB
        TillyFab(
            onClick = onFabClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-24).dp),
        )
    }
}

@SuppressLint("RestrictedApi")
private fun NavDestination?.isTopLevelDestinationInHierarchy(
    destination: TopLevelDestination
) = this?.hierarchy?.any {
    when (destination) {
        TopLevelDestination.HOME -> it.hasRoute<Home>()
        TopLevelDestination.SHOP -> it.hasRoute<ShopRoute>()
        TopLevelDestination.STATISTICS -> it.hasRoute<StatisticsRoute>()
        TopLevelDestination.MY -> it.hasRoute<MyPageRoute>()
    }
} ?: false
