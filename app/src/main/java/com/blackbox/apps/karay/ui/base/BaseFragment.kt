package com.blackbox.apps.karay.ui.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.blackbox.apps.karay.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.michaelflisar.rxbus2.interfaces.IRxBusQueue
import com.michaelflisar.rxbus2.rx.RxDisposableManager
import dagger.android.support.AndroidSupportInjection
import io.reactivex.processors.BehaviorProcessor
import kotlinx.android.synthetic.main.layout_progress.*
import org.reactivestreams.Publisher
import javax.inject.Inject


abstract class BaseFragment : Fragment(), IRxBusQueue {

    private val mResumedProcessor = BehaviorProcessor.createDefault(false)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    // --------------
    // Commons
    // --------------

    fun showLoading() {
        progressDialog?.visibility = View.VISIBLE
        progressDialog?.isIndeterminate = true
    }

    fun hideLoading() {

        //Hide Progress Layout
        progressDialog?.animate()
                ?.translationY(progressDialog.height.toFloat())
                ?.alpha(0.0f)
                ?.setDuration(resources.getInteger(R.integer.anim_duration_long).toLong())
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        progressDialog?.visibility = View.GONE
                    }
                })
    }

    fun hideViewWithDelay(view: View?) {
        //Hide Layout
        view?.animate()
                ?.translationY(view.height.toFloat())
                ?.alpha(0.0f)
                ?.setDuration(resources.getInteger(R.integer.anim_duration_long).toLong())
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view?.visibility = View.GONE
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        mResumedProcessor.onNext(true)
    }

    override fun onPause() {
        mResumedProcessor.onNext(false)
        super.onPause()
    }

    override fun onDestroy() {
        RxDisposableManager.unsubscribe(this)
        super.onDestroy()
    }

    // --------------
    // Interface RxBus
    // --------------

    override fun isBusResumed(): Boolean {
        return mResumedProcessor.value!!
    }

    override fun getResumeObservable(): Publisher<Boolean> {
        return mResumedProcessor
    }

    fun goBack() {
        view?.let {
            Navigation.findNavController(it).navigateUp()
        }
    }

    fun hideFloatingActionButton(fab: FloatingActionButton?) {
        val params = fab?.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as FloatingActionButton.Behavior?

        if (behavior != null) {
            behavior.isAutoHideEnabled = false
        }

        fab.hide()
    }

    fun showFloatingActionButton(fab: FloatingActionButton?) {
        fab?.show()
        val params = fab?.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as FloatingActionButton.Behavior?

        if (behavior != null) {
            behavior.isAutoHideEnabled = true
        }
    }
}
