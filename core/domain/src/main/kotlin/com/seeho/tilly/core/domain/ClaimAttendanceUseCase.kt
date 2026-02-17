package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.CoinRepository
import javax.inject.Inject

/**
 * 출석 보상 수령 UseCase
 * 앱 접속 시 호출하여 일일 출석 보상(5코인) 지급
 */
class ClaimAttendanceUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {
    /** @return 보상 지급 성공 여부 (이미 수령한 경우 false) */
    suspend operator fun invoke(): Boolean {
        return coinRepository.claimAttendance()
    }
}
