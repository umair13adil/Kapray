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
import com.squareup.picasso.Picasso

class WomenBrandsLocalAdapter(internal var mContext: Context, internal var layoutResourceId: Int, var data: List<WomenLocalBrand>) : ArrayAdapter<WomenLocalBrand>(mContext, layoutResourceId, data) {

    internal val TAG = "WomenBrandsLocalAdapter"

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItem = convertView

        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(layoutResourceId, parent, false)

        val womenLocalBrand = data.get(position)

        val image = listItem!!.findViewById<View>(R.id.img_brand_logo) as ImageView
        Picasso.with(mContext).load(womenLocalBrand.logo_url).into(image, object : com.squareup.picasso.Callback {
            override fun onSuccess() {
                image.visibility = View.VISIBLE
            }

            override fun onError() {

            }
        })

        val name = listItem.findViewById<View>(R.id.txt_brand_name) as TextView
        name.text = womenLocalBrand.brand

        return listItem

    }
}
