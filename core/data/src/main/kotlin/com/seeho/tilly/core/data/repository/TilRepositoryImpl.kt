package com.seeho.tilly.core.data.repository

import com.seeho.tilly.core.data.mapper.toEntity
import com.seeho.tilly.core.data.mapper.toModel
import com.seeho.tilly.core.database.dao.TilDao
import com.seeho.tilly.core.domain.repository.TilRepository
import com.seeho.tilly.core.model.Til
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * TilRepository 구현체
 * Room DAO를 통해 실제 DB 작업 수행, Entity ↔ Model 변환 담당
 */
class TilRepositoryImpl @Inject constructor(
    private val tilDao: TilDao,
) : TilRepository {

    override fun getAllTils(): Flow<List<Til>> {
        return tilDao.getAllTils().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getTilById(id: Long): Flow<Til?> {
        return tilDao.getTilById(id).map { entity ->
            entity?.toModel()
        }
    }

    override suspend fun insertTil(til: Til): Long {
        return tilDao.insertTil(til.toEntity())
    }

    override suspend fun updateTil(til: Til) {
        tilDao.updateTil(til.toEntity())
    }

    override suspend fun deleteTilById(id: Long) {
        tilDao.deleteTilById(id)
    }
}
