package com.blackbox.apps.karay.models.user

import androidx.annotation.Keep


/**
 * Created by Alessandro on 04/03/2017.
 */

@Keep
class User {

    var name: String? = null
    var email: String? = null
    var uId: String? = null
    var photoUrl: String? = null
    var phone: String? = null
    var address: String? = null
    var whatsapp: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var isAdmin: Boolean? = null
}
