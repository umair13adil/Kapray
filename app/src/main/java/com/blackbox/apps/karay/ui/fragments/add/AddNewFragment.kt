package com.blackbox.apps.karay.ui.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.images.ImageCaptured
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.utils.helpers.ImageCaptureHelper
import com.blackbox.apps.karay.utils.setTypeface
import com.michaelflisar.rxbus2.RxBusBuilder
import com.michaelflisar.rxbus2.rx.RxBusMode
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_new.*
import java.io.File


class AddNewFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_add_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAddPhotoContentLayout()

        RxBusBuilder.create(ImageCaptured::class.java)
                .withBound(this)
                .withMode(RxBusMode.Main)
                .subscribe { image ->
                    Picasso.with(activity).load(File(image.filePath)).into(img_preview)
                }
    }

    private fun setAddPhotoContentLayout() {

        //Set Fonts
        txt_h1.typeface = setTypeface(activity!!)
        btn_add_photo.typeface = setTypeface(activity!!)

        btn_add_photo.setOnClickListener {
            ImageCaptureHelper.takePicture(activity!!, CAPTURE_PHOTO)
        }
    }

    companion object {
        val CAPTURE_PHOTO = 3276
    }
}