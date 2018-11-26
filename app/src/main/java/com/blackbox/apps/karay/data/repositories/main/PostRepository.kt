package com.blackbox.apps.karay.data.repositories.main

import android.net.Uri
import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import com.blackbox.apps.karay.data.repositories.network.FireBaseHelper
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.blackbox.apps.karay.models.clothing.WomenClothing
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(private var db: RealmHelper, private var fb: FireBaseHelper) {

    fun getListOfWomenLocalBrands(): List<WomenLocalBrand> {
        return db.findAll(WomenLocalBrand::class.java)
    }

    fun addNewWomenClothing(womenClothing: WomenClothing) {

        db.add(womenClothing)
        fb.saveWomenClothingRefInFireBaseDB(womenClothing)

        uploadImageToFireBase(womenClothing.image)
    }

    fun getBrandLogoURLByName(brandName: String): String? {
        val realm = db.getRealmInstance()
        return realm.where(WomenLocalBrand::class.java).contains("brand", brandName).findFirst()?.logo_url
    }

    private fun uploadImageToFireBase(imagePath: String) {
        val userId = fb.getCurrentUser()?.uid!!
        val uri = Uri.fromFile(File(imagePath))
        val reference = FireBaseHelper.storageRef.child(fb.DIRECTORY_WOMEN_CLOTHING_IMAGES).child(userId).child(uri.lastPathSegment)
        fb.savePhotoInFireBaseStorage(uri, reference)
    }
}