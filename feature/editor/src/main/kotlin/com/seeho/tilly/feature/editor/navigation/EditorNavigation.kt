package com.seeho.tilly.feature.editor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.seeho.tilly.feature.editor.EditorScreen
import kotlinx.serialization.Serializable

@Serializable
data object EditorRoute

fun NavController.navigateToEditor() = navigate(EditorRoute)

fun NavGraphBuilder.editorScreen() {
    composable<EditorRoute> {
        EditorScreen()
    }
}
