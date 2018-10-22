package com.blackbox.apps.karay.di

import com.blackbox.apps.karay.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AppModule::class,
        BindingModule::class,
        ViewModelModule::class,
        RepositoryModule::class
))
interface AppComponent {

    @Component.Builder
    interface Builder {

        fun appModule(appModule: AppModule): Builder

        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}