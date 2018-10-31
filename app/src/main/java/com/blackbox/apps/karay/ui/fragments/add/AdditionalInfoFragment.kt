package com.blackbox.apps.karay.ui.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProviders
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.blackbox.apps.karay.models.rxbus.UserInput
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.utils.autoComplete.CustomAutoCompleteTextChangedListener
import com.blackbox.apps.karay.utils.autoComplete.WomenBrandsLocalAdapter
import com.blackbox.apps.karay.utils.setTypeface
import com.michaelflisar.rxbus2.RxBusBuilder
import com.michaelflisar.rxbus2.rx.RxBusMode
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_additional_info.*
import java.util.concurrent.TimeUnit

class AdditionalInfoFragment : BaseFragment() {

    private val TAG = "AdditionalInfoFragment"

    private lateinit var viewModel: AddNewViewModel

    lateinit var myAdapter: ArrayAdapter<WomenLocalBrand>
    lateinit var listOfWomenLocalBrands: List<WomenLocalBrand>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_additional_info, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddNewViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContent()

        RxBusBuilder.create(UserInput::class.java)
                .withQueuing(this)
                .withMode(RxBusMode.Main)
                .subscribe { data ->
                    refreshListSuggestions(data.input)
                }
    }

    private fun setContent() {

        //Get List of brand names
        listOfWomenLocalBrands = viewModel.getListOfWomenLocalBrands()

        btn_skip.typeface = setTypeface(activity!!)
        txt_h1.typeface = setTypeface(activity!!)
        btn_save.typeface = setTypeface(activity!!)

        setupAutoSuggestionsAdapter()
    }

    private fun setupAutoSuggestionsAdapter() {

        input_women_brand_name.setOnItemClickListener { parent, arg1, pos, id ->
            val rl = arg1 as LinearLayout
            val tv = rl.getChildAt(1) as AppCompatTextView
            input_women_brand_name.setText(tv.text.toString())
        }

        // add the listener so it will tries to suggest while the user types
        input_women_brand_name.addTextChangedListener(CustomAutoCompleteTextChangedListener())

        //Setup adapter
        setupAdapter(arrayListOf())
    }

    private fun refreshListSuggestions(input: String) {
        try {
            Observable.just(input)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .doOnNext { value ->
                        myAdapter.notifyDataSetChanged()

                        val listFiltered = listOfWomenLocalBrands.filter {
                            it.brand.toLowerCase().contains(value)
                        }

                        setupAdapter(listFiltered)
                    }
                    .subscribe()

        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupAdapter(listOfBrands: List<WomenLocalBrand>) {
        val brandsList = arrayListOf<WomenLocalBrand>()
        brandsList.addAll(listOfBrands)

        //set up custom ArrayAdapter
        myAdapter = WomenBrandsLocalAdapter(activity!!, R.layout.item_women_brand_local, brandsList)
        input_women_brand_name.setAdapter(myAdapter)
    }
}