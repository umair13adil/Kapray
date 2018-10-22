package com.blackbox.apps.karay.utils

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSign(private val context: FragmentActivity, private val mGoogleCallback: OnInfoLoginGoogleCallback) {

    private var mGoogleApiClient: GoogleApiClient? = null

    init {
        getConfigDefaultLogin()
    }

    private fun getConfigDefaultLogin() {
        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(context.getString(R.string.goog))
                .requestEmail()
                .build()
        // [END config_signin]

        mGoogleApiClient = GoogleApiClient.Builder(context)
                .enableAutoManage(context /* FragmentActivity */) { connectionResult -> mGoogleCallback.connectionFailedApiClient(connectionResult) }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

    }

    // [START onactivityresult]
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                mGoogleCallback.loginFailed()
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START signin]
    fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        context.startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]


    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(context) { task ->
                    if (!task.isSuccessful) {
                        mGoogleCallback.loginFailed()
                    } else {
                        mGoogleCallback.getInfoLoginGoogle(acct)
                    }
                }
    }
    // [END auth_with_google]

    interface OnInfoLoginGoogleCallback {
        fun getInfoLoginGoogle(account: GoogleSignInAccount)
        fun connectionFailedApiClient(connectionResult: ConnectionResult)
        fun loginFailed()
    }

    companion object {

        private val RC_SIGN_IN = 10
    }


}
