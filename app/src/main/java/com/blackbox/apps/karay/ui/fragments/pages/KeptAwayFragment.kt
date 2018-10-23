package com.blackbox.apps.karay.ui.fragments.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.post.Post
import com.blackbox.apps.karay.ui.base.BaseFragment

class KeptAwayFragment : BaseFragment() {

    private lateinit var viewModel: PagesViewModel
    var post: Post? = null

    companion object {

        private const val ARG_MOVIE = "post"

        fun newInstance(): KeptAwayFragment {
            val fragment = KeptAwayFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

        fun bundleArgs(movie: Post?): Bundle {
            return Bundle().apply {
                this.putParcelable(ARG_MOVIE, movie)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.detail_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PagesViewModel::class.java)

        //Get parcelable post object here
        post = arguments?.getParcelable(ARG_MOVIE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContent(post)
    }

    private fun setContent(movie: Post?) {

        //Set content in Collapsing Toolbar
        /*toolbar_layout?.title = post?.title
        toolbar_layout?.setCollapsedTitleTextAppearance(R.style.collapsedAppBar)
        toolbar_layout?.setExpandedTitleTextAppearance(R.style.expandedAppBar)
        toolbar_layout?.setContentScrimColor(ContextCompat.getColor(activity!!, R.color.colorPrimaryDark))
        toolbar_layout.setExpandedTitleColor(ContextCompat.getColor(activity!!, android.R.color.transparent))
        toolbar_layout.setStatusBarScrimColor(ContextCompat.getColor(activity!!, android.R.color.transparent))


        Picasso.with(activity).load(Constants.BASE_URL_IMAGE + post?.posterPath).into(img_header)

        //Set content in details section
        txt_title?.text = post?.title
        txt_description?.text = post?.overview*/
    }

}
