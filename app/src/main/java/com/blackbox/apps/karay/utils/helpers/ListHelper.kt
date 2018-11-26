package com.blackbox.apps.karay.utils.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blackbox.apps.karay.models.enums.AdapterActions
import com.blackbox.apps.karay.utils.recyclerViewUtils.SpacesItemDecoration
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.SelectableAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollStaggeredLayoutManager
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("StaticFieldLeak")
object ListHelper : FlexibleAdapter.OnActionStateListener, FlexibleAdapter.OnItemClickListener {

    private val TAG = "JobListHelper"

    lateinit var adapter: FlexibleAdapter<*>
    private var mItems: ArrayList<IFlexible<*>> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    var adapterActions: AdapterActions? = null

    fun setAdapterListener(listener: AdapterActions?) {
        adapterActions = listener
    }

    //List Adapter
    fun setUpAdapter(mItems: ArrayList<IFlexible<*>>?, recyclerView: RecyclerView, context: Context): FlexibleAdapter<*> {

        this.adapter = FlexibleAdapter(mItems, this, true)
        this.mItems = mItems!!
        this.recyclerView = recyclerView

        adapter.setMode(SelectableAdapter.Mode.SINGLE)

        adapter.setOnlyEntryAnimation(false)
                ?.setAnimationInterpolator(AccelerateDecelerateInterpolator())
                ?.setAnimationDelay(1000L)
                ?.setAnimationOnForwardScrolling(true)
                ?.setAnimationOnReverseScrolling(true)
                ?.setAnimationDuration(100)
                ?.setAnimationEntryStep(true)

        val layoutManager = SmoothScrollStaggeredLayoutManager(context, 2, StaggeredGridLayoutManager.VERTICAL)
        //layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val decoration = SpacesItemDecoration(16)
        recyclerView.addItemDecoration(decoration)

        adapter.setAnimateChangesWithDiffUtil(true)
                ?.setAnimateToLimit(10)
                ?.setNotifyMoveOfFilteredItems(true)
                ?.setNotifyChangeOfUnfilteredItems(true)

        return adapter
    }

    /**
     * This will return item from list.
     */
    fun <T> getListItem(position: Int): T? {
        val item = adapter.getItem(position) as? T
        return item
    }


    /***
     * This will clear all items from list.
     ***/
    fun clearListData(recyclerView: RecyclerView?) {
        try {
            recyclerView?.recycledViewPool?.clear()

            if (mItems.isNotEmpty()) {
                mItems.clear()
                adapter.clear()
                adapter.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /***
     * This will smoothly scroll to selected position with offset.
     ***/
    fun scrollToPosition(position: Int, offset: Int = 0, delay: Long = 0) {

        if (delay > 0) {
            Observable.create<Int> { it.onNext(position) }
                    .delay(delay, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = { pos ->
                                scroll(pos)
                            }
                    )
        } else {
            scroll(position)
        }
    }

    private fun scroll(position: Int) {
        try {
            recyclerView.smoothScrollToPosition(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearFilters(position: Int, delay: Long = 0) {

        Observable.create<Int> { it.onNext(position) }
                .delay(delay, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { pos ->
                            //if (adapter?.hasFilter()!! && adapter?.getFilter(String::class.java)?.isNotEmpty()!!) {
                            adapter.setFilter("")
                            adapter.filterItems(200)
                            //}
                            scrollToPosition(position, delay = 800) //Scroll to previous position after 0.8 seconds delay
                        }
                )
    }

    //List Adapter's Callback
    override fun onActionStateChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {

    }

    //List Adapter's Callback
    override fun onItemClick(view: View?, position: Int): Boolean {
        adapterActions?.onTaskClick(view, position)
        return true
    }

    /***
     * This will select first item on list.
     ***/
    private fun selectFirstItem() {
        if (mItems.isNotEmpty()) {
            onItemClick(adapter.recyclerView?.getChildAt(0), 0)
        }

        //Notify that list is loaded
        adapterActions?.onListLoaded(mItems)
    }

    fun removeItem(position: Int, isParent: Boolean) {
        try {
            if (mItems.isNotEmpty()) {

                adapter.removeItem(position)
                adapter.notifyDataSetChanged()

                if (isParent)
                    mItems.removeAt(position)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeItem(position: Int) {
        try {
            if (mItems.isNotEmpty()) {

                adapter.removeItem(position)
                adapter.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isAdapterEmpty(): Boolean {
        return adapter.itemCount == 0
    }

    fun adapterCount(): Int {
        return adapter.currentItems.size
    }

    fun refreshList() {
        adapter.notifyDataSetChanged()
        selectFirstItem()
    }

    fun refreshItem(position: Int) {
        adapter.notifyItemChanged(position)
    }


}