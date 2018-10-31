package com.blackbox.apps.karay.utils

import android.content.Context
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import io.realm.Realm

/**
 * From json to realm database
 */
object RealmImporter {

    fun importFromJson(context: Context) {
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction { realm1 ->
            val am = context.assets
            try {
                val inputStream = am.open("women_brands.json")
                realm1.createAllFromJson(WomenLocalBrand::class.java, inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}