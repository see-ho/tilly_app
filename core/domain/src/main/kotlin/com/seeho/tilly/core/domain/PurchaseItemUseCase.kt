package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.ShopRepository
import javax.inject.Inject

/**
 * 아이템 구매 UseCase
 * 코인 차감 후 구매 기록 저장
 */
class PurchaseItemUseCase @Inject constructor(
    private val shopRepository: ShopRepository,
) {
    /** @return 구매 성공 여부 (잔액 부족 시 false) */
    suspend operator fun invoke(itemId: String, price: Int): Boolean {
        return shopRepository.purchaseItem(itemId, price)
    }
}
