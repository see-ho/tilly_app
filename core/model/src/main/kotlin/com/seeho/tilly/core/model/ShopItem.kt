package com.seeho.tilly.core.model

/**
 * 상점 아이템 도메인 모델
 */
data class ShopItem(
    val id: String,              // 아이템 고유 ID (예: "monitor_retro", "theme_dracula")
    val name: String,            // 표시 이름 (예: "레트로 모니터", "Dracula")
    val description: String,     // 설명
    val category: ItemCategory,
    val price: Int,              // 코인 가격 (0이면 기본 제공)
    val imageResName: String,    // drawable 리소스 이름
    val isAnimated: Boolean = false, // GIF 애니메이션 아이템 여부
    val isDefault: Boolean = false, // 기본 제공 아이템 여부
)
