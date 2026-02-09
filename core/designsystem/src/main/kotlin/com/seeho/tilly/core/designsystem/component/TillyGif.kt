package com.seeho.tilly.core.designsystem.component

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest

@Composable
fun GifAsset(
    @DrawableRes gifRes: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // GIF를 재생하기 위한 로더 설정
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                // 안드로이드 버전에 따라 디코더 분기 처리
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(gifRes)
            .build(),
        imageLoader = imageLoader,
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Fit,
        filterQuality = FilterQuality.None
    )
}