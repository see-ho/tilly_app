package com.seeho.tilly.feature.report.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.seeho.tilly.feature.report.ReportScreen
import kotlinx.serialization.Serializable

@Serializable
data object ReportRoute

fun NavController.navigateToReport() = navigate(ReportRoute)

fun NavGraphBuilder.reportScreen() {
    composable<ReportRoute> {
        ReportScreen()
    }
}