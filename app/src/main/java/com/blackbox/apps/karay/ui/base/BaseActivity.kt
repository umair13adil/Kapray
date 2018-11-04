package com.blackbox.apps.karay.ui.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.blackbox.apps.karay.utils.RealmImporter
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.realm.Realm
import kotlinx.android.synthetic.main.layout_progress.*
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    fun showLoading(message: String) {
        try {
            card_progress?.visibility = View.VISIBLE
            txt_info_message?.visibility = View.VISIBLE
            txt_info_message?.text = message
            progressDialogHorizontal?.visibility = View.GONE
            progressDialog?.isIndeterminate = true
            progressDialog?.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showProgressLoading(message: String, progress: Int?) {

        try {
            card_progress?.visibility = View.VISIBLE
            txt_info_message?.visibility = View.VISIBLE
            txt_info_message?.text = message
            progressDialog?.visibility = View.GONE
            progressDialogHorizontal?.max = 100
            progressDialogHorizontal?.isIndeterminate = false
            progressDialogHorizontal?.visibility = View.VISIBLE

            if (progress != null) {
                progressDialogHorizontal?.progress = progress
            } else {
                showLoading(message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideLoading() {
        try {
            //Hide Progress Layout
            card_progress?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addLocalBrandsData(){
        val realm = Realm.getDefaultInstance()
        val list = realm.where(WomenLocalBrand::class.java).findAll()
        list?.let {
            if (it.isEmpty()) {
                RealmImporter.importFromJson(this)
            }
        }
    }
}