package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.ShopRepository
import com.seeho.tilly.core.model.ItemCategory
import javax.inject.Inject

/**
 * 아이템 장착 UseCase
 * 카테고리별로 하나의 아이템만 장착 가능
 */
class EquipItemUseCase @Inject constructor(
    private val shopRepository: ShopRepository,
) {
    suspend operator fun invoke(category: ItemCategory, itemId: String) {
        shopRepository.equipItem(category, itemId)
    }
}
