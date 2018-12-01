package com.blackbox.apps.karay.ui.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
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


    }

    override fun onStart() {
        super.onStart()

        /*val listOfClothing = viewModel.getListOfWomenClothing()

        if (listOfClothing.isEmpty()) {
            setEmptyContentLayout()
        } else {
            setHomeLayout()
        }*/
    }

    @SuppressLint("RestrictedApi")
    private fun setEmptyContentLayout() {

        //Set Fonts
        txt_h1?.typeface = setTypeface(activity!!)
        txt_b1?.typeface = setTypeface(activity!!)
        btn_add?.typeface = setTypeface(activity!!)

        //Show/Hide Views
        content_empty?.visibility = View.VISIBLE
        viewPager?.visibility = View.GONE
        fab_add?.visibility = View.GONE

        btn_add?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_MainFragment_to_AddNewFragment)
        }
    }

    private fun setHomeLayout() {

        val adapter = MainPagerAdapter(activity!!, childFragmentManager)

        // Set the adapter onto the view pager
        viewPager?.adapter = adapter

        //Show/Hide Views
        content_empty?.visibility = View.GONE
        viewPager?.visibility = View.VISIBLE

        fab_add?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_MainFragment_to_AddNewFragment)
        }
    }
}