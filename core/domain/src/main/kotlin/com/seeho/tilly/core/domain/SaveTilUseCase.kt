package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.TilRepository
import com.seeho.tilly.core.model.Til
import javax.inject.Inject

/**
 * 새 TIL을 저장하는 UseCase
 * Editor 화면(생성 모드)에서 사용
 */
class SaveTilUseCase @Inject constructor(
    private val tilRepository: TilRepository,
) {
    /** @return 생성된 TIL의 ID */
    suspend operator fun invoke(til: Til): Long {
        return tilRepository.insertTil(til)
    }
}
