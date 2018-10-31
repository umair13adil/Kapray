package com.blackbox.apps.karay.data.repositories.main

import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private var db: RealmHelper) : MainDataSource {

    fun getListOfWomenLocalBrands(): List<WomenLocalBrand> {
        return db.findAll(WomenLocalBrand::class.java)
    }

}