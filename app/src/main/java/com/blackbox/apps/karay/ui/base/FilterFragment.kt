package com.blackbox.apps.karay.ui.base

import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import com.blackbox.apps.karay.models.enums.OrderBy
import com.blackbox.apps.karay.models.enums.Seasons
import com.blackbox.apps.karay.models.enums.Sizes
import com.blackbox.apps.karay.utils.setUpSpinner
import com.michaelflisar.rxbus2.RxBus
import kotlinx.android.synthetic.main.fragment_my_wardrobe.*
import kotlinx.android.synthetic.main.layout_filter.*

abstract class FilterFragment : BaseFragment() {

    private var seasonSelected: Seasons = Seasons.ALL
    private var sizeSelected: Sizes = Sizes.NONE
    private var orderByAdded: OrderBy = OrderBy.LATEST
    private var orderByPurchased: OrderBy = OrderBy.LATEST

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFilterLayout()
    }

    private fun setUpFilterLayout() {

        setUpSpinner(Seasons.values(), input_season, activity!!)
                .doOnNext {
                    seasonSelected = it
                }.subscribe()

        setUpSpinner(Sizes.values(), input_size, activity!!)
                .doOnNext {
                    sizeSelected = it
                }.subscribe()

        setUpSpinner(OrderBy.values(), input_added, activity!!)
                .doOnNext {
                    orderByAdded = it
                }.subscribe()

        setUpSpinner(OrderBy.values(), input_purchased, activity!!)
                .doOnNext {
                    orderByPurchased = it
                }.subscribe()

        btn_cancel.setOnClickListener {
            sendFilter(false)
        }

        btn_apply.setOnClickListener {
            sendFilter(true)
        }
    }

    private fun sendFilter(applied:Boolean){
        drawer_layout.closeDrawer(GravityCompat.END)

        val filterData = FilterClothingData(
                season = seasonSelected,
                size = sizeSelected,
                addedOrder = orderByAdded,
                purchasedOrder = orderByPurchased,
                filterApplied = applied
        )

        RxBus.get().withSendToDefaultBus().send(filterData)
    }
}

data class FilterClothingData(var season: Seasons = Seasons.ALL,
                              var size: Sizes = Sizes.NONE,
                              var addedOrder: OrderBy = OrderBy.LATEST,
                              var purchasedOrder: OrderBy = OrderBy.LATEST,
                              var filterApplied: Boolean
)
