package com.seeho.tilly.core.data.di

import com.seeho.tilly.core.data.repository.AiAnalysisRepositoryImpl
import com.seeho.tilly.core.data.repository.CoinRepositoryImpl
import com.seeho.tilly.core.data.repository.ShopRepositoryImpl
import com.seeho.tilly.core.data.repository.TilRepositoryImpl
import com.seeho.tilly.core.domain.repository.AiAnalysisRepository
import com.seeho.tilly.core.domain.repository.CoinRepository
import com.seeho.tilly.core.domain.repository.ShopRepository
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

    @Binds
    @Singleton
    abstract fun bindAiAnalysisRepository(
        aiAnalysisRepositoryImpl: AiAnalysisRepositoryImpl,
    ): AiAnalysisRepository

    @Binds
    @Singleton
    abstract fun bindCoinRepository(
        impl: CoinRepositoryImpl,
    ): CoinRepository

    @Binds
    @Singleton
    abstract fun bindShopRepository(
        impl: ShopRepositoryImpl,
    ): ShopRepository
}
