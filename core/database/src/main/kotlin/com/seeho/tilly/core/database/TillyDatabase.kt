package com.seeho.tilly.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import com.seeho.tilly.core.database.converter.TagListConverter
import com.seeho.tilly.core.database.dao.CoinDao
import com.seeho.tilly.core.database.dao.CoinTransactionDao
import com.seeho.tilly.core.database.dao.RetrospectiveDao
import com.seeho.tilly.core.database.dao.ShopDao
import com.seeho.tilly.core.database.dao.TilDao
import com.seeho.tilly.core.database.entity.CoinEntity
import com.seeho.tilly.core.database.entity.CoinTransactionEntity
import com.seeho.tilly.core.database.entity.EquippedItemEntity
import com.seeho.tilly.core.database.entity.PurchasedItemEntity
import com.seeho.tilly.core.database.entity.RetrospectiveEntity
import com.seeho.tilly.core.database.entity.TilEntity

/**
 * Tilly 앱의 Room Database
 * 버전 1: TIL 테이블만
 * 버전 2: 월간 회고 테이블 추가
 * 버전 3: 코인 + 상점 테이블 추가
 * 버전 4: 코인 획득/사용 기록 테이블 추가
 */
@Database(
    entities = [
        TilEntity::class,
        RetrospectiveEntity::class,
        CoinEntity::class,
        PurchasedItemEntity::class,
        EquippedItemEntity::class,
        CoinTransactionEntity::class,
    ],
    version = 5,
    exportSchema = true,
)
@TypeConverters(TagListConverter::class)
abstract class TillyDatabase : RoomDatabase() {

    abstract fun tilDao(): TilDao
    abstract fun retrospectiveDao(): RetrospectiveDao
    abstract fun coinDao(): CoinDao
    abstract fun coinTransactionDao(): CoinTransactionDao
    abstract fun shopDao(): ShopDao
}

suspend fun <T> TillyDatabase.withDatabaseTransaction(block: suspend () -> T): T =
    withTransaction { block() }
