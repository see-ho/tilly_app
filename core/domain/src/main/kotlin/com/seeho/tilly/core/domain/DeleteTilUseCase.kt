package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.TilRepository
import javax.inject.Inject

/**
 * TIL을 삭제하는 UseCase
 * Detail 화면에서 사용
 */
class DeleteTilUseCase @Inject constructor(
    private val tilRepository: TilRepository,
) {
    suspend operator fun invoke(id: Long) {
        return tilRepository.deleteTilById(id)
    }
}
