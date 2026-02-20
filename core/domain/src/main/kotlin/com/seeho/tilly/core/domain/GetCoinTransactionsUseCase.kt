package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.CoinRepository
import com.seeho.tilly.core.model.CoinTransaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 코인 거래 내역 조회 UseCase
 */
class GetCoinTransactionsUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {
    operator fun invoke(): Flow<List<CoinTransaction>> {
        return coinRepository.getCoinTransactions()
    }
}
