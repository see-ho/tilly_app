package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.CoinRepository
import com.seeho.tilly.core.model.UserCoin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 유저 코인 정보 조회 UseCase
 */
class GetUserCoinUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {
    operator fun invoke(): Flow<UserCoin> {
        return coinRepository.getUserCoin()
    }
}
