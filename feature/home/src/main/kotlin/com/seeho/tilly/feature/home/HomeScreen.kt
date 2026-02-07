package com.seeho.tilly.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onTilClick: (Long) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Home (Click)",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                // UI는 단순히 ViewModel 함수 호출만
                viewModel.onTilClick(12345)
                // 실제 이동은 콜백으로 상위(NavHost)에 위임
                onTilClick(12345)
            }
        )
    }
}
