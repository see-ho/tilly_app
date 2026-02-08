package com.seeho.tilly.feature.tildetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.seeho.tilly.core.navigation.TilDetail
import com.seeho.tilly.feature.tildetails.TilDetailScreen

fun NavController.navigateToTilDetail(tilId: Long) = navigate(TilDetail(tilId))

fun NavGraphBuilder.tilDetailScreen() {
    composable<TilDetail> { backStackEntry ->
        val detail: TilDetail = backStackEntry.toRoute()
        TilDetailScreen(tilDetail = detail)
    }
}
