package com.blackbox.apps.karay.ui.fragments.main

import android.app.Application
import androidx.lifecycle.ViewModel
import com.blackbox.apps.karay.data.repositories.main.MainRepository
import com.blackbox.apps.karay.models.clothing.WomenClothing
import javax.inject.Inject

class MainViewModel @Inject constructor(private var app: Application, private var mainRepository: MainRepository) : ViewModel() {

    fun getListOfWomenClothing(): List<WomenClothing> {
        return mainRepository.getListOfWomenClothing()
    }

}