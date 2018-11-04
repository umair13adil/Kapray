package com.blackbox.apps.karay.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blackbox.apps.karay.ui.activities.login.LoginViewModel
import com.blackbox.apps.karay.ui.fragments.add.AddNewViewModel
import com.blackbox.apps.karay.ui.fragments.detail.DetailViewModel
import com.blackbox.apps.karay.ui.fragments.main.MainViewModel
import com.blackbox.apps.karay.ui.fragments.pages.PagesViewModel
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
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PagesViewModel::class)
    abstract fun bindPagesViewModel(viewModel: PagesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddNewViewModel::class)
    abstract fun bindAddNewViewModel(viewModel: AddNewViewModel): ViewModel


    @Binds
    abstract fun provideViewModelFactory(factory: MyViewModelFactory): ViewModelProvider.Factory
}