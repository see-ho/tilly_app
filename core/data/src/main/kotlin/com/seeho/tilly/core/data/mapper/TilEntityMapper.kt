package com.seeho.tilly.core.data.mapper

import com.seeho.tilly.core.database.entity.TilEntity
import com.seeho.tilly.core.model.Til

/**
 * TilEntity ↔ Til 모델 간 변환 확장함수
 */

/** Entity → 도메인 모델 변환 */
fun TilEntity.toModel(): Til {
    return Til(
        id = id,
        title = title,
        learned = learned,
        difficulty = difficulty,
        tomorrow = tomorrow,
        tags = tags,
        emotion = emotion,
        emotionScore = emotionScore,
        difficultyLevel = difficultyLevel,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

/** 도메인 모델 → Entity 변환 */
fun Til.toEntity(): TilEntity {
    return TilEntity(
        id = id,
        title = title,
        learned = learned,
        difficulty = difficulty,
        tomorrow = tomorrow,
        tags = tags,
        emotion = emotion,
        emotionScore = emotionScore,
        difficultyLevel = difficultyLevel,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
