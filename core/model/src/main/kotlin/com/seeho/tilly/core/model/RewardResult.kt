package com.seeho.tilly.core.model

data class RewardResult(
    val rewards: List<RewardItem>,
) {
    val totalAmount: Int get() = rewards.sumOf { it.amount }
}

data class RewardItem(
    val amount: Int,
    val description: String,
)
