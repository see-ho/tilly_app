package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.TilRepository
import com.seeho.tilly.core.model.Til
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ID로 단일 TIL을 조회하는 UseCase
 * Detail, Editor(수정 모드) 화면에서 사용
 */
class GetTilByIdUseCase @Inject constructor(
    private val tilRepository: TilRepository,
) {
    operator fun invoke(id: Long): Flow<Til?> {
        return tilRepository.getTilById(id)
    }
}
