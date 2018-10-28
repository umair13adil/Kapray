package com.blackbox.apps.karay.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.base.BaseActivity
import com.blackbox.apps.karay.ui.fragments.add.AddNewFragment
import com.blackbox.apps.karay.utils.createImageDirectories
import com.blackbox.apps.karay.utils.helpers.CompressionHelper
import com.michaelflisar.rxbus2.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Create App Directories
        createImageDirectories()

        val navController = Navigation.findNavController(this, R.id.main_fragment)

        // Set up ActionBar
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)

        //Setup navigation view with navigation controller
        navigation_view.setupWithNavController(navController)

        navController.addOnNavigatedListener { controller, destination ->

            /*if (destination.id == R.id.edit_post_details) {
                showToolBarOption(false)
            } else {
                showToolBarOption(true)
            }*/
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(drawer_layout, Navigation.findNavController(this, R.id.main_fragment))
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val filterItem = menu.findItem(R.id.action_search)

        searchItem.isVisible = true

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
            val searchManager: SearchManager = this@MainActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this@MainActivity.componentName))

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {

                    if (!searchView.isIconified) {
                        searchView.isIconified = true
                    }

                    //Clear focus & close search
                    searchItem.collapseActionView()
                    searchView.clearFocus()

                    //Send 'QUERY SUBMITTED' flag to call search API
                    //RxBus.get().send(SearchQuery(query, Constants.SEARCH_QUERY_SUBMITTED))

                    return false
                }

                override fun onQueryTextChange(s: String): Boolean {

                    //Send search query to subscribers
                    //RxBus.get().send(SearchQuery(s, Constants.SEARCH_AND_FILTER))

                    return false
                }
            })
            true
        }

        filterItem.setOnMenuItemClickListener {
            /*val dialog: DialogFragment = MovieFilterDialog()
            dialog.show(supportFragmentManager, "filterDialog")*/
            true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            /* R.id.all_movies_fragment -> doNavigation(item, R.id.movies_fragment)
             R.id.fav_movies_fragment -> doNavigation(item, R.id.fav_movies_fragment)
             R.id.fav_suggested_fragment -> doNavigation(item, R.id.fav_suggested_fragment)*/
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doNavigation(item: MenuItem, id: Int): Boolean {
        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, id))
    }

    private fun showToolBarOption(show: Boolean) {
        if (show) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == AddNewFragment.CAPTURE_PHOTO && resultCode == RESULT_OK) {
                showLoading(getString(R.string.txt_preparing_image))

                CompressionHelper.compressImage(this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onNext = { imageCaptured ->
                                    hideLoading()
                                    //Send result here
                                    RxBus.get().send(imageCaptured)
                                },
                                onError = {
                                    hideLoading()
                                }
                        )

            }
        }

    }

}
