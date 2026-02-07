package com.seeho.tilly.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationRoute

@Serializable
data class TilDetail(val tilId: Long) : NavigationRoute
