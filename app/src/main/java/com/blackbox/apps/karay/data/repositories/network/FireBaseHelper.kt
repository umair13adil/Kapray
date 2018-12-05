package com.blackbox.apps.karay.data.repositories.network

import android.net.Uri
import android.util.Log
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.models.user.User
import com.blackbox.plog.pLogs.PLog
import com.blackbox.plog.pLogs.models.LogLevel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        val file = File(imagePath)
        val uri = Uri.fromFile(file)
        val fileName = uri.lastPathSegment

        //Compose Reference
        val reference = storageRef
                .child(DIRECTORY_WOMEN_CLOTHING_IMAGES)
                .child(userId)
                .child(postId)
                .child(fileName)

        savePhotoInFireBaseStorage(uri, reference, postId)
    }

    private fun updateImageURLInDB(imageDownloadPath: String, postId: String, fileName: String) {

        dbRef.child(WOMEN_CLOTHING_ROOT)
                .child(postId)
                .child("image_url")
                .setValue(imageDownloadPath)

        dbRef.child(WOMEN_CLOTHING_ROOT)
                .child(postId)
                .child("file_name")
                .setValue(fileName)


        Log.i(TAG, "Updating image URL in firebase DB. FileName: $fileName")
    }

    private fun savePhotoInFireBaseStorage(uri: Uri, reference: StorageReference, postId: String) {

        val fileName = uri.lastPathSegment
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
                    updateImageURLInDB(it.toString(), postId, fileName)
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

    /**
     * This will retrieve data from FireBase database and sync with Realm DB.
     */
    fun syncWomenClothingData(): Observable<List<WomenClothing>> {

        return Observable.create { emitter ->

            val list = arrayListOf<WomenClothing>()
            val refWomenClothing = dbRef.child(WOMEN_CLOTHING_ROOT)
            val fireBaseUser = getCurrentUser()

            fireBaseUser?.let {
                refWomenClothing.orderByChild("userId").equalTo(it.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (data in dataSnapshot.children) {

                            data.getValue(WomenClothing::class.java)?.let { dbData ->
                                list.add(dbData)
                            }
                        }

                        if (!emitter.isDisposed) {
                            emitter.onNext(list)
                            emitter.onComplete()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                        if (!emitter.isDisposed) {
                            emitter.onError(databaseError.toException())
                            emitter.onComplete()
                        }
                    }
                })
            }
        }
    }

    /**
     * This will retrieve data from FireBase database and sync with Realm DB.
     */
    fun getWomenClothingInfoById(id: String): Observable<WomenClothing> {

        return Observable.create { emitter ->

            val refWomenClothing = dbRef.child(WOMEN_CLOTHING_ROOT)
            val fireBaseUser = getCurrentUser()

            fireBaseUser?.let {
                refWomenClothing.orderByKey().equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (data in dataSnapshot.children) {

                            data.getValue(WomenClothing::class.java)?.let { dbData ->

                                PLog.logThis(TAG, "getWomenClothingInfoById", "Found: $dbData", LogLevel.INFO)

                                if (!emitter.isDisposed) {
                                    emitter.onNext(dbData)
                                    emitter.onComplete()
                                }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                        if (!emitter.isDisposed) {
                            emitter.onError(databaseError.toException())
                            emitter.onComplete()
                        }
                    }
                })
            }
        }
    }

    /**
     * This will delete data from FireBase database and sync with Realm DB.
     */
    fun deleteWomenClothingInfoById(id: String) {

        val refWomenClothing = dbRef.child(WOMEN_CLOTHING_ROOT)
        val fireBaseUser = getCurrentUser()

        fireBaseUser?.let {
            refWomenClothing.orderByKey().equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {

                        data.getValue(WomenClothing::class.java)?.let { clothingData ->
                            deleteImageFileFromStorage(fireBaseUser.uid, clothingData.id, clothingData.file_name)
                        }

                        data.ref.removeValue()

                        PLog.logThis(TAG, "deleteWomenClothingInfoById", "Deleted!", LogLevel.INFO)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    PLog.logThis(TAG, "deleteWomenClothingInfoById", exception = databaseError.toException())
                }
            })
        }
    }

    fun deleteImageFileFromStorage(userId: String, id: String, fileName: String) {

        val reference = storageRef
                .child(DIRECTORY_WOMEN_CLOTHING_IMAGES)
                .child(userId)
                .child(id)
                .child(fileName)

        PLog.logThis(TAG, "deleteImageFileFromStorage", "Ref: ${reference.path}", LogLevel.INFO)

        reference.delete().addOnSuccessListener {
            PLog.logThis(TAG, "deleteImageFileFromStorage", "Image file deleted!", LogLevel.INFO)
        }
    }
}