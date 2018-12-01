package com.blackbox.apps.karay.di

import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import com.blackbox.apps.karay.data.repositories.main.MainRepository
import com.blackbox.apps.karay.data.repositories.main.PostRepository
import com.blackbox.apps.karay.data.repositories.network.FireBaseHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainDataSource(db: RealmHelper, fb: FireBaseHelper): MainRepository {
        return MainRepository(db, fb)
    }

    @Provides
    @Singleton
    fun providePostDataSource(db: RealmHelper, fb: FireBaseHelper): PostRepository {
        return PostRepository(db, fb)
    }
}