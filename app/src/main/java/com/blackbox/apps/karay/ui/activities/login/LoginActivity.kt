package com.blackbox.apps.karay.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.activities.MainActivity
import com.blackbox.apps.karay.ui.base.BaseActivity
import com.blackbox.apps.karay.utils.Constants
import com.blackbox.apps.karay.utils.FacebookSign
import com.blackbox.apps.karay.utils.GoogleSign
import com.blackbox.apps.karay.utils.Preferences
import com.facebook.FacebookException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), GoogleSign.OnInfoLoginGoogleCallback, FacebookSign.InfoLoginFaceCallback {

    val TAG: String = "LoginActivity"

    private var mGoogleSign: GoogleSign? = null
    private var mFacebookSign: FacebookSign? = null

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

        mGoogleSign!!.onActivityResult(requestCode, resultCode, data!!) // Google Login
        mFacebookSign!!.onActivityResult(requestCode, resultCode, data!!)// Facebook Login
    }

    override fun getInfoLoginGoogle(account: GoogleSignInAccount) {
        signIn()
    }

    override fun connectionFailedApiClient(connectionResult: ConnectionResult) {
        toast("Error PlayServices cod =" + connectionResult)
    }

    override fun loginFailed() {
        toast("Login Failed, try again.")
    }

    override fun getInfoFace() {
        Toast.makeText(this, "Logged in with facebook..please wait...", Toast.LENGTH_SHORT).show()
        signIn()
    }

    override fun cancelLoginFace() {
        toast("Login Failed, try again.")
    }

    override fun errorLoginFace(e: FacebookException) {
        toast("Error Facebook Exception message =" + e.message)
    }

    private fun initViews() {
        mFacebookSign = FacebookSign(this, this)
        mGoogleSign = GoogleSign(this, this)
    }

    private fun signInGoogle() {
        mGoogleSign!!.signIn()
    }

    private fun signInFacebook() {
        mFacebookSign!!.signIn()
    }

    private fun toast(mensage: String) {
        Toast.makeText(this, mensage, Toast.LENGTH_LONG).show()
    }

    private fun signIn() {

        viewModel.saveUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {

                            if (it == Constants.QUERY_SUCCESS) {
                                Preferences.getInstance().save(Constants.KEY_LOGIN, true)
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                toast("Login Failed Send User, try again.")
                            }
                        },
                        onError = {
                            it.printStackTrace()
                        },
                        onComplete = {}
                )
    }
}
