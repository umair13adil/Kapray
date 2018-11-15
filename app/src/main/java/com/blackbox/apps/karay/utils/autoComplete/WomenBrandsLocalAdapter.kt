package com.blackbox.apps.karay.utils.autoComplete

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.bumptech.glide.Glide

class WomenBrandsLocalAdapter(internal var mContext: Context, internal var layoutResourceId: Int, var data: List<WomenLocalBrand>) : ArrayAdapter<WomenLocalBrand>(mContext, layoutResourceId, data) {

    internal val TAG = "WomenBrandsLocalAdapter"

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItem = convertView

        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(layoutResourceId, parent, false)

        val womenLocalBrand = data.get(position)

        val image = listItem!!.findViewById<View>(R.id.img_brand_logo) as ImageView
        Glide.with(mContext).load(womenLocalBrand.logo_url).into(image)

        val name = listItem.findViewById<View>(R.id.txt_brand_name) as TextView
        name.text = womenLocalBrand.brand

        return listItem

    }
}
