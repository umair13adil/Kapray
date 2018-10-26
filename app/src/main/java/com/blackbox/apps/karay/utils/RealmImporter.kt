package com.blackbox.apps.karay.utils

import android.content.Context
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
                val inputStream = am.open("sample.json")
                //realm1.createAllFromJson(Job::class.java, inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                realm1.close()
            }
        }
    }
}