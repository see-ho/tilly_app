package com.seeho.tilly.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.seeho.tilly.feature.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object Home

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(Home, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onTilClick: (Long) -> Unit,
) {
    composable<Home> {
        HomeScreen(onTilClick = onTilClick)
    }
}