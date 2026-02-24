package com.seeho.tilly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.domain.GetAllTilsUseCase
import com.seeho.tilly.core.domain.GetShopItemsUseCase
import com.seeho.tilly.core.designsystem.theme.AppTheme
import com.seeho.tilly.core.designsystem.theme.themeIdToAppTheme
import com.seeho.tilly.core.model.ItemCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * 앱 전역 상태 관리 ViewModel
 * - 장착된 테마 아이템 ID를 AppTheme으로 변환하여 제공
 * - 오늘 작성한 TIL ID 관리 (FAB 상태 전환용)
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    getShopItemsUseCase: GetShopItemsUseCase,
    getAllTilsUseCase: GetAllTilsUseCase,
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

    /** 오늘 작성한 TIL의 ID (없으면 null) — FAB 상태 전환에 사용 */
    val todayTilId: StateFlow<Long?> = getAllTilsUseCase()
        .map { tils ->
            val today = LocalDate.now()
            tils.firstOrNull { til ->
                Instant.ofEpochMilli(til.createdAt)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate() == today
            }?.id
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
}

