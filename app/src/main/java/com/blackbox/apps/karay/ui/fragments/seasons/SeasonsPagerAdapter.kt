package com.blackbox.apps.karay.ui.fragments.seasons

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.blackbox.apps.karay.models.Seasons

class SeasonsPagerAdapter() : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): String {
        val seasons = Seasons.values()[position]
        return seasons.value
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return Seasons.values().size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val seasons = Seasons.values()[position]
        return seasons.value
    }

}