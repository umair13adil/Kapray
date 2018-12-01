package com.blackbox.apps.karay.ui.items

import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.karay.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractHeaderItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.item_header_seasons.view.*

class SeasonsHeaderItem(private val title: String) : AbstractHeaderItem<SeasonsHeaderItem.HeaderViewHolder>() {

    override fun equals(other: Any?): Boolean {
        if (other is SeasonsHeaderItem) {
            val inItem = other as SeasonsHeaderItem?
            return this.title == inItem!!.title
        }
        return false
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_header_seasons
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): HeaderViewHolder {
        return HeaderViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?, holder: HeaderViewHolder?, position: Int, payloads: MutableList<Any>?) {

        if (payloads?.size!! > 0) {
            Log.d(this.javaClass.simpleName, "Payload $payloads")
        } else {
            holder?.mTitle!!.text = title
        }
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

    class HeaderViewHolder(view: View?, adapter: FlexibleAdapter<*>?) : FlexibleViewHolder(view, adapter, true) {

        var mTitle: AppCompatTextView?

        init {
            this.mTitle = view?.txt_title
        }
    }

}