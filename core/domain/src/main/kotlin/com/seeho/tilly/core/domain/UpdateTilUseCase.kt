package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.TilRepository
import com.seeho.tilly.core.model.Til
import javax.inject.Inject

/**
 * TIL을 수정하는 UseCase
 * Editor 화면(수정 모드)에서 사용
 */
class UpdateTilUseCase @Inject constructor(
    private val tilRepository: TilRepository,
) {
    suspend operator fun invoke(til: Til) {
        return tilRepository.updateTil(til)
    }
}
