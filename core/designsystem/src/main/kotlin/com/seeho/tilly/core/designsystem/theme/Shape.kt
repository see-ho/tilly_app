package com.seeho.tilly.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * TILLY Shape System
 */

val TillyShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),   // --radius-sm: calc(10dp - 4dp)
    small = RoundedCornerShape(8.dp),        // --radius-md: calc(10dp - 2dp)
    medium = RoundedCornerShape(10.dp),      // --radius-lg: 10dp (0.625rem)
    large = RoundedCornerShape(14.dp),       // --radius-xl: calc(10dp + 4dp)
    extraLarge = RoundedCornerShape(20.dp)   // For large cards and containers
)

// ============================================
// Custom Shapes
// ============================================

// Pixel Border Style (for retro-styled elements)
val PixelBorderShape = RoundedCornerShape(2.dp)

// Card shapes with specific corners
val CardShapeTop = RoundedCornerShape(
    topStart = 12.dp,
    topEnd = 12.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)

val CardShapeBottom = RoundedCornerShape(
    topStart = 0.dp,
    topEnd = 0.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)

// Floating Action Button shape
val FABShape = RoundedCornerShape(50)

// Bottom Sheet shape
val BottomSheetShape = RoundedCornerShape(
    topStart = 20.dp,
    topEnd = 20.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)

// Dialog shape
val DialogShape = RoundedCornerShape(16.dp)

// Chip/Tag shape
val ChipShape = TillyShapes.small

// Button shape (slightly rounded)
val ButtonShape = TillyShapes.medium

// Input field shape
val InputShape = TillyShapes.small
