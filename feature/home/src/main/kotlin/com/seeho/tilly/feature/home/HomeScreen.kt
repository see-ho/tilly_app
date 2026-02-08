package com.seeho.tilly.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onTilClick: (Long) -> Unit,
    onEditorClick: () -> Unit,
    onShopClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Home",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.onTilClick(12345)
                onTilClick(12345)
            }) {
                Text("Go to detail")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onEditorClick) {
                Text("Go to Editor")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onShopClick) {
                Text("Go to Shop")
            }
        }
    }
}
