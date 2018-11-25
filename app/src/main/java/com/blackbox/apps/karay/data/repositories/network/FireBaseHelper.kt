package com.blackbox.apps.karay.data.repositories.network

import android.net.Uri
import com.blackbox.apps.karay.models.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.reactivex.Observable


object FireBaseHelper {

    //User Table
    private const val USER_ROOT = "user"

    val storageRef by lazy { FirebaseStorage.getInstance().reference }

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

    fun updateImageURLInDB(imageDownloadPath: String) {
        val user = getCurrentUser()
        FirebaseDatabase.getInstance().reference.child(user?.uid!!)
                .child("image_url")
                .setValue(imageDownloadPath)
    }

    fun savePhotoInFireBaseStorage(uri: Uri, reference: StorageReference) {

        val uploadTask = reference.putFile(uri)

        uploadTask.continueWithTask { task ->

            if (!task.isSuccessful) {
                throw task.exception!!
            }

            return@continueWithTask reference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

            } else {

            }
        }


    }

    /**
     * This will return current signed in 'FireBase User
     */
    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }
}