package com.seeho.tilly.feature.mypage.coinhistory

import com.seeho.tilly.core.model.CoinTransaction
import java.time.LocalDate

/**
 * 코인 히스토리 UI 상태
 */
data class CoinHistoryUiState(
    val currentBalance: Int = 0,
    val transactionsByDate: Map<LocalDate, List<CoinTransaction>> = emptyMap(),
    val isLoading: Boolean = true,
)
