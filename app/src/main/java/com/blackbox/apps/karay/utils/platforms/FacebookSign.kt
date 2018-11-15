package com.blackbox.apps.karay.utils.platforms

import android.app.Activity
import android.content.Intent
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import java.util.*


class FacebookSign(private val mActivity: Activity) {

    private val mCallbackManager: CallbackManager = CallbackManager.Factory.create()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun signIn(): Observable<LoginRequestResult> {

        return Observable.create { emitter ->

            val permissionNeeds = Arrays.asList("email")

            LoginManager.getInstance().logInWithReadPermissions(mActivity, permissionNeeds)

            LoginManager.getInstance().registerCallback(mCallbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(loginResults: LoginResult) {

                            val auth = FirebaseAuth.getInstance()
                            val credential = FacebookAuthProvider.getCredential(loginResults.accessToken.token)

                            auth.signInWithCredential(credential)
                                    .addOnCompleteListener(mActivity) { task ->
                                        if (task.isSuccessful) {
                                            emitter.onNext(LoginRequestResult.LOGIN_SUCCESS)
                                        } else {
                                            emitter.onNext(LoginRequestResult.LOGIN_FAILED)
                                        }
                                    }
                        }

                        override fun onCancel() {
                            emitter.onNext(LoginRequestResult.LOGIN_FAILED)
                        }

                        override fun onError(e: FacebookException) {
                            emitter.onError(e)
                        }
                    })
        }
    }
}
