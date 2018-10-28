package com.blackbox.apps.karay.ui.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.utils.setTypeface
import kotlinx.android.synthetic.main.fragment_additional_info.*


class AdditionalInfoFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_additional_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContent()
    }

    private fun setContent(){

        btn_skip.typeface = setTypeface(activity!!)
        txt_h1.typeface = setTypeface(activity!!)
        btn_save.typeface = setTypeface(activity!!)
    }
}