package com.blackbox.apps.karay

import android.app.Activity
import android.app.Application
import com.blackbox.apps.karay.di.DaggerAppComponent
import com.blackbox.apps.karay.utils.commons.Preferences
import com.crashlytics.android.Crashlytics
import com.google.firebase.FirebaseApp
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject





/**
 * Created by umair on 17/07/2017.
 */

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        Preferences.init(this)

        Fabric.with(this, Crashlytics())

        Realm.init(this)
        Realm.setDefaultConfiguration(realmConfig)

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    val realmConfig: RealmConfiguration
        get() = RealmConfiguration.Builder()
                .name("Kapray.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build()
}
