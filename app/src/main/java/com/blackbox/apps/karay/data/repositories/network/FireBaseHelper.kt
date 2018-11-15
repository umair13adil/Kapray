package com.blackbox.apps.karay.data.repositories.network

import com.blackbox.apps.karay.models.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable

object FireBaseHelper {

    //User Table
    private val USER_ROOT = "user"

    /**
     * Save user's reference in FireBase DB
     */
    fun saveUserRefInFireBaseDB(): Observable<User> {

        return Observable.create { emitter ->

            val referenceUser = FirebaseDatabase.getInstance().reference.child(USER_ROOT)
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
                        if (!emitter.isDisposed) {
                            emitter.onNext(user)
                            emitter.onComplete()
                        }
                    } else {
                        if (!emitter.isDisposed) {
                            emitter.onError(Throwable("Login Failed Send User, try again."))
                            emitter.onComplete()
                        }
                    }
                }
            }
        }
    }
}