package com.seeho.tilly.feature.shop.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.seeho.tilly.feature.shop.ShopScreen
import kotlinx.serialization.Serializable

@Serializable
data object ShopRoute

fun NavController.navigateToShop() = navigate(ShopRoute)

fun NavGraphBuilder.shopScreen() {
    composable<ShopRoute> {
        ShopScreen()
    }
}
