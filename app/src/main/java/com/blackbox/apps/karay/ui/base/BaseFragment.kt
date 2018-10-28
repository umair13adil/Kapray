package com.blackbox.apps.karay.ui.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.post.Post
import com.blackbox.apps.karay.ui.fragments.detail.DetailFragment
import com.blackbox.apps.karay.ui.items.PostItem
import com.blackbox.apps.karay.ui.items.ProgressItem
import com.michaelflisar.rxbus2.interfaces.IRxBusQueue
import com.michaelflisar.rxbus2.rx.RxDisposableManager
import dagger.android.support.AndroidSupportInjection
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.SelectableAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.processors.BehaviorProcessor
import kotlinx.android.synthetic.main.progress_item.*
import org.reactivestreams.Publisher
import javax.inject.Inject


abstract class BaseFragment : Fragment(),
        FlexibleAdapter.OnItemClickListener,
        FlexibleAdapter.OnItemSwipeListener, IRxBusQueue {

    private val mResumedProcessor = BehaviorProcessor.createDefault(false)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //List Adapter
    var adapter: FlexibleAdapter<*>? = null
    private var layoutManager: LinearLayoutManager? = null
    private var mItems: ArrayList<IFlexible<*>>? = null
    private var isExpanded = false
    var progressItem = ProgressItem()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private val TAG = "BaseFragment"
    var fragmentActions: FragmentActions? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        adapter?.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            adapter?.onRestoreInstanceState(savedInstanceState)
        }
    }

    //*********************//
    // List Adapter Start
    //*********************//

    /**
     * This will setup list adapter.
     */
    fun setUpAdapter(recyclerView: RecyclerView) {

        adapter = FlexibleAdapter(mItems, this, true)
        adapter?.setMode(SelectableAdapter.Mode.SINGLE)

        adapter?.setOnlyEntryAnimation(false)
                ?.setAnimationInterpolator(AccelerateDecelerateInterpolator())
                ?.setAnimationDelay(200L)
                ?.setAnimationOnForwardScrolling(true)
                ?.setAnimationOnReverseScrolling(false)

        layoutManager = SmoothScrollLinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()

        adapter?.setSwipeEnabled(true)
                ?.setNotifyChangeOfUnfilteredItems(true)
    }

    fun setEndlessScroll(scrollListener: FlexibleAdapter.EndlessScrollListener) {
        (adapter as? FlexibleAdapter<ProgressItem>)
                ?.setLoadingMoreAtStartUp(false)
                ?.setEndlessPageSize(20) //Endless is automatically disabled if newItems < 20 (Size of our response from server)
                ?.setEndlessScrollListener(scrollListener, progressItem)
    }

    //List Adapter's Callback
    override fun onItemSwipe(position: Int, direction: Int) {

        //Refresh tile
        adapter!!.notifyItemChanged(position)

        //Send Parent Swipe Callback
        if (direction == ItemTouchHelper.RIGHT) {
            /* if (listActions != null)
                 listActions?.swipedRight(true, position, 0)*/
        } else {
            /*if (listActions != null)
                listActions?.swipedLeft(true, position, 0)*/
        }
    }

    //List Adapter's Callback
    override fun onActionStateChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {}

    //List Adapter's Callback
    override fun onItemClick(view: View?, position: Int): Boolean {

        try {
            val bundle = DetailFragment.bundleArgs(getListObject(position))
            //Navigation.findNavController(view!!).navigate(R.id.action_movieFragment_to_detailFragment, bundle)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Log.e(TAG, "Error: %s", e)
        }

        return true
    }

    /***
     * This will clear all items from list.
     ***/
    fun clearListData() {
        try {
            if (isAdapterNotEmpty()) {
                mItems?.clear()
                adapter?.clear()
                adapter?.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /***
     * Will return true if items are added in adapter.
     ***/
    private fun isAdapterNotEmpty(): Boolean {
        return mItems != null && mItems?.isNotEmpty()!!
    }

    /***
     * This will clear filter from list adapter.
     ***/
    fun clearFilter() {
        if (isAdapterNotEmpty()) {
            adapter?.setFilter(null)
            adapter?.filterItems(mItems!! as List<Nothing>, 350)
        }
    }

    /***
     * This will set filter to list adapter.
     ***/
    fun setFilter(query: String) {
        if (isAdapterNotEmpty()) {
            if (adapter?.hasNewFilter(query)!!) {
                adapter?.setFilter(query)
                adapter?.filterItems(mItems!! as List<Nothing>, 350)
            } else {
                clearFilter()
            }
        }
    }

    /***
     * This will populate list adapter with data keeping first item as selected.
     ***/
    fun showOrHideList(recyclerView: RecyclerView, emptyView: View, postList: List<Post>) {

        if (mItems == null)
            mItems = arrayListOf<IFlexible<*>>()

        try {

            for (post in postList) {

                //Parent Item is an expandable with Level=0
                val movieItem = PostItem(post)

                //Add Item to List
                mItems?.add(movieItem)
            }

            adapter?.updateDataSet(mItems!! as List<Nothing>)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        //Show or Hide list
        if (!isAdapterNotEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
    }

    /**
     * This will return parent task item from list.
     */
    fun getListObject(position: Int): Post? {
        val item = adapter?.getItem(position) as? PostItem
        return item?.post
    }


    //*********************//
    // List Adapter End
    //*********************//

    protected fun startFragment(fragment: BaseFragment, clearBackstack: Boolean, replace: Boolean, forceStart: Boolean) {
        if (fragmentActions != null) {
            fragmentActions!!.startFragment(fragment, clearBackstack, replace, forceStart)
        }
    }

    protected fun finish() {
        if (fragmentActions != null) {
            fragmentActions!!.finishFragment()
        }
    }

    interface FragmentActions {

        fun startFragment(fragment: BaseFragment, clearBackstack: Boolean, replace: Boolean, forceStart: Boolean)

        fun finishFragment()
    }

    // --------------
    // Commons
    // --------------

    fun showLoading(view: View?) {
        progress_bar?.visibility = View.VISIBLE
        view?.visibility = View.GONE
        progress_bar?.isIndeterminate = true
    }

    fun hideLoading(view: View?) {

        //Show layout with animation
        view?.animate()
                ?.alpha(1f)
                ?.setDuration(resources.getInteger(R.integer.anim_duration_long).toLong())
                ?.setListener(null)

        //Hide Progress Layout
        progress_bar?.animate()
                ?.translationY(progress_bar.height.toFloat())
                ?.alpha(0.0f)
                ?.setDuration(resources.getInteger(R.integer.anim_duration_long).toLong())
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        progress_bar.visibility = View.GONE
                    }
                })
    }

    fun hideViewWithDelay(view: View?){
        //Hide Layout
        view?.animate()
                ?.translationY(view.height.toFloat())
                ?.alpha(0.0f)
                ?.setDuration(resources.getInteger(R.integer.anim_duration_long).toLong())
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.visibility = View.GONE
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        mResumedProcessor.onNext(true)
    }

    override fun onPause() {
        mResumedProcessor.onNext(false)
        super.onPause()
    }

    override fun onDestroy() {
        RxDisposableManager.unsubscribe(this)
        super.onDestroy()
    }

    // --------------
    // Interface RxBus
    // --------------

    override fun isBusResumed(): Boolean {
        return mResumedProcessor.value!!
    }

    override fun getResumeObservable(): Publisher<Boolean> {
        return mResumedProcessor
    }
}
