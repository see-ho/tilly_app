package com.seeho.tilly.feature.editor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.seeho.tilly.feature.editor.EditorScreen
import kotlinx.serialization.Serializable

@Serializable
data class EditorRoute(val tilId: Long = -1L) // -1L = 생성 모드

/**
 * Editor 화면으로 이동
 * @param tilId null 또는 -1이면 생성 모드, 그 외에는 수정 모드
 */
fun NavController.navigateToEditor(tilId: Long? = null) {
    navigate(EditorRoute(tilId = tilId ?: -1L))
}

fun NavGraphBuilder.editorScreen(
    onBackClick: () -> Unit,
    onShowDetail: (Long) -> Unit,
) {
    composable<EditorRoute> {
        EditorScreen(
            onBackClick = onBackClick,
            onShowDetail = onShowDetail,
        )
    }
}
