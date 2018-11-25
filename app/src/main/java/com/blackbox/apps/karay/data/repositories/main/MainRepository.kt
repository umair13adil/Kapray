package com.blackbox.apps.karay.data.repositories.main

import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.blackbox.apps.karay.models.clothing.WomenClothing
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private var db: RealmHelper) {

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
        return db.copyFromRealm(list)
    }
}