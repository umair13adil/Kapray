package com.blackbox.apps.karay.data.repositories.main

import android.content.Context
import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private var context: Context, private var db: RealmHelper) : MainDataSource {


}