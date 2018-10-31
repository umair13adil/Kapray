package com.blackbox.apps.karay.ui.items

import android.animation.Animator
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.helpers.AnimatorHelper
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFilterable
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.utils.DrawableUtils
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.item_women_brand_local.view.*

class WomenLocalBrandItem(val womenLocal: WomenLocalBrand) : AbstractFlexibleItem<WomenLocalBrandItem.ViewHolder>(), IFilterable<String> {

    init {
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
        if (o is WomenLocalBrandItem) {
            val inItem = o as WomenLocalBrandItem?
            return this.womenLocal.id == inItem!!.womenLocal.id
        }
        return false
    }

    override fun hashCode(): Int {
        return womenLocal.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_women_brand_local
    }

    override fun filter(constraint: String?): Boolean {
        /*return womenLocal.originalTitle != null && womenLocal.originalTitle?.toLowerCase()?.trim()?.contains(constraint!!)!! ||
                womenLocal.overview != null && womenLocal.overview?.toLowerCase()?.trim()?.contains(constraint!!)!!*/
        return true
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): ViewHolder {
        return ViewHolder(view!!, adapter!!)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, viewHolder: ViewHolder, position: Int, payloads: MutableList<Any>?) {

        Picasso.with(viewHolder.brandLogo.context)
                .load(womenLocal.logo_url)
                .into(viewHolder.brandLogo)

        viewHolder.brandName.text = womenLocal.brand

        val context = viewHolder.itemView.context
        val drawable: Drawable = DrawableUtils.getSelectableBackgroundCompat(
                ContextCompat.getColor(context, R.color.colorWhite),
                ContextCompat.getColor(context, R.color.colorAccent), // pressed background
                ContextCompat.getColor(context, R.color.colorPrimaryDark)) // ripple color
        DrawableUtils.setBackgroundCompat(viewHolder.frontView, drawable)
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        var brandLogo: AppCompatImageView
        var brandName: AppCompatTextView

        init {
            this.brandName = view.txt_brand_name
            this.brandLogo = view.img_brand_logo
        }

        override fun scrollAnimators(animators: List<Animator>, position: Int, isForward: Boolean) {
            AnimatorHelper.slideInFromTopAnimator(animators, itemView, mAdapter.recyclerView)
        }

        override fun getActivationElevation(): Float {
            return 1.5f
        }
    }

    override fun toString(): String {
        return "Item[" + super.toString() + "]"
    }
}
