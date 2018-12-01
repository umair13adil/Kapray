package com.blackbox.apps.karay.ui.fragments.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.enums.AdapterActions
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.ui.fragments.detail.DetailFragment
import com.blackbox.plog.pLogs.PLog
import com.blackbox.plog.pLogs.models.LogLevel
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_recycler_view.*


class MyWardrobeFragment : BaseFragment(), AdapterActions {

    private lateinit var viewModel: PagesViewModel
    private val TAG = "MyWardrobeFragment"

    companion object {

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

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clothings = viewModel.getListOfWomenClothing()
        viewModel.setUpListAdapter(clothings, recycler_view, activity!!, adapterActions = this)

        fab_add?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_MainFragment_to_AddNewFragment)
        }
    }

    override fun onTaskClick(view: View?, position: Int) {
        PLog.logThis(TAG,"onTaskClick","onTaskClick $position", LogLevel.INFO)

        viewModel.getWomenClothingItem(position)?.let {

            val bundle = DetailFragment.bundleArgs(it)
            Navigation.findNavController(view!!).navigate(R.id.action_MyWardrobeFragment_to_detailFragment, bundle)
        }
    }

    override fun onListLoaded(mItems: ArrayList<IFlexible<*>>) {

    }

}
