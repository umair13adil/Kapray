package com.blackbox.apps.karay.ui.fragments.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.utils.setTypeface
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment() {

    private val TAG = "MainFragment"
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEmptyContentLayout()

        viewModel.getListOfWomenClothing().forEach {
            Log.i(TAG,it.toString())
        }
    }

    private fun setEmptyContentLayout(){

        //Set Fonts
        txt_h1.typeface = setTypeface(activity!!)
        txt_b1.typeface = setTypeface(activity!!)
        btn_add.typeface = setTypeface(activity!!)

        //Show/Hide Views
        content_empty.visibility = View.VISIBLE
        content_main.visibility = View.GONE

        btn_add.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_MainFragment_to_AddNewFragment)
        }
    }

    private fun setHomeLayout(){
        val adapter = MainPagerAdapter(activity!!, fragmentManager!!)

        // Set the adapter onto the view pager
        viewPager.adapter = adapter

        sliding_tabs.setupWithViewPager(viewPager)

        //Show/Hide Views
        content_empty.visibility = View.GONE
        content_main.visibility = View.VISIBLE
    }
}