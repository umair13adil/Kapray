package com.blackbox.apps.karay.ui.activities

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.rxbus.AppEvents
import com.blackbox.apps.karay.models.rxbus.EventData
import com.blackbox.apps.karay.ui.activities.login.LoginActivity
import com.blackbox.apps.karay.ui.base.BaseActivity
import com.blackbox.apps.karay.ui.fragments.add.AddNewFragment
import com.blackbox.apps.karay.ui.fragments.detail.DetailFragment
import com.blackbox.apps.karay.ui.fragments.main.MainViewModel
import com.blackbox.apps.karay.utils.ColorUtils
import com.blackbox.apps.karay.utils.commons.Constants
import com.blackbox.apps.karay.utils.commons.Preferences
import com.blackbox.apps.karay.utils.createImageDirectories
import com.blackbox.apps.karay.utils.helpers.CompressionHelper
import com.blackbox.apps.karay.utils.helpers.PermissionsHelper
import com.blackbox.apps.karay.utils.helpers.SearchHelper
import com.blackbox.plog.pLogs.PLog
import com.blackbox.plog.pLogs.models.LogLevel
import com.google.firebase.auth.FirebaseAuth
import com.michaelflisar.rxbus2.RxBus
import com.michaelflisar.rxbus2.RxBusBuilder
import com.michaelflisar.rxbus2.rx.RxBusMode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"
    private var showMenu = false
    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkStoragePermissions()
        checkCameraPermissions()

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        //Sync all data between FireBase & local RealmDb
        viewModel.syncAll()

        //Create App Directories
        createImageDirectories()

        navController = Navigation.findNavController(this, R.id.main_fragment)

        // Set up ActionBar
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)

        //Setup navigation view with navigation controller
        navigation_view.setupWithNavController(navController)

        navController.addOnNavigatedListener { controller, destination ->

            when {
                controller.currentDestination?.id == R.id.fragment_my_wardrobe -> {
                    setDefaultOptions()
                }
                controller.currentDestination?.id == R.id.fragment_brands_list -> {
                    showMenu = false
                    invalidateOptionsMenu()
                    showToolBarOption(true)
                }
                controller.currentDestination?.id == R.id.detailFragment -> {
                    showMenu = false
                    showToolBarOption(false)
                }
                else -> setDefaultOptions()
            }
        }


        //Add Local Brands Data
        addLocalBrandsData()

        RxBusBuilder.create(EventData::class.java)
                .withMode(RxBusMode.Main)
                .subscribe { bus ->
                    PLog.logThis(TAG, "RxBusBuilder", bus.toString(), LogLevel.INFO)
                    when (bus.events) {
                        AppEvents.STYLE_ACTION_BAR -> {
                            styleStatusBar()
                        }
                    }
                }
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

        val logoutItem = menu.findItem(R.id.action_logout)
        logoutItem.setOnMenuItemClickListener {
            doLogout()
        }

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

    private fun doLogout(): Boolean {
        FirebaseAuth.getInstance().signOut()
        Preferences.getInstance().save(Constants.KEY_LOGIN, false)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        return false
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

    private fun setDefaultOptions() {
        showMenu = true
        invalidateOptionsMenu()
        showToolBarOption(true)
        styleStatusBar(setDefault = true)
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

    private fun styleStatusBar(setDefault: Boolean = false) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!setDefault)
                window.statusBarColor = ColorUtils.manipulateColor(DetailFragment.mPaletteColor, 0.32f)
            else
                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }
    }

    private fun checkStoragePermissions() {
        //Request for storage permissions then start camera
        PermissionsHelper.requestStoragePermissions(this)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { callback ->

                        },
                        onError = {

                        }
                )
    }

    private fun checkCameraPermissions() {
        //Request for camera permissions and then open camera
        PermissionsHelper.requestCameraPermissions(this)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { callback ->

                        },
                        onError = {

                        }
                )
    }
}
