package com.blackbox.apps.karay.ui.fragments.add

import androidx.lifecycle.ViewModel
import com.blackbox.apps.karay.data.repositories.main.MainRepository
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import javax.inject.Inject

class AddNewViewModel @Inject constructor(private var mainRepository: MainRepository) : ViewModel() {

    fun getListOfWomenLocalBrands():List<WomenLocalBrand>{
        return mainRepository.getListOfWomenLocalBrands()
    }
}