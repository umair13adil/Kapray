package com.blackbox.apps.karay.data.repositories.main

import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import com.blackbox.apps.karay.data.repositories.network.FireBaseHelper
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.blackbox.apps.karay.models.clothing.WomenClothing
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(private var db: RealmHelper, private var fb: FireBaseHelper) {

    fun getListOfWomenLocalBrands(): List<WomenLocalBrand> {
        return db.findAll(WomenLocalBrand::class.java)
    }

    fun addNewWomenClothing(womenClothing: WomenClothing) {

        //Add to DB
        db.add(womenClothing)

        //Update post to DB once post is uploaded to FireBase
        fb.saveWomenClothingRefInFireBaseDB(womenClothing)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            db.update(it)
                        },
                        onError = {
                            it.printStackTrace()
                        }
                )
    }

    fun updateWomenClothing(womenClothing: WomenClothing) {

        //Add to DB
        db.update(womenClothing)

        //Update post to DB once post is uploaded to FireBase
        fb.updateWomenClothingRefInFireBaseDB(womenClothing)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            db.update(it)
                        },
                        onError = {
                            it.printStackTrace()
                        }
                )
    }

    fun getBrandLogoURLByName(brandName: String): String? {
        val realm = db.getRealmInstance()
        return realm.where(WomenLocalBrand::class.java).contains("brand", brandName).findFirst()?.logo_url
    }
}