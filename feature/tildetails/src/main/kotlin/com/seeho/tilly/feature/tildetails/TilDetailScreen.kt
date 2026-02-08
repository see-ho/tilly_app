package com.seeho.tilly.feature.tildetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.seeho.tilly.core.navigation.TilDetail

@Composable
fun TilDetailScreen(
    tilDetail: TilDetail,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "TIL Detail: ${tilDetail.tilId}")
    }
}
