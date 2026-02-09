package com.seeho.tilly.core.designsystem.theme

import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun TillyTheme(
    appTheme: AppTheme = AppTheme.TILLY_GREEN,
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> getColorScheme(appTheme, darkTheme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TillyTypography,
        shapes = TillyShapes,
        content = content
    )
}

/**
 * TILLY Color Schemes
 * Light and Dark color schemes for each theme
 */

// ============================================
// Helper Functions for Color Schemes
// ============================================

private fun tillyLightColorScheme(
    primary: Color,
    onPrimary: Color,
    primaryContainer: Color,
    onPrimaryContainer: Color,
    tertiary: Color,
    onTertiary: Color,
    tertiaryContainer: Color,
    onTertiaryContainer: Color,
) = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    secondary = NeoGray200,
    onSecondary = Color(0xFF030213),
    secondaryContainer = NeoGray100,
    onSecondaryContainer = Color(0xFF030213),
    tertiary = tertiary,
    onTertiary = onTertiary,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    error = NeoDestructive,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color.White,
    onBackground = Color(0xFF030213),
    surface = Color.White,
    onSurface = Color(0xFF030213),
    surfaceVariant = NeoGray50,
    onSurfaceVariant = NeoGray400,
    outline = Color(0x1A000000),
    outlineVariant = NeoDarkBorder,
)

// ============================================
// Light Color Schemes
// ============================================

// Tilly Green Light Scheme
internal val TillyGreenLightScheme = tillyLightColorScheme(
    primary = NeoTerminalGreen,
    onPrimary = NeoDarkBase,
    primaryContainer = Color(0xFFE0F8F0),
    onPrimaryContainer = Color(0xFF003822),
    tertiary = Color(0xFF9C27B0),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF3E5F5),
    onTertiaryContainer = Color(0xFF4A148C),
)

// Dracula Light Scheme
internal val DraculaLightScheme = tillyLightColorScheme(
    primary = DraculaPink,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF3E5F5),
    onPrimaryContainer = Color(0xFF4A148C),
    tertiary = DraculaCyan,
    onTertiary = NeoDarkBase,
    tertiaryContainer = Color(0xFFE0F8F0),
    onTertiaryContainer = Color(0xFF003822),
)

// Monokai Light Scheme
internal val MonokaiLightScheme = tillyLightColorScheme(
    primary = MonokaiPink,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0B2),
    onPrimaryContainer = Color(0xFFE65100),
    tertiary = MonokaiGreen,
    onTertiary = NeoDarkBase,
    tertiaryContainer = Color(0xFFE0F8F0),
    onTertiaryContainer = Color(0xFF003822),
)

// One Dark Light Scheme
internal val OneDarkLightScheme = tillyLightColorScheme(
    primary = OneDarkBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB),
    onPrimaryContainer = Color(0xFF0D47A1),
    tertiary = OneDarkGreen,
    onTertiary = NeoDarkBase,
    tertiaryContainer = Color(0xFFE0F8F0),
    onTertiaryContainer = Color(0xFF003822),
)

// Nord Light Scheme
internal val NordLightScheme = tillyLightColorScheme(
    primary = NordFrost2,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB),
    onPrimaryContainer = Color(0xFF0D47A1),
    tertiary = NordAurora3,
    onTertiary = NeoDarkBase,
    tertiaryContainer = Color(0xFFE0F8F0),
    onTertiaryContainer = Color(0xFF003822),
)

// Gruvbox Light Scheme
internal val GruvboxLightScheme = tillyLightColorScheme(
    primary = GruvboxAqua,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB),
    onPrimaryContainer = Color(0xFF0D47A1),
    tertiary = GruvboxOrange,
    onTertiary = NeoDarkBase,
    tertiaryContainer = Color(0xFFE0F8F0),
    onTertiaryContainer = Color(0xFF003822),
)

// ============================================
// Dark Color Schemes
// ============================================

// Tilly Green Dark Scheme
internal val TillyGreenDarkScheme = darkColorScheme(
    primary = NeoTerminalGreen,
    onPrimary = NeoDarkBase,
    primaryContainer = NeoTerminalGreenDim,
    onPrimaryContainer = NeoTerminalGreenBright,

    secondary = NeoDarkSurface,
    onSecondary = Color(0xFFFCFCFC),
    secondaryContainer = NeoDarkBorder,
    onSecondaryContainer = Color(0xFFFCFCFC),

    tertiary = Color(0xFF9C27B0),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF7B1FA2),
    onTertiaryContainer = Color(0xFFF3E5F5),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = NeoDarkBase,
    onBackground = Color(0xFFFCFCFC),

    surface = NeoDarkBase,
    onSurface = Color(0xFFFCFCFC),
    surfaceVariant = NeoDarkSurface,
    onSurfaceVariant = Color(0xFFB5B5B5),

    outline = NeoDarkBorder,
    outlineVariant = Color(0xFF454545),
)

// Dracula Dark Scheme
internal val DraculaDarkScheme = darkColorScheme(
    primary = DraculaPink,
    onPrimary = DraculaBackground,
    primaryContainer = DraculaPurple,
    onPrimaryContainer = DraculaForeground,

    secondary = DraculaCurrentLine,
    onSecondary = DraculaForeground,
    secondaryContainer = DraculaComment,
    onSecondaryContainer = DraculaForeground,

    tertiary = DraculaCyan,
    onTertiary = DraculaBackground,
    tertiaryContainer = DraculaGreen,
    onTertiaryContainer = DraculaForeground,

    error = DraculaRed,
    onError = DraculaBackground,
    errorContainer = DraculaOrange,
    onErrorContainer = DraculaForeground,

    background = DraculaBackground,
    onBackground = DraculaForeground,

    surface = DraculaBackground,
    onSurface = DraculaForeground,
    surfaceVariant = DraculaCurrentLine,
    onSurfaceVariant = DraculaForeground,

    outline = DraculaComment,
    outlineVariant = DraculaCurrentLine,
)

