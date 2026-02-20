package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.TilRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 전체 TIL 작성 수를 반환하는 UseCase
 */
class GetTotalTilCountUseCase @Inject constructor(
    private val tilRepository: TilRepository,
) {
    operator fun invoke(): Flow<Int> {
        return tilRepository.getTilCount()
    }
}
