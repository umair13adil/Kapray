package com.blackbox.apps.karay.ui.fragments.main

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.ui.fragments.pages.MyWardrobeFragment

class MainPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): BaseFragment {

        return when (position) {
            0 -> MyWardrobeFragment.newInstance()
            else -> MyWardrobeFragment.newInstance()
        }
    }

    // This determines the number of tabs
    override fun getCount(): Int {
        return 2
    }

    // This determines the title for each tab
    override fun getPageTitle(position: Int): CharSequence? {

        // Generate title based on item position
        return when (position) {
            0 -> mContext.getString(R.string.page_in_closet)
            else -> mContext.getString(R.string.page_in_closet)
        }
    }

}