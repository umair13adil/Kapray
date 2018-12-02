package com.blackbox.apps.karay.ui.fragments.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import com.blackbox.apps.karay.data.repositories.main.MainRepository
import javax.inject.Inject

class DetailViewModel @Inject constructor(private var app: Application, private var mainRepository: MainRepository) : ViewModel() {

    fun deletePost(id: String) {
        mainRepository.deletePost(id)
    }

}