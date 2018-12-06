package com.blackbox.apps.karay.ui.fragments.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.base.BaseFragment
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_my_wardrobe.*
import kotlinx.android.synthetic.main.fragment_recycler_view.*


class BrandsListFragment : BaseFragment() {

    private lateinit var viewModel: PagesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_my_wardrobe, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PagesViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()

        img_empty_placeholder?.visibility = View.GONE

        viewModel.getListOfWomenClothingBrands()
                .subscribeBy(
                        onNext = {
                            viewModel.setUpListAdapter(it, recycler_view, activity!!, stickyHeaders = false)
                        },
                        onError = {
                            it.printStackTrace()
                        },
                        onComplete = {
                            hideLoading()
                        }
                )

    }
}
