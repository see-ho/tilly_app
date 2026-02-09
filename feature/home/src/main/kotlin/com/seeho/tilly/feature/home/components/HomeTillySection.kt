package com.seeho.tilly.feature.home.components

import android.content.res.Configuration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.seeho.tilly.core.designsystem.component.TillyRoom
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.seeho.tilly.core.designsystem.theme.TillyTheme

@Composable
fun HomeTillySection(
    onShopClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp) // 상단 여백 보정
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .clickable { onShopClick() },
        contentAlignment = Alignment.Center
    ) {
        TillyRoom(
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeTillySectionPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeTillySection(onShopClick = {})
        }
    }
}
