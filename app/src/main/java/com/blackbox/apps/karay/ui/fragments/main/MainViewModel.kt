package com.blackbox.apps.karay.ui.fragments.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.karay.data.repositories.main.MainRepository
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.models.enums.Seasons
import com.blackbox.apps.karay.ui.items.WomenClothingItem
import com.blackbox.apps.karay.utils.helpers.ListHelper
import com.google.android.material.tabs.TabLayout
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import javax.inject.Inject

class MainViewModel @Inject constructor(private var app: Application, private var mainRepository: MainRepository) : ViewModel() {

    private val TAG = "MainViewModel"
    private var adapter: FlexibleAdapter<*>? = null

    fun getListOfWomenClothing(inCloset: Boolean): List<WomenClothing> {
        return mainRepository.getListOfWomenClothing(inCloset)
    }

    fun getListOfWomenClothing(): List<WomenClothing> {
        return mainRepository.getListOfWomenClothing()
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

    fun setUpListAdapter(clothings: List<WomenClothing>, recycler_view: RecyclerView, context: Context) {

        val mItems = arrayListOf<IFlexible<*>>()

        try {
            var isSelected = true

            for (clothing in clothings) {

                val jobTaskItem = WomenClothingItem(clothing)
                jobTaskItem.isSelected = isSelected
                isSelected = false

                //Add Item to List
                mItems.add(jobTaskItem)
            }

            adapter = ListHelper.setUpAdapter(mItems, recycler_view, context)
            adapter?.notifyItemRangeInserted(0, mItems.size)


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun filter(constraint: String) {
        adapter?.setFilter(constraint)
        adapter?.filterItems()
    }

    fun refreshAdapter() {
        //adapter?.notifyDataSetChanged()
    }
}