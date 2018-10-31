package com.blackbox.apps.karay.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_recycler_view.*

class AllPostsFragment : BaseFragment() {

    val TAG: String = AllPostsFragment::class.java.simpleName

    private lateinit var viewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PostViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel.setupMultiTab()

        setUpAdapter(recycler_view)

        showLoading(empty_view)
        viewModel.retrieveData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            hideLoading(recycler_view)

                        },
                        onError = {
                            hideLoading(recycler_view)
                        }
                )


        //adapter?.updateDataSet()
    }


    companion object {
        val GET_PHOTO = 11
        val REQUEST_CODE_CHOOSE = 1132
    }


    /*private fun initializeRecyclerView() {

        //create our adapters
        val stickyHeaderAdapter = StickyHeaderAdapter()
        val headerAdapter = HeaderAdapter<PostItem>()

        if (activity == null)
            return


        mAdapter = FastItemAdapter<PostItem>()
        mAdapter!!.withSelectable(true)
        mAdapter!!.withPositionBasedStateManagement(false)

        val fastScrollIndicatorAdapter = FastScrollIndicatorAdapter<PostItem>()

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.addItemDecoration(DividerItemDecoration(activity))
        recycler_view.adapter = fastScrollIndicatorAdapter.wrap(stickyHeaderAdapter.wrap(headerAdapter.wrap(mAdapter)))

        //this adds the Sticky Headers within our list
        val decoration = StickyRecyclerHeadersDecoration(stickyHeaderAdapter)
        recycler_view.addItemDecoration(decoration)

        //so the headers are aware of changes
        stickyHeaderAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                decoration.invalidateHeaders()
            }
        })


        mAdapter!!.withOnClickListener(FastAdapter.OnClickListener<PostItem> { v, adapter, item, position ->
            try {
                val womenLocal = mAdapter!!.getAdapterItem(position)
                var route = womenLocal.route + getString(R.string.google_maps_key)
                val sendLocationToMap = Intent(Intent.ACTION_VIEW,
                        Uri.parse(route))
                startActivity(sendLocationToMap)
            } catch (e1: NullPointerException) {

            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context(), "Google Maps is not Installed", Toast.LENGTH_SHORT).show()
                val uri = Uri.parse("market://details?id=com.google.android.apps.maps")
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                startActivity(goToMarket)
            }
            false
        })

        mAdapter!!.itemFilter.withFilterPredicate(IItemAdapter.Predicate<PostItem> { item, constraint ->

            val fItem = constraint.toString().toLowerCase()
            !item.categoryName.toString().toLowerCase().contains(fItem)
        })

        mAdapter!!.itemFilter.withItemFilterListener(this)
    }*/
}
