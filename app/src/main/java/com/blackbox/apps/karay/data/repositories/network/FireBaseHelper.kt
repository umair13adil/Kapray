package com.blackbox.apps.karay.data.repositories.network

import android.net.Uri
import android.util.Log
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.models.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.reactivex.Observable
import java.io.File


object FireBaseHelper {

    private val TAG = "FireBaseHelper"

    //Firebase DB Tables
    private const val USER_ROOT = "user"
    private const val WOMEN_CLOTHING_ROOT = "women_clothing"

    //FireBase Storage Directories
    const val DIRECTORY_WOMEN_CLOTHING_IMAGES = "WOMEN_CLOTHING_IMAGES/"

    val storageRef by lazy { FirebaseStorage.getInstance().reference }
    val dbRef by lazy { FirebaseDatabase.getInstance().reference }

    /**
     * Save user's reference in FireBase DB
     */
    fun saveUserRefInFireBaseDB(): Observable<User> {

        return Observable.create { emitter ->

            val referenceUser = dbRef.child(USER_ROOT)
            val fireBaseUser = getCurrentUser()

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

    /**
     * Save Women's Clothing info in FireBase DB
     */
    fun saveWomenClothingRefInFireBaseDB(womenClothing: WomenClothing): Observable<WomenClothing> {

        return Observable.create { emitter ->

            val refWomenClothing = dbRef.child(WOMEN_CLOTHING_ROOT)
            val fireBaseUser = getCurrentUser()

            if (fireBaseUser != null) {
                val userId = fireBaseUser.uid
                womenClothing.userId = userId

                val postId = refWomenClothing.push().key!!
                womenClothing.id = postId

                //Save Image to FireBase Storage
                uploadImageToFireBase(womenClothing.image, postId)

                refWomenClothing.child(postId).setValue(womenClothing).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i(TAG, "Women clothing info updated in DB.")

                        if (!emitter.isDisposed) {
                            emitter.onNext(womenClothing)
                            emitter.onComplete()
                        }

                    } else {
                        Log.e(TAG, "Unable to save info to FireBase DB.")

                        if (!emitter.isDisposed) {
                            emitter.onError(Throwable("Unable to save info to FireBase DB."))
                            emitter.onComplete()
                        }
                    }
                }
            }
        }
    }

    private fun uploadImageToFireBase(imagePath: String, postId: String) {
        val userId = getCurrentUser()?.uid!!
        val uri = Uri.fromFile(File(imagePath))

        //Compose Reference
        val reference = storageRef
                .child(DIRECTORY_WOMEN_CLOTHING_IMAGES)
                .child(userId)
                .child(postId)
                .child(uri.lastPathSegment)

        savePhotoInFireBaseStorage(uri, reference, postId)
    }

    private fun updateImageURLInDB(imageDownloadPath: String, postId: String) {

        dbRef.child(WOMEN_CLOTHING_ROOT)
                .child(postId)
                .child("image_url")
                .setValue(imageDownloadPath)

        Log.i(TAG, "Updating image URL in firebase DB.")
    }

    private fun savePhotoInFireBaseStorage(uri: Uri, reference: StorageReference, postId: String) {

        val uploadTask = reference.putFile(uri)

        uploadTask.continueWithTask { task ->

            if (!task.isSuccessful) {
                throw task.exception!!
            }

            return@continueWithTask reference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                downloadUri?.let {
                    updateImageURLInDB(it.toString(), postId)
                }
            } else {
                Log.e(TAG, "Unable to upload image to firebase storage.")
            }
        }
    }

    /**
     * This will return current signed in 'FireBase User
     */
    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }
}