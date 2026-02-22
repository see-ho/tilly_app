package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.CoinRepository
import com.seeho.tilly.core.model.RewardResult
import javax.inject.Inject

/**
 * 출석 보상 수령 UseCase
 * 앱 접속 시 호출하여 일일 출석 보상 지급
 */
class ClaimAttendanceUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {
    /** @return 보상 결과 (보상 항목 리스트), 이미 수령한 경우 null */
    suspend operator fun invoke(): RewardResult? {
        return coinRepository.claimAttendance()
    }
}
