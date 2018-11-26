package com.blackbox.apps.karay.ui.fragments.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.karay.data.repositories.main.MainRepository
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.models.enums.AdapterActions
import com.blackbox.apps.karay.models.enums.Seasons
import com.blackbox.apps.karay.ui.items.WomenClothingItem
import com.blackbox.apps.karay.ui.items.WomenLocalBrandItem
import com.blackbox.apps.karay.utils.helpers.ListHelper
import com.google.android.material.tabs.TabLayout
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import javax.inject.Inject

class MainViewModel @Inject constructor(private var app: Application, private var mainRepository: MainRepository) : ViewModel() {

    private val TAG = "MainViewModel"
    private var adapter: FlexibleAdapter<*>? = null

    fun getListOfWomenClothing(inCloset: Boolean): ArrayList<IFlexible<*>> {
        val list = mainRepository.getListOfWomenClothing(inCloset)

        val mItems = arrayListOf<IFlexible<*>>()

        list.forEach {
            val item1 = WomenClothingItem(it)

            //Add Item to List
            mItems.add(item1)
        }

        return mItems
    }

    fun getListOfWomenClothing(): ArrayList<IFlexible<*>> {
        val list = mainRepository.getListOfWomenClothing()

        val mItems = arrayListOf<IFlexible<*>>()

        list.forEach {
            val item1 = WomenClothingItem(it)

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

    fun setupTabs(tab_seasons: TabLayout) {

        for (item in Seasons.values()) {
            tab_seasons.addTab(tab_seasons.newTab().setText(item.value))
        }

        tab_seasons.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                Log.i(TAG, "${p0?.text}")

                if (p0?.text!! == Seasons.ALL.value) {
                    filter("")
                } else
                    filter(p0.text.toString())
            }

        })
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

    fun refreshAdapter() {
        //adapter?.notifyDataSetChanged()
    }

    fun getWomenClothingItem(position: Int): WomenClothing? {
        return ListHelper.getListItem<WomenClothingItem>(position)?.womenClothing
    }
}