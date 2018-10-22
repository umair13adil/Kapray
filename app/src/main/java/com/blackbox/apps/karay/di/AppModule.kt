package com.blackbox.apps.karay.di

import android.app.Application
import com.blackbox.apps.karay.App
import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Module(includes = [AndroidInjectionModule::class])
class AppModule {

    @Provides
    @Singleton
    fun app(): Application {
        return App()
    }

    @Provides
    @Singleton
    fun provideRealm(): RealmHelper {
        return RealmHelper()
    }
}