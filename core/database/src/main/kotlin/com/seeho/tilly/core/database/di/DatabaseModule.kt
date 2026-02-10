package com.seeho.tilly.core.database.di

import android.content.Context
import androidx.room.Room
import com.seeho.tilly.core.database.TillyDatabase
import com.seeho.tilly.core.database.dao.TilDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt DI 모듈: TillyDatabase와 TilDao를 싱글톤으로 제공
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTillyDatabase(
        @ApplicationContext context: Context,
    ): TillyDatabase {
        return Room.databaseBuilder(
            context,
            TillyDatabase::class.java,
            "tilly_database",
        ).build()
    }

    @Provides
    fun provideTilDao(database: TillyDatabase): TilDao {
        return database.tilDao()
    }
}
