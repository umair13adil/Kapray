package com.blackbox.apps.karay.data.repositories.main

import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import com.blackbox.apps.karay.data.repositories.network.FireBaseHelper
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.models.enums.OrderBy
import com.blackbox.apps.karay.models.enums.Sizes
import com.blackbox.apps.karay.models.rxbus.AppEvents
import com.blackbox.apps.karay.models.rxbus.EventData
import com.blackbox.apps.karay.ui.base.FilterClothingData
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

    fun getFilteredListOfWomenClothing(filterClothingData: FilterClothingData): Observable<List<WomenClothing>> {

        return Observable.create { emitter ->

            //Find all items, only if they are not deleted
            val list = db.getRealmInstance().where(WomenClothing::class.java)
                    .isEmpty("deletedOn")
                    .findAll()

            var copiedList = db.copyFromRealm(list)
            PLog.logThis(TAG, "Filter1", "Size: ${copiedList.size}", LogLevel.INFO)

            copiedList = if (filterClothingData.purchasedOrder == OrderBy.LATEST) {
                copiedList.sortedByDescending {
                    it.date_purchased
                }
            } else {
                copiedList.sortedBy {
                    it.date_purchased
                }
            }

            PLog.logThis(TAG, "Filter2", "Size: ${copiedList.size}", LogLevel.INFO)

            copiedList = if (filterClothingData.addedOrder == OrderBy.LATEST) {
                copiedList.sortedByDescending {
                    it.date_added
                }
            } else {
                copiedList.sortedBy {
                    it.date_added
                }
            }

            PLog.logThis(TAG, "Filter3", "Size: ${copiedList.size}", LogLevel.INFO)

            copiedList = copiedList.filter {
                it.season_info == filterClothingData.season.value
            }

            if (copiedList.mapNotNull { it.size_info }.isNotEmpty()) {

                if (filterClothingData.size != Sizes.NONE) {
                    copiedList = copiedList.filter {
                        it.size_info == filterClothingData.size.value
                    }
                }
            }

            PLog.logThis(TAG, "Filter4", "Size: ${copiedList.size}", LogLevel.INFO)

            if (!emitter.isDisposed) {
                emitter.onNext(copiedList)
                emitter.onComplete()
            }
        }
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

    fun deletePost(id: String) {
        val item = db.findById(WomenClothing::class.java, "id", id)

        item?.let {
            db.removeFromRealm(it)
        }

        fb.deleteWomenClothingInfoById(id)
    }
}