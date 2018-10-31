package com.blackbox.apps.karay.models.enums

import android.view.View
import eu.davidea.flexibleadapter.items.IFlexible

interface AdapterActions {

    fun onTaskClick(view: View?, position: Int)

    fun onListLoaded(mItems: ArrayList<IFlexible<*>>)
}