package com.blackbox.apps.karay.ui.fragments.detail

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.data.repositories.network.FireBaseHelper
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.models.enums.DialogCallback
import com.blackbox.apps.karay.models.rxbus.AppEvents
import com.blackbox.apps.karay.models.rxbus.EventData
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.ui.fragments.add.AddNewFragment
import com.blackbox.apps.karay.utils.GlideApp
import com.blackbox.apps.karay.utils.helpers.DialogsHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.michaelflisar.rxbus2.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.detail_layout.*
import java.io.File
import java.util.concurrent.TimeUnit


class DetailFragment : BaseFragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var clothingItem: WomenClothing

    companion object {

        private const val ARG_ITEM = "womenClothing"
        var LABEL = ""
        var mPaletteColor = 0

        fun bundleArgs(item: WomenClothing?): Bundle {
            return Bundle().apply {
                this.putParcelable(ARG_ITEM, item)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.detail_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)

        //Get parcelable movie object here
        clothingItem = (arguments?.getParcelable(ARG_ITEM) as? WomenClothing)!!
        LABEL = clothingItem.brand_name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContent()
    }

    private fun setContent() {

        //Set content in Collapsing Toolbar
        toolbar_layout?.title = clothingItem.brand_name
        toolbar_layout?.setCollapsedTitleTextAppearance(R.style.collapsedAppBar)
        toolbar_layout?.setExpandedTitleTextAppearance(R.style.expandedAppBar)
        toolbar_layout?.setContentScrimColor(ContextCompat.getColor(activity!!, R.color.colorPrimaryDark))
        toolbar_layout.setExpandedTitleColor(ContextCompat.getColor(activity!!, android.R.color.transparent))
        toolbar_layout.setStatusBarScrimColor(ContextCompat.getColor(activity!!, android.R.color.transparent))

        //If is URL
        if (clothingItem.image.contains("https:")) {

            FireBaseHelper.getWomenClothingImageById(clothingItem.id)
                    .doOnSubscribe {
                        showLoading()
                    }
                    .subscribeBy(
                            onNext = { ref ->

                                GlideApp.with(activity!!)
                                        .asBitmap()
                                        .load(ref)
                                        .listener(object : RequestListener<Bitmap> {
                                            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                                if (resource != null) {
                                                    val p = Palette.from(resource).generate()
                                                    // Use generated instance
                                                    mPaletteColor = p.getMutedColor(ContextCompat.getColor(activity!!, R.color.colorAccent))
                                                    RxBus.get().send(EventData(AppEvents.STYLE_ACTION_BAR))
                                                }
                                                hideLoading()
                                                return false
                                            }

                                            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                                hideLoading()
                                                return false
                                            }
                                        })
                                        .into(img_header)
                            },
                            onError = {
                                hideLoading()
                                it.printStackTrace()
                            }
                    )
        } else {

            Glide.with(activity!!)
                    .asBitmap()
                    .load(File(clothingItem.image))
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            if (resource != null) {
                                val p = Palette.from(resource).generate()
                                // Use generated instance
                                mPaletteColor = p.getMutedColor(ContextCompat.getColor(activity!!, R.color.colorAccent))
                                RxBus.get().send(EventData(AppEvents.STYLE_ACTION_BAR))
                            }
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    })
                    .into(img_header)
        }

        //Set content in details section
        txt_title?.text = clothingItem.brand_name

        btn_back.setOnClickListener {
            goBack()
        }

        fab_delete.setOnClickListener {

            DialogsHelper.showConfirmDeleteDialog(activity!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .debounce(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = { callback ->
                                if (callback == DialogCallback.POSITIVE) {
                                    if (clothingItem.id.isNotEmpty()) {
                                        viewModel.deletePost(clothingItem.id)
                                        goBack()
                                    }
                                }
                            }
                    )
        }

        fab_edit.setOnClickListener {
            val args = AddNewFragment.bundleArgs(clothingItem)
            Navigation.findNavController(view!!).navigate(R.id.action_detailFragment_to_AddNewFragment, args)
        }

        showHideViews()
    }

    private fun showHideViews(){
        txt_description?.text = "" +
                "Season: ${clothingItem.season_info}" +
                "\nSize: ${clothingItem.size_info}"

        if(clothingItem.date_purchased.isNotEmpty()){
            txt_description?.append("\nPurchased On: ${clothingItem.date_purchased}")
        }

        if(clothingItem.price.isNotEmpty()){
            txt_description?.append("\nPrice: ${clothingItem.price}")
        }

        txt_description?.append("\nAdded On: ${clothingItem.date_added}")
    }
}
