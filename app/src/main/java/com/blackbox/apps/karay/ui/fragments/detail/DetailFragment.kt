package com.blackbox.apps.karay.ui.fragments.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.detail_layout.*
import java.io.File

class DetailFragment : BaseFragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var clothingItem:WomenClothing

    companion object {

        private const val ARG_ITEM = "womenClothing"
        var LABEL = ""

        fun bundleArgs(item: WomenClothing?): Bundle {
            return Bundle().apply {
                this.putParcelable(ARG_ITEM, item)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.detail_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)

        //Get parcelable movie object here
        clothingItem = (arguments?.getParcelable(ARG_ITEM) as? WomenClothing)!!
        LABEL = clothingItem.brand_name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContent()
    }

    private fun setContent() {

        //Set content in Collapsing Toolbar
        toolbar_layout?.title = clothingItem.brand_name
        toolbar_layout?.setCollapsedTitleTextAppearance(R.style.collapsedAppBar)
        toolbar_layout?.setExpandedTitleTextAppearance(R.style.expandedAppBar)
        toolbar_layout?.setContentScrimColor(ContextCompat.getColor(activity!!, R.color.colorPrimaryDark))
        toolbar_layout.setExpandedTitleColor(ContextCompat.getColor(activity!!, android.R.color.transparent))
        toolbar_layout.setStatusBarScrimColor(ContextCompat.getColor(activity!!, android.R.color.transparent))


        Glide.with(activity!!)
                .load(File(clothingItem.image))
                .into(img_header)

        //Set content in details section
        txt_title?.text = clothingItem.brand_name
        txt_description?.text = clothingItem.date_purchased
    }

}
