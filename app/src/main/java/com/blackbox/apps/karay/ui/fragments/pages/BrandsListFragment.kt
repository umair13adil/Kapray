package com.blackbox.apps.karay.ui.fragments.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_recycler_view.*


class BrandsListFragment : BaseFragment() {

    private lateinit var viewModel: PagesViewModel

    companion object {

        fun newInstance(): BrandsListFragment {
            val fragment = BrandsListFragment()
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

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val brands = viewModel.getListOfWomenClothingBrands()
        viewModel.setUpListAdapter(brands, recycler_view, activity!!)
    }
}
