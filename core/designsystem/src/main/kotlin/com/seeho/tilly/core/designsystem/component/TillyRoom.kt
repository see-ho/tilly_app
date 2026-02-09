package com.seeho.tilly.core.designsystem.component

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import com.seeho.tilly.core.designsystem.R
import com.seeho.tilly.core.designsystem.theme.TillyTheme

/**
 * Tilly 캐릭터와 방의 아이템들을 레이어별로 겹쳐서 보여주는 컴포넌트
 * 추후 상점 시스템에서 아이템 ID를 넘겨받아 동적으로 변경할 수 있도록 설계
 */
@Composable
fun TillyRoom(
    modifier: Modifier = Modifier,
    @DrawableRes chairRes: Int = R.drawable.bg_chair_basic,
    @DrawableRes deskRes: Int = R.drawable.bg_desk_basic,
    @DrawableRes charRes: Int = R.drawable.gif_tilly,
    @DrawableRes comRes: Int = R.drawable.gif_obj_basic_com,
    @DrawableRes keyboardRes: Int = R.drawable.obj_keyboard_basic,
    @DrawableRes deskItemRes: Int? = null,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // 1. 의자
        PixelAsset(resId = chairRes)

        // 2. 책상
        PixelAsset(resId = deskRes)

        // 3. 키보드
        PixelAsset(resId = keyboardRes)

        // 4. 캐릭터
        GifAsset(gifRes = charRes)

        // 5. 컴퓨터
        GifAsset(gifRes = comRes)

        // 6. 책상 위 소품 (있을 경우만)
        deskItemRes?.let { itemRes ->
            PixelAsset(resId = itemRes)
        }
    }
}


@Composable
private fun PixelAsset(
    @DrawableRes resId: Int,
    modifier: Modifier = Modifier
) {
    Image(
        bitmap = ImageBitmap.imageResource(id = resId),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        filterQuality = FilterQuality.None
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TillyRoomPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(
                modifier = Modifier.size(300.dp),
                contentAlignment = Alignment.Center
            ) {
                TillyRoom()
            }
        }
    }
}
