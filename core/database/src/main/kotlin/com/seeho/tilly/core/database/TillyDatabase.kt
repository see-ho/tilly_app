package com.seeho.tilly.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seeho.tilly.core.database.converter.TagListConverter
import com.seeho.tilly.core.database.dao.RetrospectiveDao
import com.seeho.tilly.core.database.dao.TilDao
import com.seeho.tilly.core.database.entity.RetrospectiveEntity
import com.seeho.tilly.core.database.entity.TilEntity

/**
 * Tilly 앱의 Room Database
 * 버전 1: TIL 테이블만
 * 버전 2: 월간 회고 테이블 추가
 */
@Database(
    entities = [TilEntity::class, RetrospectiveEntity::class],
    version = 2,
    exportSchema = true,
)
@TypeConverters(TagListConverter::class)
abstract class TillyDatabase : RoomDatabase() {

    abstract fun tilDao(): TilDao
    abstract fun retrospectiveDao(): RetrospectiveDao
}
