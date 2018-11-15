package com.blackbox.apps.karay.utils.platforms

import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSign(private val context: FragmentActivity, val googleSignInCallBack: GoogleSignInCallBack) {

    private val TAG = "GoogleSign"
    private var mGoogleApiClient: GoogleApiClient? = null

    init {
        getConfigDefaultLogin()
    }

    private fun getConfigDefaultLogin() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(context)
                .enableAutoManage(context) {
                    if (!it.isSuccess) {
                        Log.e(TAG, "${it.errorMessage}")
                        googleSignInCallBack.onLoginFailed()
                    }
                }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount

                account?.let {
                    fireBaseAuthWithGoogle(it)
                }
            } else {
                googleSignInCallBack.onLoginFailed()
            }
        }
    }

    fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        context.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun fireBaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(context) { task ->
                    if (!task.isSuccessful) {
                        googleSignInCallBack.onLoginSuccess()
                    } else {
                        googleSignInCallBack.onLoginFailed()
                    }
                }
    }

    companion object {
        private val RC_SIGN_IN = 10
    }

    interface GoogleSignInCallBack {
        fun onLoginSuccess()
        fun onLoginFailed()
    }

}
