package com.seeho.tilly.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TIL(Today I Learned)의 Room Entity
 */
@Entity(tableName = "tils")
data class TilEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,                    // 자동 생성 PK
    val title: String,                   // TIL 제목
    val learned: String,                 // 오늘 배운 것 (필수)
    val difficulty: String? = null,      // 어려웠던 점 (선택)
    val tomorrow: String? = null,        // 내일 할 일 (선택)
    val tags: String = "[]",             // 태그 리스트 (JSON 문자열로 저장)
    val emotion: String? = null,         // AI 분석: 감정 키워드
    val emotionScore: Int? = null,       // AI 분석: 감정 점수 (1-5)
    val difficultyLevel: String? = null, // AI 분석: 체감 난이도
    val createdAt: Long,                 // 생성 일시 (milliseconds)
    val updatedAt: Long? = null          // 수정 일시 (milliseconds)
)
