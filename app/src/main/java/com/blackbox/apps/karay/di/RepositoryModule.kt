package com.blackbox.apps.karay.di

import android.content.Context
import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import com.blackbox.apps.karay.data.repositories.main.MainRepository
import com.blackbox.apps.karay.data.repositories.post.PostRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun providePostRepository(context: Context, db: RealmHelper): PostRepository {
        return PostRepository(context,db)
    }

    @Provides
    @Singleton
    fun provideMainDataSource(context: Context, db: RealmHelper): MainRepository {
        return MainRepository(context, db)
    }
}