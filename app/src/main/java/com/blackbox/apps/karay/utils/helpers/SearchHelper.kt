package com.blackbox.apps.karay.utils.helpers

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.view.MenuItem
import android.widget.SearchView
import com.blackbox.apps.karay.R
import com.michaelflisar.rxbus2.RxBus

object SearchHelper {

    //Search
    const val SEARCH_AND_FILTER = "%filter1%"
    const val CLEAR_SEARCH = "%clear1%"
    const val SEARCH_QUERY_SUBMITTED = "%query1%"

    private var showMenu = true

    fun setMenuVisibility(show: Boolean) {
        this.showMenu = show
    }

    fun setupSearchItem(searchItem: MenuItem, activity: Activity) {

        searchItem.isVisible = showMenu

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {

                //Send 'CLEAR SEARCH' value to clear filter
                //RxBus.get().send(SearchQuery("", Constants.CLEAR_SEARCH))

                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }
        })

        searchItem.setOnMenuItemClickListener {

            val searchView = searchItem.actionView.findViewById<SearchView>(R.id.action_search) as SearchView
            val searchManager: SearchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {

                    if (!searchView.isIconified) {
                        searchView.isIconified = true
                    }

                    //Clear focus & close search
                    searchItem.collapseActionView()
                    searchView.clearFocus()

                    //Send 'QUERY SUBMITTED' flag to call search API
                    RxBus.get().send(SearchQuery(query, SEARCH_QUERY_SUBMITTED))

                    return false
                }

                override fun onQueryTextChange(s: String): Boolean {

                    //Send search query to subscribers
                    RxBus.get().send(SearchQuery(s, SEARCH_AND_FILTER))

                    return false
                }
            })
            true
        }
    }
}

data class SearchQuery(val query: String, val action: String) {

}