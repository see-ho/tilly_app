package com.seeho.tilly.core.data.mapper

import com.seeho.tilly.core.database.entity.CoinTransactionEntity
import com.seeho.tilly.core.model.CoinTransaction
import com.seeho.tilly.core.model.CoinTransactionType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun CoinTransactionEntity.toModel(): CoinTransaction = CoinTransaction(
    id = id,
    amount = amount,
    type = try {
        CoinTransactionType.valueOf(type)
    } catch (_: IllegalArgumentException) {
        CoinTransactionType.UNKNOWN
    },
    description = description,
    balanceAfter = balanceAfter,
    createdAt = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(createdAt),
        ZoneId.systemDefault(),
    ),
)
