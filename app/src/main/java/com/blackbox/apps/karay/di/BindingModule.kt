package com.blackbox.apps.karay.di


import com.blackbox.apps.karay.ui.activities.MainActivity
import com.blackbox.apps.karay.ui.activities.login.LoginActivity
import com.blackbox.apps.karay.ui.base.BaseActivity
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.ui.fragments.detail.DetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * All components (Activity, Fragment, Services) that have been injected, must be declared here,
 * otherwise app will give exception during run-time.
 *
 * App can give following exceptions during run-time:
 * 1. UninitializedPropertyAccessException: lateinit property has not been initialized
 * 2. IllegalArgumentException: No injector factory bound
 */
@Module
internal abstract class BindingModule {

    /****************************
     * Activities
     * **************************/

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun baseActivity(): BaseActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun loginActivity(): LoginActivity

    /****************************
     ** Fragments
     ****************************/

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun baseFragment(): BaseFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun detailFragment(): DetailFragment


    /****************************
     ** Dialogs
     ****************************/



    /****************************
     ** Services
     ****************************/
}