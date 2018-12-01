package com.blackbox.apps.karay.di


import com.blackbox.apps.karay.ui.activities.MainActivity
import com.blackbox.apps.karay.ui.activities.login.LoginActivity
import com.blackbox.apps.karay.ui.base.BaseActivity
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.ui.fragments.add.AddNewFragment
import com.blackbox.apps.karay.ui.fragments.add.AdditionalInfoFragment
import com.blackbox.apps.karay.ui.fragments.detail.DetailFragment
import com.blackbox.apps.karay.ui.fragments.main.MainFragment
import com.blackbox.apps.karay.ui.fragments.pages.BrandsListFragment
import com.blackbox.apps.karay.ui.fragments.pages.MyWardrobeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

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

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun mainFragment(): MainFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun myWardrobeFragment(): MyWardrobeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun brandsListFragment(): BrandsListFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun addNewFragment(): AddNewFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun additionalInfoFragment(): AdditionalInfoFragment


    /****************************
     ** Dialogs
     ****************************/


    /****************************
     ** Services
     ****************************/
}