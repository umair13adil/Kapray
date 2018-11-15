package com.blackbox.apps.karay.ui.activities.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.blackbox.apps.karay.data.repositories.network.FireBaseHelper
import com.blackbox.apps.karay.models.user.User
import io.reactivex.Observable
import javax.inject.Inject

class LoginViewModel @Inject constructor(private var app: Application) : ViewModel() {

    fun saveUser(): Observable<User> {
        return FireBaseHelper.saveUserRefInFireBaseDB()
    }
}