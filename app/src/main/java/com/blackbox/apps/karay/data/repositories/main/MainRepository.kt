package com.blackbox.apps.karay.data.repositories.main

import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import com.blackbox.apps.karay.data.repositories.network.FireBaseHelper
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.plog.pLogs.PLog
import com.blackbox.plog.pLogs.models.LogLevel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private var db: RealmHelper, private var fb: FireBaseHelper) {

    private val TAG = "MainRepository"

    fun getListOfWomenLocalBrands(): List<WomenLocalBrand> {
        return db.findAll(WomenLocalBrand::class.java)
    }

    fun getListOfWomenClothing(inCloset: Boolean): List<WomenClothing> {
        val list = db.findAll(WomenClothing::class.java).filter {
            it.kept_away == !inCloset
        }
        return db.copyFromRealm(list)
    }

    fun getListOfWomenClothing(): List<WomenClothing> {
        val list = db.findAll(WomenClothing::class.java)
        return db.copyFromRealm(list).sortedBy {
            it.season_info
        }
    }

    fun syncAll() {
        fb.syncWomenClothingData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            it.forEach { item ->
                                db.update(item)
                                PLog.logThis(TAG,"syncAll", item.toString(), LogLevel.INFO)
                            }
                        },
                        onError = {
                            it.printStackTrace()
                        }
                )
    }
}