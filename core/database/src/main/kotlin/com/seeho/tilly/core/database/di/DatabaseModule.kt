package com.seeho.tilly.core.database.di

import android.content.Context
import androidx.room.Room
import com.seeho.tilly.core.database.TillyDatabase
import com.seeho.tilly.core.database.dao.RetrospectiveDao
import com.seeho.tilly.core.database.dao.TilDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

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
        ).fallbackToDestructiveMigration()  //  TODO 출시 후 삭제,  Migration으로 교체
            .setQueryExecutor(Dispatchers.IO.asExecutor())
            .setTransactionExecutor(Dispatchers.IO.asExecutor())
            .build()
    }

    @Provides
    fun provideTilDao(database: TillyDatabase): TilDao {
        return database.tilDao()
    }

    @Provides
    fun provideRetrospectiveDao(database: TillyDatabase): RetrospectiveDao {
        return database.retrospectiveDao()
    }
}
