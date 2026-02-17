package com.seeho.tilly.core.data.mapper

import com.seeho.tilly.core.database.entity.CoinEntity
import com.seeho.tilly.core.model.UserCoin

fun CoinEntity.toModel(): UserCoin = UserCoin(
    balance = balance,
    lastClaimedDate = lastClaimedDate,
    dailyTilClaimed = dailyTilClaimed,
    dailyAttendanceClaimed = dailyAttendanceClaimed,
    dailyAdWatchCount = dailyAdWatchCount,
    streakCount = streakCount,
)
