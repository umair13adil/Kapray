package com.blackbox.apps.karay.models.brands

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WomenLocalBrand(@PrimaryKey var id: Int = 0, var brand: String = "", var logo_url: String = "", var logo_url_local: String = "") : RealmObject() {

}