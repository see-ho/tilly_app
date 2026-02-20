package com.seeho.tilly.feature.mypage.coinhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.domain.GetCoinTransactionsUseCase
import com.seeho.tilly.core.domain.GetUserCoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * 코인 히스토리 화면 ViewModel
 */
@HiltViewModel
class CoinHistoryViewModel @Inject constructor(
    getUserCoinUseCase: GetUserCoinUseCase,
    getCoinTransactionsUseCase: GetCoinTransactionsUseCase,
) : ViewModel() {

    val uiState: StateFlow<CoinHistoryUiState> = combine(
        getUserCoinUseCase(),
        getCoinTransactionsUseCase(),
    ) { userCoin, transactions ->
        // 날짜별 그룹핑
        val grouped = transactions.groupBy { it.createdAt.toLocalDate() }
            .toSortedMap(compareByDescending { it })

        CoinHistoryUiState(
            currentBalance = userCoin.balance,
            transactionsByDate = grouped,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CoinHistoryUiState(),
    )
}
