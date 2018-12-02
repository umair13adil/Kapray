package com.blackbox.apps.karay.ui.fragments.pages

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.karay.data.repositories.main.MainRepository
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.models.enums.AdapterActions
import com.blackbox.apps.karay.ui.items.SeasonsHeaderItem
import com.blackbox.apps.karay.ui.items.WomenClothingItem
import com.blackbox.apps.karay.ui.items.WomenLocalBrandItem
import com.blackbox.apps.karay.utils.helpers.ListHelper
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class PagesViewModel @Inject constructor(private var app: Application, private var mainRepository: MainRepository) : ViewModel() {


    private var listItemsCount = 0
    private var adapter: FlexibleAdapter<*>? = null


    fun getListOfWomenClothing(fetchByType: Boolean = false, inCloset: Boolean = false): Observable<ArrayList<IFlexible<*>>> {

        return Observable.create { emitter ->

            val mItems = arrayListOf<IFlexible<*>>()
            var lastHeader = ""

            val listObservable: Observable<List<WomenClothing>> = if (fetchByType) {
                mainRepository.getListOfWomenClothing(inCloset)
            } else {
                mainRepository.getListOfWomenClothing()
            }

            listObservable.subscribeBy(
                    onNext = { list ->

                        //Set size of list
                        listItemsCount = list.size

                        list.sortedBy {
                            it.season_info
                        }.forEach { item ->
                            var headerItem: SeasonsHeaderItem? = null

                            if (lastHeader.isEmpty() || lastHeader != item.season_info) {
                                headerItem = SeasonsHeaderItem(item.season_info)
                            }

                            val item1 = WomenClothingItem(item, headerItem)

                            //Add Item to List
                            mItems.add(item1)

                            lastHeader = item.season_info
                        }

                        if (!emitter.isDisposed) {
                            emitter.onNext(mItems)
                            emitter.onComplete()
                        }
                    },
                    onError = {
                        it.printStackTrace()

                        if (!emitter.isDisposed) {
                            emitter.onError(it)
                            emitter.onComplete()
                        }
                    }
            )
        }
    }

    fun getListOfWomenClothingBrands(): ArrayList<IFlexible<*>> {
        val list = mainRepository.getListOfWomenLocalBrands()

        val mItems = arrayListOf<IFlexible<*>>()

        list.forEach {
            val item1 = WomenLocalBrandItem(it)

            //Add Item to List
            mItems.add(item1)
        }

        return mItems
    }

    fun setUpListAdapter(mItems: ArrayList<IFlexible<*>>, recycler_view: RecyclerView, context: Context, adapterActions: AdapterActions? = null) {
        adapter = ListHelper.setUpAdapter(mItems, recycler_view, context)
        adapter?.notifyItemRangeInserted(0, mItems.size)
        ListHelper.setAdapterListener(adapterActions)
    }

    fun getSizeOfList(): Int {
        return listItemsCount
    }

    fun filter(constraint: String) {
        ListHelper.filter(constraint)
    }

    fun clearFilter() {
        ListHelper.clearFilters()
    }

    fun getWomenClothingItem(position: Int): WomenClothing? {
        if (ListHelper.getListItem<WomenClothingItem>(position) is WomenClothingItem) {
            return ListHelper.getListItem<WomenClothingItem>(position)?.womenClothing
        }
        return null
    }
}