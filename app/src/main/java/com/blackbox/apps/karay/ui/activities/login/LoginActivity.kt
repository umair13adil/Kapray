package com.blackbox.apps.karay.ui.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.activities.MainActivity
import com.blackbox.apps.karay.ui.base.BaseActivity
import com.blackbox.apps.karay.utils.commons.Constants
import com.blackbox.apps.karay.utils.commons.Preferences
import com.blackbox.apps.karay.utils.platforms.FacebookSign
import com.blackbox.apps.karay.utils.platforms.GoogleSign
import com.blackbox.apps.karay.utils.platforms.LoginRequestResult
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

        //generateKeyHash(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        //check preferences login
        if (Preferences.getInstance().getBoolean(Constants.KEY_LOGIN, false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        initViews()

        btn_facebook.setOnClickListener {
            signInFacebook()
        }

        btn_google.setOnClickListener {
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
                            if (it == LoginRequestResult.LOGIN_SUCCESS) {
                                signIn()
                            } else if (it == LoginRequestResult.LOGIN_FAILED) {

                            }
                        },
                        onError = {
                            it.printStackTrace()
                        }
                )
    }

    override fun onLoginSuccess() {
        signIn()
    }

    override fun onLoginFailed() {

    }

    private fun signIn() {

        viewModel.saveUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            Preferences.getInstance().save(Constants.KEY_LOGIN, true)
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        },
                        onError = {
                            toast("Login Failed Send User, try again.")
                        },
                        onComplete = {}
                )
    }
}
