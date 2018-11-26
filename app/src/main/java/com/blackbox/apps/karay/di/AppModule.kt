package com.blackbox.apps.karay.di

import android.app.Application
import com.blackbox.apps.karay.App
import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import com.blackbox.apps.karay.data.repositories.network.FireBaseHelper
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

    @Provides
    @Singleton
    fun provideFirebase(): FireBaseHelper {
        return FireBaseHelper
    }
}