package com.seeho.tilly.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seeho.tilly.core.database.converter.TagListConverter
import com.seeho.tilly.core.database.dao.TilDao
import com.seeho.tilly.core.database.entity.TilEntity

/**
 * Tilly 앱의 Room Database
 * 버전 1: TIL 테이블만
 */
@Database(
    entities = [TilEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(TagListConverter::class)
abstract class TillyDatabase : RoomDatabase() {

    abstract fun tilDao(): TilDao
}
