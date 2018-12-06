package com.blackbox.apps.karay.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.ViewModelProviders
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.activities.MainActivity
import com.blackbox.apps.karay.ui.base.BaseActivity
import com.blackbox.apps.karay.utils.commons.Constants
import com.blackbox.apps.karay.utils.commons.Preferences
import com.blackbox.apps.karay.utils.generateKeyHash
import com.blackbox.apps.karay.utils.platforms.FacebookSign
import com.blackbox.apps.karay.utils.platforms.GoogleSign
import com.blackbox.apps.karay.utils.platforms.LoginRequestResult
import com.blackbox.apps.karay.utils.showToast
import com.bumptech.glide.Glide
import com.transitionseverywhere.Fade
import com.transitionseverywhere.Slide
import com.transitionseverywhere.TransitionManager
import com.transitionseverywhere.TransitionSet
import com.transitionseverywhere.extra.Scale
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity(), GoogleSign.GoogleSignInCallBack {

    val TAG: String = "LoginActivity"

    private lateinit var mGoogleSign: GoogleSign
    private lateinit var mFacebookSign: FacebookSign

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        generateKeyHash(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        //check preferences login
        if (Preferences.getInstance().getBoolean(Constants.KEY_LOGIN, false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        initViews()

        btn_facebook.setOnClickListener {
            showLoading(getString(R.string.txt_logging_in))
            signInFacebook()
        }

        btn_google.setOnClickListener {
            showLoading(getString(R.string.txt_logging_in))
            signInGoogle()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mGoogleSign.onActivityResult(requestCode, resultCode, data!!) // Google Login
        mFacebookSign.onActivityResult(requestCode, resultCode, data)// Facebook Login
    }

    private fun initViews() {
        mFacebookSign = FacebookSign(this)
        mGoogleSign = GoogleSign(this, this)

        setupViews()
        hideLoading()
    }

    private fun signInGoogle() {
        mGoogleSign.signIn()
    }

    private fun signInFacebook() {
        mFacebookSign.signIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            hideLoading()

                            if (it == LoginRequestResult.LOGIN_SUCCESS) {
                                signIn()
                            } else if (it == LoginRequestResult.LOGIN_FAILED) {
                                showToast(getString(R.string.txt_logging_in_failed), this)
                            }
                        },
                        onError = {
                            it.printStackTrace()
                        }
                )
    }

    override fun onLoginSuccess() {
        hideLoading()
        signIn()
    }

    override fun onLoginFailed() {
        showToast(getString(R.string.txt_logging_in_failed), this)
    }

    private fun signIn() {

        viewModel.saveUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            hideLoading()

                            Preferences.getInstance().save(Constants.KEY_LOGIN, true)
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        },
                        onError = {
                            showToast(getString(R.string.txt_logging_in_failed), this)
                        },
                        onComplete = {}
                )
    }

    private fun setupViews() {

        Glide.with(this)
                .load(R.drawable.img_splash)
                .into(img_splash_bg)

        val set = TransitionSet()
                .addTransition(Scale(1f))
                .addTransition(Fade())
                .addTransition(Slide(Gravity.END))
                .setInterpolator(LinearOutSlowInInterpolator())

        TransitionManager.beginDelayedTransition(container_login, set)
        app_logo.visibility = View.VISIBLE

        Glide.with(this)
                .load(R.drawable.ic_logo_empty)
                .into(app_logo)
    }
}
