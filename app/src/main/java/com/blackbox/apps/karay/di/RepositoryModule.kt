package com.blackbox.apps.karay.di

import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import com.blackbox.apps.karay.data.repositories.main.MainRepository
import com.blackbox.apps.karay.data.repositories.main.PostRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainDataSource(db: RealmHelper): MainRepository {
        return MainRepository(db)
    }

    @Provides
    @Singleton
    fun providePostDataSource(db: RealmHelper): PostRepository {
        return PostRepository(db)
    }
}