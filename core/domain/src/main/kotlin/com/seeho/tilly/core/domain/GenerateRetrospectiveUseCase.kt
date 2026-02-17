package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.AiAnalysisRepository
import com.seeho.tilly.core.model.MonthlyRetrospective
import com.seeho.tilly.core.model.Til
import javax.inject.Inject

class GenerateRetrospectiveUseCase @Inject constructor(
    private val aiAnalysisRepository: AiAnalysisRepository,
) {
    suspend operator fun invoke(
        month: Int,
        year: Int,
        tils: List<Til>,
    ): Result<MonthlyRetrospective> {
        return aiAnalysisRepository.generateRetrospective(month, year, tils)
    }
}
