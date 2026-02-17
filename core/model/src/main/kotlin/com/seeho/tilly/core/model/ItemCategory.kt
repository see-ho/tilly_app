package com.seeho.tilly.core.model

/**
 * 상점 아이템 카테고리
 */
enum class ItemCategory(val label: String) {
    EQUIPMENT("장비"),    // 모니터
    KEYBOARD("키보드"),   // 키보드
    DESK("책상"),
    CHAIR("의자"),
    DECORATION("장식"),   // 머그컵 등
    THEME("테마"),        // 코드 에디터 테마
}
