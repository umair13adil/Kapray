package com.blackbox.apps.karay.ui.fragments.pages

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.karay.data.repositories.main.MainRepository
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.models.enums.AdapterActions
import com.blackbox.apps.karay.ui.items.SeasonsHeaderItem
import com.blackbox.apps.karay.ui.items.WomenClothingItem
import com.blackbox.apps.karay.ui.items.WomenLocalBrandItem
import com.blackbox.apps.karay.utils.helpers.ListHelper
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import javax.inject.Inject

class PagesViewModel @Inject constructor(private var app: Application, private var mainRepository: MainRepository) : ViewModel() {


    private var adapter: FlexibleAdapter<*>? = null

    fun getListOfWomenClothing(inCloset: Boolean): ArrayList<IFlexible<*>> {
        val list = mainRepository.getListOfWomenClothing(inCloset)

        val mItems = arrayListOf<IFlexible<*>>()

        list.forEach {

            val headerItem = SeasonsHeaderItem(it.season_info)
            val item1 = WomenClothingItem(it, headerItem)

            //Add Item to List
            mItems.add(item1)
        }

        return mItems
    }

    fun getListOfWomenClothing(): ArrayList<IFlexible<*>> {
        val list = mainRepository.getListOfWomenClothing()

        val mItems = arrayListOf<IFlexible<*>>()

        list.forEach {

            val headerItem = SeasonsHeaderItem(it.season_info)
            val item1 = WomenClothingItem(it, headerItem)

            //Add Item to List
            mItems.add(item1)
        }

        return mItems
    }

    fun getListOfWomenClothingBrands(): ArrayList<IFlexible<*>> {
        val list = mainRepository.getListOfWomenLocalBrands()

        val mItems = arrayListOf<IFlexible<*>>()

        list.forEach {
            val item1 = WomenLocalBrandItem(it)

            //Add Item to List
            mItems.add(item1)
        }

        return mItems
    }

    fun setUpListAdapter(mItems: ArrayList<IFlexible<*>>, recycler_view: RecyclerView, context: Context, adapterActions: AdapterActions? = null) {
        adapter = ListHelper.setUpAdapter(mItems, recycler_view, context)
        adapter?.notifyItemRangeInserted(0, mItems.size)
        ListHelper.setAdapterListener(adapterActions)
    }

    private fun filter(constraint: String) {
        adapter?.setFilter(constraint)
        adapter?.filterItems()
    }

    fun getWomenClothingItem(position: Int): WomenClothing? {
        return ListHelper.getListItem<WomenClothingItem>(position)?.womenClothing
    }
}