package com.seeho.tilly.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Tilly 앱 전용 SnackbarHost
 */
@Composable
fun TillySnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { data ->
        Snackbar(
            snackbarData = data,
            containerColor = Color.Black,
            contentColor = Color.White,
            actionColor = Color.White,
            shape = MaterialTheme.shapes.medium,
        )
    }
}
