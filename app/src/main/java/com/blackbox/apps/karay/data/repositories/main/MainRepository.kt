package com.blackbox.apps.karay.data.repositories.main

import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import com.blackbox.apps.karay.data.repositories.network.FireBaseHelper
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.models.rxbus.AppEvents
import com.blackbox.apps.karay.models.rxbus.EventData
import com.blackbox.plog.pLogs.PLog
import com.blackbox.plog.pLogs.models.LogLevel
import com.michaelflisar.rxbus2.RxBus
import io.reactivex.Observable
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

    fun getListOfWomenClothing(inCloset: Boolean): Observable<List<WomenClothing>> {

        return Observable.create { emitter ->

            //Find all items, only if they are not deleted
            val list = db.getRealmInstance().where(WomenClothing::class.java).isEmpty("deletedOn").findAll()

            list.addChangeListener { t, changeSet ->

                val copiedList = db.copyFromRealm(t)

                if (!emitter.isDisposed) {
                    emitter.onNext(copiedList)
                    emitter.onComplete()
                }
            }

            val copiedList = db.copyFromRealm(list).filter {
                it.kept_away == !inCloset
            }

            if (!emitter.isDisposed) {
                emitter.onNext(copiedList)
                emitter.onComplete()
            }
        }
    }

    fun getListOfWomenClothing(): Observable<List<WomenClothing>> {

        return Observable.create { emitter ->

            //Find all items, only if they are not deleted
            val list = db.getRealmInstance().where(WomenClothing::class.java).isEmpty("deletedOn").findAll()

            list.addChangeListener { t, changeSet ->

                val copiedList = db.copyFromRealm(t)

                if (!emitter.isDisposed) {
                    emitter.onNext(copiedList)
                    emitter.onComplete()
                }
            }

            val copiedList = db.copyFromRealm(list)

            if (!emitter.isDisposed) {
                emitter.onNext(copiedList)
                emitter.onComplete()
            }
        }
    }

    fun syncAll() {
        val list = db.getRealmInstance().where(WomenClothing::class.java).findAll()

        Observable.fromIterable(list)
                .blockingForEach {

                    //If Id is empty then it means that it is not yet updated in Firebase DB
                    if (it.id.isEmpty()) {
                        fb.saveWomenClothingRefInFireBaseDB(it)
                    } else {

                        //Check if item is not already present in firebase DB, then insert it
                        fb.getWomenClothingInfoById(it.id)
                                .subscribeBy(
                                        onNext = {
                                            fb.saveWomenClothingRefInFireBaseDB(it)
                                        },
                                        onError = {
                                            it.printStackTrace()
                                        }
                                )
                    }
                }

        fb.syncWomenClothingData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            it.forEach { item ->
                                db.update(item)
                                PLog.logThis(TAG, "syncAll", item.toString(), LogLevel.INFO)
                            }

                            //Notify listeners that sync has been completed
                            val eventData = EventData(AppEvents.SYNC_COMPLETED, data = arrayOf(it.size.toString()))
                            RxBus.get().withSendToDefaultBus().send(eventData)
                        },
                        onError = {
                            it.printStackTrace()
                        }
                )
    }
}