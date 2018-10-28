package com.blackbox.apps.karay.ui.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.images.ImageCaptured
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.utils.helpers.ImageCaptureHelper
import com.blackbox.apps.karay.utils.helpers.PermissionsHelper
import com.blackbox.apps.karay.utils.helpers.PermissionsHelper.requestCameraPermissions
import com.blackbox.apps.karay.utils.setTypeface
import com.michaelflisar.rxbus2.RxBusBuilder
import com.michaelflisar.rxbus2.rx.RxBusMode
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_add_new.*
import java.io.File
import java.util.concurrent.TimeUnit


class AddNewFragment : BaseFragment() {

    var storagePermissionsAllowed = false
    var cameraPermissionsAllowed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_add_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Check Storage Permissions
        checkStoragePermissions()
        checkCameraPermissions()

        setAddPhotoContentLayout()

        RxBusBuilder.create(ImageCaptured::class.java)
                .withBound(this)
                .withMode(RxBusMode.Main)
                .withQueuing(this)
                .subscribe { image ->
                    setPhotoCapturedLayout(image)
                }
    }

    private fun setAddPhotoContentLayout() {

        //Set Fonts
        txt_h1.typeface = setTypeface(activity!!)
        btn_add_photo.typeface = setTypeface(activity!!)
        btn_retake_photo.typeface = setTypeface(activity!!)
        btn_next.typeface = setTypeface(activity!!)

        btn_add_photo.setOnClickListener {
            openCamera()
        }

        btn_retake_photo.setOnClickListener {
            openCamera()
        }

        btn_next.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_AddNewFragment_to_AdditionalInfoFragment)
        }
    }

    private fun setPhotoCapturedLayout(image: ImageCaptured) {

        img_preview?.let {
            Picasso.with(activity).load(File(image.filePath)).into(it)
        }

        //Show/Hide Views
        btn_add_photo.visibility = View.GONE
        btn_retake_photo.visibility = View.VISIBLE
        btn_next.visibility = View.VISIBLE
        hideViewWithDelay(txt_h1)
    }

    private fun checkStoragePermissions() {
        //Request for storage permissions then start camera
        PermissionsHelper.requestStoragePermissions(activity!!)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { callback ->
                            if(callback){
                                storagePermissionsAllowed = true
                            }
                        }
                )
    }

    private fun checkCameraPermissions() {
        //Request for camera permissions and then open camera
        requestCameraPermissions(activity!!)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { callback ->
                            if(callback){
                                cameraPermissionsAllowed = true
                            }
                        }
                )
    }

    private fun openCamera(){

        if(!storagePermissionsAllowed){
            checkStoragePermissions()
            return
        }

        if(!cameraPermissionsAllowed){
            checkCameraPermissions()
            return
        }

        //Hide Buttons
        btn_add_photo.visibility = View.GONE
        btn_retake_photo.visibility = View.GONE

        //Start Camera
        ImageCaptureHelper.takePicture(activity!!, CAPTURE_PHOTO)
    }

    companion object {
        val CAPTURE_PHOTO = 3276
    }
}