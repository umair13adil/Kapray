package com.blackbox.apps.karay.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * Created by Alessandro on 05/03/2017.
 */
class FacebookSign(private val mActivity: FragmentActivity, private val mFaceCallback: InfoLoginFaceCallback?) {
    private val mCallbackManager: CallbackManager

    init {
        mCallbackManager = CallbackManager.Factory.create()
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun signIn() {
        val permissionNeeds = Arrays.asList("email", "public_profile")
        LoginManager.getInstance().logInWithReadPermissions(
                mActivity,
                permissionNeeds)

        LoginManager.getInstance().registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResults: LoginResult) {
                        handleFacebookAccessToken(loginResults.accessToken)
                    }

                    override fun onCancel() {
                        mFaceCallback?.cancelLoginFace()
                    }

                    override fun onError(e: FacebookException) {
                        mFaceCallback?.errorLoginFace(e)
                    }
                })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val auth = FirebaseAuth.getInstance()
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity) { task ->
                    if (!task.isSuccessful) {
                        mFaceCallback!!.cancelLoginFace()
                    } else {
                        mFaceCallback!!.getInfoFace()
                    }
                }
    }

    interface InfoLoginFaceCallback {
        fun getInfoFace()
        fun cancelLoginFace()
        fun errorLoginFace(e: FacebookException)
    }

    companion object {

    }

}
