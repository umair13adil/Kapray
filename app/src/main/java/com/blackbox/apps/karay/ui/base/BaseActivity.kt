package com.blackbox.apps.karay.ui.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.blackbox.apps.karay.utils.commons.RealmImporter
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

    fun showLoading(message: String = "") {

        if (message.isNotEmpty()) {
            txt_info_message?.visibility = View.VISIBLE
            txt_info_message?.text = message
        }

        progressDialog?.visibility = View.VISIBLE
        progressDialog?.isIndeterminate = true
    }

    fun hideLoading() {

        //Hide Progress Layout
        progressDialog?.animate()
                ?.translationY(progressDialog.height.toFloat())
                ?.alpha(0.0f)
                ?.setDuration(resources.getInteger(R.integer.anim_duration_long).toLong())
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        progressDialog.visibility = View.GONE
                        txt_info_message.visibility = View.GONE
                    }
                })
    }

    fun addLocalBrandsData() {
        val realm = Realm.getDefaultInstance()
        val list = realm.where(WomenLocalBrand::class.java).findAll()
        list?.let {
            if (it.isEmpty()) {
                RealmImporter.importFromJson(this)
            }
        }
    }

    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}