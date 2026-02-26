package com.seeho.tilly.di

import com.seeho.tilly.core.common.notification.NotificationScheduler
import com.seeho.tilly.worker.NotificationSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindNotificationScheduler(
        impl: NotificationSchedulerImpl,
    ): NotificationScheduler
}
