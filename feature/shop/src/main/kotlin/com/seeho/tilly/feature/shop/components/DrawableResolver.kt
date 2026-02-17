package com.seeho.tilly.feature.shop.components

import android.content.Context

/**
 * drawable 리소스 이름 → drawable ID 변환 유틸
 * "obj_com_basic" 같은 리소스 이름을 R.drawable.obj_com_basic의 ID로 변환
 */
fun resolveDrawableId(context: Context, resName: String?): Int {
    if (resName.isNullOrBlank()) return 0
    return context.resources.getIdentifier(resName, "drawable", context.packageName)
}
