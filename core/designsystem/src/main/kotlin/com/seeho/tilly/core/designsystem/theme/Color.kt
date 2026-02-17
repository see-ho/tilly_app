package com.seeho.tilly.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * TILLY - Neo-Retro Developer Theme
 * Material 3 Color System
 */

// ============================================
// Theme Enum
// ============================================
enum class AppTheme {
    TILLY_GREEN,    // Default - Terminal Green
    DRACULA,        // Dracula theme - Purple/Pink
    MONOKAI,        // Monokai - Orange/Yellow/Green
    ONE_DARK,       // One Dark (Atom/VSCode) - Blue
    NORD,           // Nord - Arctic/Blue
    GRUVBOX         // Gruvbox - Retro/Warm
}

// ============================================
// Base Neo-Retro Colors (Tilly Green Theme)
// ============================================
// Dark Base Colors
val NeoDarkBase = Color(0xFF121212)
val NeoDarkSurface = Color(0xFF1E1E1E)
val NeoDarkSurfaceVariant = Color(0xFF0D0D0D)
val NeoDarkBorder = Color(0xFF2A2A2A)

// Terminal Green (Primary Theme)
val NeoTerminalGreen = Color(0xFF00E676)
val NeoTerminalGreenDim = Color(0xFF00C853)
val NeoTerminalGreenBright = Color(0xFF69F0AE)

// Neutral Colors
val NeoGray50 = Color(0xFFF3F3F5)
val NeoGray100 = Color(0xFFECECF0)
val NeoGray200 = Color(0xFFE9EBEF)
val NeoGray300 = Color(0xFFCBCED4)
val NeoGray400 = Color(0xFF717182)
val NeoSubtext = Color(0xFF99A0AF) // 차트 보조 텍스트, 지시선 등

// Semantic Colors
val NeoDestructive = Color(0xFFD4183D)
val NeoSuccess = Color(0xFF00E676)
val NeoWarning = Color(0xFFFF9800)

// ============================================
// Alternative Theme Colors
// ============================================
// Dracula Theme Colors (Official Dracula)
val DraculaBackground = Color(0xFF282A36)
val DraculaCurrentLine = Color(0xFF44475A)
val DraculaForeground = Color(0xFFF8F8F2)
val DraculaComment = Color(0xFF6272A4)
val DraculaCyan = Color(0xFF8BE9FD)
val DraculaGreen = Color(0xFF50FA7B)
val DraculaOrange = Color(0xFFFFB86C)
val DraculaPink = Color(0xFFFF79C6)
val DraculaPurple = Color(0xFFBD93F9)
val DraculaRed = Color(0xFFFF5555)
val DraculaYellow = Color(0xFFF1FA8C)

// Monokai Theme Colors (Official Monokai Pro)
val MonokaiBackground = Color(0xFF272822)
val MonokaiBlack = Color(0xFF1E1F1C)
val MonokaiForeground = Color(0xFFF8F8F2)
val MonokaiComment = Color(0xFF75715E)
val MonokaiGreen = Color(0xFFA6E22E)
val MonokaiOrange = Color(0xFFFD971F)
val MonokaiPink = Color(0xFFF92672)
val MonokaiPurple = Color(0xFFAE81FF)
val MonokaiYellow = Color(0xFFE6DB74)
val MonokaiCyan = Color(0xFF66D9EF)

// One Dark Theme Colors (Atom/VSCode One Dark)
val OneDarkBackground = Color(0xFF282C34)
val OneDarkBlack = Color(0xFF21252B)
val OneDarkForeground = Color(0xFFABB2BF)
val OneDarkComment = Color(0xFF5C6370)
val OneDarkRed = Color(0xFFE06C75)
val OneDarkOrange = Color(0xFFD19A66)
val OneDarkYellow = Color(0xFFE5C07B)
val OneDarkGreen = Color(0xFF98C379)
val OneDarkCyan = Color(0xFF56B6C2)
val OneDarkBlue = Color(0xFF61AFEF)
val OneDarkPurple = Color(0xFFC678DD)

// Nord Theme Colors (Official Nord)
val NordPolarNight0 = Color(0xFF2E3440)
val NordPolarNight1 = Color(0xFF3B4252)
val NordPolarNight2 = Color(0xFF434C5E)
val NordPolarNight3 = Color(0xFF4C566A)
val NordSnowStorm0 = Color(0xFFD8DEE9)
val NordSnowStorm1 = Color(0xFFE5E9F0)
val NordSnowStorm2 = Color(0xFFECEFF4)
val NordFrost0 = Color(0xFF8FBCBB)
val NordFrost1 = Color(0xFF88C0D0)
val NordFrost2 = Color(0xFF81A1C1)
val NordFrost3 = Color(0xFF5E81AC)
val NordAurora0 = Color(0xFFBF616A)
val NordAurora1 = Color(0xFFD08770)
val NordAurora2 = Color(0xFFEBCB8B)
val NordAurora3 = Color(0xFFA3BE8C)
val NordAurora4 = Color(0xFFB48EAD)

// Gruvbox Theme Colors (Official Gruvbox Dark)
val GruvboxBackground = Color(0xFF282828)
val GruvboxBackgroundHard = Color(0xFF1D2021)
val GruvboxForeground = Color(0xFFEBDBB2)
val GruvboxGray = Color(0xFF928374)
val GruvboxRed = Color(0xFFFB4934)
val GruvboxGreen = Color(0xFFB8BB26)
val GruvboxYellow = Color(0xFFFABD2F)
val GruvboxBlue = Color(0xFF83A598)
val GruvboxPurple = Color(0xFFD3869B)
val GruvboxAqua = Color(0xFF8EC07C)
val GruvboxOrange = Color(0xFFFE8019)

// ============================================
// Emotion/Analysis Colors (for TIL Analysis)
// ============================================
val EmotionLevel1 = Color(0xFF0F9950)  // 최저
val EmotionLevel2 = Color(0xFF119F54)
val EmotionLevel3 = Color(0xFF71B666)
val EmotionLevel4 = Color(0xFF86B868)
val EmotionLevel5 = Color(0xFFDAC670)  // 최고

// Emotion Enum Colors
val EmotionAchievement = Color(0xFF4CAF50)
val EmotionSatisfaction = Color(0xFF2196F3)
val EmotionNormal = Color(0xFF9E9E9E)
val EmotionHard = Color(0xFFFF9800)
val EmotionFrustration = Color(0xFFF44336)

// Difficulty Colors
val DifficultyEasy = Color(0xFF4CAF50)
val DifficultyNormal = Color(0xFFFFEB3B)
val DifficultyHard = Color(0xFFFF9800)
val DifficultyVeryHard = Color(0xFFF44336)

// ============================================
// Chart Colors (for Statistics)
// ============================================
val ChartGreen = Color(0xFF00E676)
val ChartCyan = Color(0xFF8BE9FD)
val ChartPurple = Color(0xFFBD93F9)
val ChartPink = Color(0xFFFF79C6)
val ChartOrange = Color(0xFFFFB86C)
val ChartBlue = Color(0xFF36A2EB)
val ChartYellow = Color(0xFFF1FA8C)
val ChartRed = Color(0xFFFF5555)

// 차트용 컬러 팔레트
val ChartPalette = listOf(
    ChartGreen, ChartCyan, ChartPurple, ChartPink,
    ChartOrange, ChartBlue, ChartYellow, ChartRed,
)
