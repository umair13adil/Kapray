package com.blackbox.apps.karay.ui.fragments.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.enums.AdapterActions
import com.blackbox.apps.karay.models.enums.WardrobeType
import com.blackbox.apps.karay.models.rxbus.AppEvents
import com.blackbox.apps.karay.models.rxbus.EventData
import com.blackbox.apps.karay.ui.base.FilterClothingData
import com.blackbox.apps.karay.ui.base.FilterFragment
import com.blackbox.apps.karay.ui.fragments.detail.DetailFragment
import com.blackbox.apps.karay.utils.helpers.SearchEvents
import com.blackbox.apps.karay.utils.helpers.SearchQuery
import com.blackbox.plog.pLogs.PLog
import com.blackbox.plog.pLogs.models.LogLevel
import com.bumptech.glide.Glide
import com.michaelflisar.rxbus2.RxBusBuilder
import com.michaelflisar.rxbus2.rx.RxBusMode
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_my_wardrobe.*
import kotlinx.android.synthetic.main.fragment_recycler_view.*


class MyWardrobeFragment : FilterFragment(), AdapterActions {

    private lateinit var viewModel: PagesViewModel
    private val TAG = "MyWardrobeFragment"

    companion object {

        private const val ARG_TYPE = "wardrobeType"

        fun newInstance(): MyWardrobeFragment {
            val fragment = MyWardrobeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_my_wardrobe, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PagesViewModel::class.java)
        viewModel.doOnStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (arguments?.getString(MyWardrobeFragment.ARG_TYPE))?.let {
            when (it) {
                WardrobeType.ALL.type -> showListOfWomenClothing()
                WardrobeType.IN_CLOSET.type -> showListOfWomenClothing(showByType = true, inCloset = true)
                WardrobeType.KEPT_AWAY.type -> showListOfWomenClothing(showByType = true, inCloset = false)
            }
        }


        fab_add?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_MainFragment_to_AddNewFragment)
        }

        fab_filter?.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.END)
        }

        RxBusBuilder.create(EventData::class.java)
                .withMode(RxBusMode.Main)
                .subscribe { bus ->
                    when (bus.events) {
                        AppEvents.SYNC_COMPLETED -> {
                            PLog.logThis(TAG, "RxBusBuilder", viewModel.getSizeOfList().toString(), LogLevel.INFO)
                            if (bus.data.first().toInt() != viewModel.getSizeOfList()) {
                                showListOfWomenClothing()
                            }
                        }
                    }
                }

        RxBusBuilder.create(SearchQuery::class.java)
                .withMode(RxBusMode.Main)
                .subscribe { bus ->
                    when (bus.action) {
                        SearchEvents.CLEAR_SEARCH -> viewModel.clearFilter()
                        SearchEvents.SEARCH_AND_FILTER -> viewModel.filter(bus.query)
                        SearchEvents.SEARCH_QUERY_SUBMITTED -> viewModel.filter(bus.query)
                    }
                }

        RxBusBuilder.create(FilterClothingData::class.java)
                .withMode(RxBusMode.Main)
                .subscribe { bus ->

                    if (bus.filterApplied) {
                        getFilteredList(bus)
                    } else {
                        showListOfWomenClothing()
                    }
                }
    }

    private fun showListOfWomenClothing(showByType: Boolean = false, inCloset: Boolean = false) {

        showLoading()

        val listOfClothing: Observable<ArrayList<IFlexible<*>>> = if (!showByType) {
            viewModel.getListOfWomenClothing()
        } else {
            viewModel.getListOfWomenClothing(fetchByType = true, inCloset = inCloset)
        }

        listOfClothing.subscribeBy(
                onNext = {
                    if (it.isNotEmpty()) {
                        showEmptyListPlaceholder(true)
                        viewModel.setUpListAdapter(it, recycler_view, activity!!, adapterActions = this, stickyHeaders = true)
                    } else {
                        showEmptyListPlaceholder(false)
                    }
                },
                onError = {
                    it.printStackTrace()
                },
                onComplete = {
                    hideLoading()
                }
        )
    }

    private fun showEmptyListPlaceholder(show: Boolean = false) {

        if (show) {
            showFloatingActionButton(fab_filter)
            img_empty_placeholder?.visibility = View.GONE
        } else {
            hideFloatingActionButton(fab_filter)
            img_empty_placeholder?.visibility = View.VISIBLE

            Glide.with(this)
                    .load(R.drawable.img_empty_list_placeholder)
                    .into(img_empty_placeholder)
        }
    }

    private fun getFilteredList(filterClothingData: FilterClothingData) {

        showLoading()

        viewModel.getFilteredList(filterClothingData)
                .subscribeBy(
                        onNext = {
                            if (it.isNotEmpty()) {
                                showEmptyListPlaceholder(true)
                                viewModel.setUpListAdapter(it, recycler_view, activity!!, adapterActions = this, stickyHeaders = false)
                            } else {
                                showEmptyListPlaceholder(false)
                            }
                        },
                        onError = {
                            it.printStackTrace()
                        },
                        onComplete = {
                            hideLoading()
                        }
                )
    }

    override fun onTaskClick(view: View?, position: Int) {
        viewModel.getWomenClothingItem(position)?.let {

            val bundle = DetailFragment.bundleArgs(it)
            Navigation.findNavController(view!!).navigate(R.id.action_MyWardrobeFragment_to_detailFragment, bundle)
        }
    }

    override fun onListLoaded(mItems: ArrayList<IFlexible<*>>) {

    }

}
