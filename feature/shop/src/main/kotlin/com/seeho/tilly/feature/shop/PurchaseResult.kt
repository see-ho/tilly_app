package com.seeho.tilly.feature.shop

/**
 * 아이템 구매 결과 이벤트
 */
sealed class PurchaseResult {
    data class Success(val itemName: String) : PurchaseResult()
    data object InsufficientFunds : PurchaseResult()
}
