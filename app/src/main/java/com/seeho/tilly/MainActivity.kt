package com.seeho.tilly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.ui.TillyApp

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()

        setContent {
            // 장착된 테마를 실시간 관찰 → 앱 전체 색상 즉시 변경
            val currentTheme by mainViewModel.currentAppTheme.collectAsStateWithLifecycle()

            TillyTheme(appTheme = currentTheme) {
                TillyApp()
            }
        }
    }
}