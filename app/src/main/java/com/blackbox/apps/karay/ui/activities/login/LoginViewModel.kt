package com.blackbox.apps.karay.ui.activities.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.blackbox.apps.karay.models.user.User
import com.blackbox.apps.karay.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import javax.inject.Inject

class LoginViewModel @Inject constructor(private var app: Application) : ViewModel() {


    fun saveUser(): Observable<String> {
        return Observable.create { subscriber ->
            val referenceUser = FirebaseDatabase.getInstance().reference.child(Constants.USER_ROOT)
            val fireBaseUser = FirebaseAuth.getInstance().currentUser

            if (fireBaseUser != null) {
                val user = User()
                user.name = fireBaseUser.displayName
                user.email = fireBaseUser.email
                user.isAdmin = false

                user.photoUrl = if (fireBaseUser.photoUrl == null) "default_uri" else fireBaseUser.photoUrl!!.toString()
                user.uId = fireBaseUser.uid

                referenceUser.child(fireBaseUser.uid).setValue(user).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (!subscriber.isDisposed) {
                            subscriber.onNext(Constants.QUERY_SUCCESS)
                        } else {
                            subscriber.onError(Throwable("Error Signing In.."))
                        }
                    }
                }
            }
        }
    }
}