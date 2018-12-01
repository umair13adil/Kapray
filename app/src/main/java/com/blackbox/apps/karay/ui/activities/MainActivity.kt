package com.blackbox.apps.karay.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.base.BaseActivity
import com.blackbox.apps.karay.ui.fragments.add.AddNewFragment
import com.blackbox.apps.karay.utils.createImageDirectories
import com.blackbox.apps.karay.utils.helpers.CompressionHelper
import com.blackbox.apps.karay.utils.helpers.SearchHelper
import com.michaelflisar.rxbus2.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"
    private var showMenu = false
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Create App Directories
        createImageDirectories()

        navController = Navigation.findNavController(this, R.id.main_fragment)

        // Set up ActionBar
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)

        //Setup navigation view with navigation controller
        navigation_view.setupWithNavController(navController)

        navController.addOnNavigatedListener { controller, destination ->

            Log.i(TAG, "${controller.currentDestination?.label} , ${destination.label}")

            if (controller.currentDestination?.id == R.id.main_fragment) {
                showMenu = true
                invalidateOptionsMenu()
            } else if (controller.currentDestination?.id == R.id.fragment_brands_list) {
                showMenu = false
                invalidateOptionsMenu()
                //TODO Hide Filter
            } else if (controller.currentDestination?.id == R.id.detailFragment) {
                showMenu = false
                appbar?.visibility = View.GONE
            } else {
                showMenu = false
                invalidateOptionsMenu()
            }
        }


        //Add Local Brands Data
        addLocalBrandsData()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout)
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
        SearchHelper.setupSearchItem(searchItem, this)
        SearchHelper.setMenuVisibility(showMenu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.fragment_in_closet -> doNavigation(item, R.id.fragment_in_closet)
            R.id.fragment_kept_away -> doNavigation(item, R.id.fragment_kept_away)
            R.id.fragment_brands_list -> doNavigation(item, R.id.fragment_brands_list)
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
