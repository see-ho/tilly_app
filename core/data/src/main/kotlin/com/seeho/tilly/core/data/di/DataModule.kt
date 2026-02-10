package com.seeho.tilly.core.data.di

import com.seeho.tilly.core.data.repository.TilRepositoryImpl
import com.seeho.tilly.core.domain.repository.TilRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt DI 모듈: Repository 인터페이스 ↔ 구현체 바인딩
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindTilRepository(
        impl: TilRepositoryImpl,
    ): TilRepository
}
