package com.blackbox.apps.karay.ui.fragments.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.base.BaseFragment

class DetailFragment : BaseFragment() {

    private lateinit var viewModel: DetailViewModel

    companion object {

        private const val ARG_MOVIE = "womenClothing"

        /*fun bundleArgs(movie: Post?): Bundle {
            return Bundle().apply {
                this.putParcelable(ARG_MOVIE, movie)
            }
        }*/
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.detail_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setContent() {

        //Set content in Collapsing Toolbar
        /*toolbar_layout?.title = womenClothing?.title
        toolbar_layout?.setCollapsedTitleTextAppearance(R.style.collapsedAppBar)
        toolbar_layout?.setExpandedTitleTextAppearance(R.style.expandedAppBar)
        toolbar_layout?.setContentScrimColor(ContextCompat.getColor(activity!!, R.color.colorPrimaryDark))
        toolbar_layout.setExpandedTitleColor(ContextCompat.getColor(activity!!, android.R.color.transparent))
        toolbar_layout.setStatusBarScrimColor(ContextCompat.getColor(activity!!, android.R.color.transparent))


        Picasso.with(activity).load(Constants.BASE_URL_IMAGE + womenClothing?.posterPath).into(img_header)

        //Set content in details section
        txt_title?.text = womenClothing?.title
        txt_description?.text = womenClothing?.overview*/
    }

}
