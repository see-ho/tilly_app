package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.CoinRepository
import javax.inject.Inject

/**
 * 범용 코인 추가 UseCase
 */
class AddCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {
    suspend operator fun invoke(amount: Int) {
        coinRepository.addCoins(amount)
    }
}
