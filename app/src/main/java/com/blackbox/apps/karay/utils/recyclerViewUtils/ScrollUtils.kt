package com.blackbox.apps.karay.utils.recyclerViewUtils

import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.karay.utils.recyclerViewUtils.FlexiSmoothScroller

fun smoothScroll(recyclerView: RecyclerView, targetPos: Int) {
    val smoothScroller = FlexiSmoothScroller(recyclerView.context).setMillisecondsPerInchSearchingTarget(100f)
    smoothScroller.targetPosition = targetPos
    recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
}