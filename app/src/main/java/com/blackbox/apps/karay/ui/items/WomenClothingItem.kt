package com.blackbox.apps.karay.ui.items

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.data.repositories.network.FireBaseHelper
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.utils.GlideApp
import com.blackbox.apps.karay.utils.setTypeface
import com.bumptech.glide.Glide
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.davidea.flexibleadapter.items.IFilterable
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.item_women_clothing.view.*
import java.io.File

class WomenClothingItem(val womenClothing: WomenClothing, val headerItem: SeasonsHeaderItem?) : AbstractSectionableItem<WomenClothingItem.ViewHolder, SeasonsHeaderItem>(headerItem), IFilterable<String> {

    init {
        this.header = headerItem
        isDraggable = false
        isSwipeable = true
        isSelectable = true
    }

    //Boolean flags
    var isSelected = false

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement.
     */
    override fun equals(o: Any?): Boolean {
        if (o is WomenClothingItem) {
            val inItem = o as WomenClothingItem?
            return this.womenClothing.id == inItem!!.womenClothing.id
        }
        return false
    }

    override fun hashCode(): Int {
        return womenClothing.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_women_clothing
    }

    override fun filter(constraint: String?): Boolean {
        return womenClothing.brand_name.toLowerCase().contains(constraint?.toLowerCase()!!)
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): ViewHolder? {
        return ViewHolder(view!!, adapter!!)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, viewHolder: ViewHolder, position: Int, payloads: MutableList<Any>?) {

        val mContext = viewHolder.brandLogo.context

        //If is URL
        if (womenClothing.image.contains("https:")) {

            FireBaseHelper.getWomenClothingImageById(womenClothing.id)
                    .subscribeBy(
                            onNext = {
                                GlideApp.with(mContext)
                                        .load(it)
                                        .into(viewHolder.clothingImage)
                            },
                            onError = {
                                it.printStackTrace()
                            }
                    )
        } else {
            Glide.with(mContext)
                    .load(File(womenClothing.image))
                    .into(viewHolder.clothingImage)
        }

        if (womenClothing.brand_logo_url.isNotEmpty()) {

            Glide.with(viewHolder.brandLogo.context)
                    .load(womenClothing.brand_logo_url)
                    .into(viewHolder.brandLogo)
        }

        showTextOrHide(viewHolder.brandName, womenClothing.brand_name)
        showTextOrHide(viewHolder.datePurchased, womenClothing.date_purchased)
        showTextOrHide(viewHolder.season, womenClothing.season_info)
    }

    private fun showTextOrHide(view: AppCompatTextView, text: String) {
        if (text.isNotEmpty()) {
            view.visibility = View.VISIBLE
            view.text = text
            view.typeface = setTypeface(view.context)
        } else {
            view.visibility = View.GONE
        }
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        var clothingImage: AppCompatImageView
        var brandLogo: AppCompatImageView
        var brandName: AppCompatTextView
        var datePurchased: AppCompatTextView
        var season: AppCompatTextView

        init {
            this.brandName = view.txt_brand_name
            this.datePurchased = view.txt_date_purchased
            this.season = view.txt_season
            this.brandLogo = view.img_brand_logo
            this.clothingImage = view.img_cloth
        }
    }

    override fun toString(): String {
        return "Item[" + super.toString() + "]"
    }
}
