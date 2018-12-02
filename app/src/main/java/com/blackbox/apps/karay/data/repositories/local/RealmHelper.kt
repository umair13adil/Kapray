package com.blackbox.apps.karay.data.repositories.local

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults


class RealmHelper {

    fun getRealmInstance(): Realm {
        try {
            return Realm.getDefaultInstance()
        } catch (e: Exception) {
            return Realm.getDefaultInstance()
        }
    }

    fun <T : RealmObject> add(model: T): T {
        val realm = getRealmInstance()
        realm.executeTransaction {
            it.copyToRealm(model)
        }
        return model
    }

    fun <T : RealmObject> update(model: T): T {
        val realm = getRealmInstance()
        realm.executeTransaction {
            it.copyToRealmOrUpdate(model)
        }
        return model
    }

    fun <T : RealmObject> remove(clazz: Class<T>) {
        val realm = getRealmInstance()
        realm.executeTransaction {
            it.delete(clazz)
        }
    }

    fun <T : RealmObject> findAll(clazz: Class<T>): List<T> {
        return getRealmInstance().where(clazz).findAll()
    }

    fun <T : RealmObject> copyFromRealm(clazz: List<T>): List<T> {
        return getRealmInstance().copyFromRealm(clazz)
    }

    fun <T : RealmObject> copyFromRealm(model: T): T {
        val realm = getRealmInstance()
        return realm.copyFromRealm(model)
    }

}