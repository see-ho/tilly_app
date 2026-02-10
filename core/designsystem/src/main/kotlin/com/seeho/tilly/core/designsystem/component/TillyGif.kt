package com.seeho.tilly.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun GifAsset(
    @DrawableRes gifRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = gifRes,
        contentDescription = contentDescription,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Fit,
        filterQuality = FilterQuality.None
    )
}