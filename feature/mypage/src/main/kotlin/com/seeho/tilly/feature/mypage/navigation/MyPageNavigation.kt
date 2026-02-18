package com.seeho.tilly.feature.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.seeho.tilly.feature.mypage.MyPageScreen
import kotlinx.serialization.Serializable

@Serializable
data object MyPageRoute

fun NavController.navigateToMyPage() = navigate(MyPageRoute)

fun NavGraphBuilder.myPageScreen() {
    composable<MyPageRoute> {
        MyPageScreen()
    }
}
