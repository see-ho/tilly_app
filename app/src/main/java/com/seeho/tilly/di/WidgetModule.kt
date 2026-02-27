package com.seeho.tilly.di

import com.seeho.tilly.core.common.widget.WidgetUpdater
import com.seeho.tilly.widget.GlanceWidgetUpdater
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 위젯 관련 DI 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class WidgetModule {

    @Binds
    @Singleton
    abstract fun bindWidgetUpdater(
        impl: GlanceWidgetUpdater,
    ): WidgetUpdater
}
