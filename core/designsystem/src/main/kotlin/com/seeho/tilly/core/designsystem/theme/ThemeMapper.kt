package com.seeho.tilly.core.designsystem.theme

/**
 * 상점 테마 아이템 ID → AppTheme 매핑
 * ShopItemData의 테마 아이템 ID와 AppTheme enum을 연결
 */
fun themeIdToAppTheme(themeId: String?): AppTheme = when (themeId) {
    "theme_tilly_green" -> AppTheme.TILLY_GREEN
    "theme_dracula" -> AppTheme.DRACULA
    "theme_monokai" -> AppTheme.MONOKAI
    "theme_one_dark" -> AppTheme.ONE_DARK
    "theme_nord" -> AppTheme.NORD
    "theme_gruvbox" -> AppTheme.GRUVBOX
    else -> AppTheme.TILLY_GREEN // 기본 테마
}
