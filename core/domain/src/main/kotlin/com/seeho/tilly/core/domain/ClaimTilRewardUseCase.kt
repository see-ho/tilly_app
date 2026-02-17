package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.CoinRepository
import javax.inject.Inject

/**
 * TIL 작성 보상 수령 UseCase
 * TIL 저장 성공 후 호출하여 코인(20코인) 지급
 */
class ClaimTilRewardUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {
    /** @return 보상 지급 성공 여부 (이미 수령한 경우 false) */
    suspend operator fun invoke(): Boolean {
        return coinRepository.claimTilReward()
    }
}
