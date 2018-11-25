package com.blackbox.apps.karay.ui.fragments.add

import androidx.lifecycle.ViewModel
import com.blackbox.apps.karay.data.repositories.main.PostRepository
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.blackbox.apps.karay.models.clothing.WomenClothing
import javax.inject.Inject

class AddNewViewModel @Inject constructor(private var postRepository: PostRepository) : ViewModel() {

    fun getListOfWomenLocalBrands(): List<WomenLocalBrand> {
        return postRepository.getListOfWomenLocalBrands()
    }

    fun saveWomenClothingInfo(womenClothing: WomenClothing) {
        return postRepository.addNewWomenClothing(womenClothing)
    }

    fun getLogoURL(name: String): String? {
        return postRepository.getBrandLogoURLByName(name)
    }
}