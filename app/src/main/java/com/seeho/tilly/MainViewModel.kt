package com.seeho.tilly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.domain.GetShopItemsUseCase
import com.seeho.tilly.core.designsystem.theme.AppTheme
import com.seeho.tilly.core.designsystem.theme.themeIdToAppTheme
import com.seeho.tilly.core.model.ItemCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * 앱 전역 테마 관리 ViewModel
 * 장착된 테마 아이템 ID를 AppTheme으로 변환하여 제공
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    getShopItemsUseCase: GetShopItemsUseCase,
) : ViewModel() {

    /** 현재 장착된 AppTheme (장착 테마 변경 시 자동 갱신) */
    val currentAppTheme: StateFlow<AppTheme> = getShopItemsUseCase.getEquippedItems()
        .map { equippedItems ->
            val themeId = equippedItems[ItemCategory.THEME]
            themeIdToAppTheme(themeId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppTheme.TILLY_GREEN,
        )
}
