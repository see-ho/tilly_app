package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.TilRepository
import com.seeho.tilly.core.model.Til
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 전체 TIL 목록을 조회하는 UseCase
 * Home 화면에서 사용
 */
class GetAllTilsUseCase @Inject constructor(
    private val tilRepository: TilRepository,
) {
    operator fun invoke(): Flow<List<Til>> {
        return tilRepository.getAllTils()
    }
}
