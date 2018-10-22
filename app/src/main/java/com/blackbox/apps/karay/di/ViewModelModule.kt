package com.blackbox.apps.karay.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blackbox.apps.karay.ui.activities.login.LoginViewModel
import com.blackbox.apps.karay.ui.fragments.detail.DetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * All ViewModel classes that uses Dagger2 injection, must be declared here to support constructor injection,
 * otherwise app will give following exception on runtime access:
 * IllegalArgumentException: "unknown model call class"
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailViewModel(viewModel: DetailViewModel): ViewModel


    @Binds
    abstract fun provideViewModelFactory(factory: MyViewModelFactory): ViewModelProvider.Factory
}