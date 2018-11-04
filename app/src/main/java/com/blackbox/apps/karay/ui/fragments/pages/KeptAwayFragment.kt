package com.blackbox.apps.karay.ui.fragments.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.enums.AdapterActions
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.ui.fragments.main.MainViewModel
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import kotlinx.android.synthetic.main.fragment_view_clothings.*

class KeptAwayFragment : BaseFragment(), AdapterActions {

    private lateinit var viewModel: MainViewModel

    companion object {

        fun newInstance(): KeptAwayFragment {
            val fragment = KeptAwayFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_view_clothings, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setupTabs(tab_seasons)

        viewModel.setUpListAdapter(recycler_view, activity!!)
    }

    override fun onTaskClick(view: View?, position: Int) {

    }

    override fun onListLoaded(mItems: ArrayList<IFlexible<*>>) {

    }

    override fun onResume() {
        super.onResume()

        viewModel.refreshAdapter()
    }
}
