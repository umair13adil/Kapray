package com.blackbox.apps.karay.data.repositories.post

import com.blackbox.apps.karay.data.repositories.local.RealmHelper
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PostRepository @Inject constructor(private var db: RealmHelper) : PostDataSource {


}