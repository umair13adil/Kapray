package com.blackbox.apps.karay.ui.fragments.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.karay.data.repositories.main.MainRepository
import com.blackbox.apps.karay.models.Seasons
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.ui.items.WomenClothingItem
import com.blackbox.apps.karay.utils.helpers.ListHelper
import com.google.android.material.tabs.TabLayout
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import javax.inject.Inject

class MainViewModel @Inject constructor(private var app: Application, private var mainRepository: MainRepository) : ViewModel() {

    private var adapter: FlexibleAdapter<*>? = null

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

            }

        })
    }

    fun setUpListAdapter(recycler_view: RecyclerView, context: Context) {

        val mItems = arrayListOf<IFlexible<*>>()

        val clothings = getListOfWomenClothing()

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

    fun refreshAdapter() {
        //adapter?.notifyDataSetChanged()
    }
}