// Monokai Dark Scheme
internal val MonokaiDarkScheme = darkColorScheme(
    primary = MonokaiPink,
    onPrimary = MonokaiBackground,
    primaryContainer = MonokaiPurple,
    onPrimaryContainer = MonokaiForeground,

    secondary = MonokaiBlack,
    onSecondary = MonokaiForeground,
    secondaryContainer = MonokaiComment,
    onSecondaryContainer = MonokaiForeground,

    tertiary = MonokaiCyan,
    onTertiary = MonokaiBackground,
    tertiaryContainer = MonokaiGreen,
    onTertiaryContainer = MonokaiForeground,

    error = MonokaiPink,
    onError = MonokaiBackground,
    errorContainer = MonokaiOrange,
    onErrorContainer = MonokaiForeground,

    background = MonokaiBackground,
    onBackground = MonokaiForeground,

    surface = MonokaiBackground,
    onSurface = MonokaiForeground,
    surfaceVariant = MonokaiBlack,
    onSurfaceVariant = MonokaiForeground,

    outline = MonokaiComment,
    outlineVariant = MonokaiBlack,
)

// One Dark Dark Scheme
internal val OneDarkDarkScheme = darkColorScheme(
    primary = OneDarkBlue,
    onPrimary = OneDarkBackground,
    primaryContainer = OneDarkCyan,
    onPrimaryContainer = OneDarkForeground,

    secondary = OneDarkBlack,
    onSecondary = OneDarkForeground,
    secondaryContainer = OneDarkComment,
    onSecondaryContainer = OneDarkForeground,

    tertiary = OneDarkGreen,
    onTertiary = OneDarkBackground,
    tertiaryContainer = OneDarkPurple,
    onTertiaryContainer = OneDarkForeground,

    error = OneDarkRed,
    onError = OneDarkBackground,
    errorContainer = OneDarkOrange,
    onErrorContainer = OneDarkForeground,

    background = OneDarkBackground,
    onBackground = OneDarkForeground,

    surface = OneDarkBackground,
    onSurface = OneDarkForeground,
    surfaceVariant = OneDarkBlack,
    onSurfaceVariant = OneDarkForeground,

    outline = OneDarkComment,
    outlineVariant = OneDarkBlack,
)

// Nord Dark Scheme
internal val NordDarkScheme = darkColorScheme(
    primary = NordFrost2,
    onPrimary = NordPolarNight0,
    primaryContainer = NordFrost3,
    onPrimaryContainer = NordSnowStorm2,

    secondary = NordPolarNight1,
    onSecondary = NordSnowStorm0,
    secondaryContainer = NordPolarNight3,
    onSecondaryContainer = NordSnowStorm1,

    tertiary = NordFrost0,
    onTertiary = NordPolarNight0,
    tertiaryContainer = NordAurora3,
    onTertiaryContainer = NordSnowStorm2,

    error = NordAurora0,
    onError = NordPolarNight0,
    errorContainer = NordAurora1,
    onErrorContainer = NordSnowStorm2,

    background = NordPolarNight0,
    onBackground = NordSnowStorm2,

    surface = NordPolarNight0,
    onSurface = NordSnowStorm2,
    surfaceVariant = NordPolarNight1,
    onSurfaceVariant = NordSnowStorm0,

    outline = NordPolarNight3,
    outlineVariant = NordPolarNight2,
)

// Gruvbox Dark Scheme
internal val GruvboxDarkScheme = darkColorScheme(
    primary = GruvboxAqua,
    onPrimary = GruvboxBackground,
    primaryContainer = GruvboxGreen,
    onPrimaryContainer = GruvboxForeground,

    secondary = GruvboxBackgroundHard,
    onSecondary = GruvboxForeground,
    secondaryContainer = GruvboxGray,
    onSecondaryContainer = GruvboxForeground,

    tertiary = GruvboxBlue,
    onTertiary = GruvboxBackground,
    tertiaryContainer = GruvboxPurple,
    onTertiaryContainer = GruvboxForeground,

    error = GruvboxRed,
    onError = GruvboxBackground,
    errorContainer = GruvboxOrange,
    onErrorContainer = GruvboxForeground,

    background = GruvboxBackground,
    onBackground = GruvboxForeground,

    surface = GruvboxBackground,
    onSurface = GruvboxForeground,
    surfaceVariant = GruvboxBackgroundHard,
    onSurfaceVariant = GruvboxForeground,

    outline = GruvboxGray,
    outlineVariant = GruvboxBackgroundHard,
)

// ============================================
// Helper Functions
// ============================================

/**
 * Get ColorScheme based on theme and dark mode
 */
internal fun getColorScheme(theme: AppTheme, isDarkTheme: Boolean) = when (theme) {
    AppTheme.TILLY_GREEN -> if (isDarkTheme) TillyGreenDarkScheme else TillyGreenLightScheme
    AppTheme.DRACULA -> if (isDarkTheme) DraculaDarkScheme else DraculaLightScheme
    AppTheme.MONOKAI -> if (isDarkTheme) MonokaiDarkScheme else MonokaiLightScheme
    AppTheme.ONE_DARK -> if (isDarkTheme) OneDarkDarkScheme else OneDarkLightScheme
    AppTheme.NORD -> if (isDarkTheme) NordDarkScheme else NordLightScheme
    AppTheme.GRUVBOX -> if (isDarkTheme) GruvboxDarkScheme else GruvboxLightScheme
}