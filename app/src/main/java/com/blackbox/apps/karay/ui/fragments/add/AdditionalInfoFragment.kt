package com.blackbox.apps.karay.ui.fragments.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.brands.WomenLocalBrand
import com.blackbox.apps.karay.models.clothing.WomenClothing
import com.blackbox.apps.karay.models.enums.Seasons
import com.blackbox.apps.karay.models.rxbus.UserInput
import com.blackbox.apps.karay.ui.base.BaseFragment
import com.blackbox.apps.karay.utils.autoComplete.CustomAutoCompleteTextChangedListener
import com.blackbox.apps.karay.utils.autoComplete.WomenBrandsLocalAdapter
import com.blackbox.apps.karay.utils.commons.DateTimeUtils
import com.blackbox.apps.karay.utils.setTypeface
import com.michaelflisar.rxbus2.RxBusBuilder
import com.michaelflisar.rxbus2.rx.RxBusMode
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_additional_info.*
import java.util.*


class AdditionalInfoFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {

    private val TAG = "AdditionalInfoFragment"

    private lateinit var viewModel: AddNewViewModel

    lateinit var myAdapter: ArrayAdapter<WomenLocalBrand>
    lateinit var listOfWomenLocalBrands: List<WomenLocalBrand>
    private val womenClothing = WomenClothing()

    companion object {
        private const val ARG_IMAGE_PATH = "image_path"

        fun bundleArgs(string: String): Bundle {
            return Bundle().apply {
                this.putString(ARG_IMAGE_PATH, string)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_additional_info, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddNewViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val image_path = it.getString(ARG_IMAGE_PATH)
            womenClothing.image = image_path

            //Set Temp Id, it will be updated once post is stored in FireBase
            womenClothing.id = womenClothing.image.hashCode().toString()
        }

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
        txt_h2.typeface = setTypeface(activity!!)
        btn_save.typeface = setTypeface(activity!!)
        input_date_purchased.typeface = setTypeface(activity!!)
        input_kept_away.typeface = setTypeface(activity!!)
        input_unstitched.typeface = setTypeface(activity!!)

        setupAutoSuggestionsAdapter()

        setUpSeasonsSpinner()

        input_date_purchased.setOnClickListener {
            showDatePicker()
        }

        input_kept_away.setOnCheckedChangeListener { compoundButton, b ->
            womenClothing.kept_away = b
        }

        input_unstitched.setOnCheckedChangeListener { compoundButton, b ->
            womenClothing.un_stitched = b
        }

        btn_skip.setOnClickListener {
            viewModel.saveWomenClothingInfo(womenClothing)
            Navigation.findNavController(view!!).navigate(R.id.action_AdditionalInfoFragment_to_MainFragment)
        }

        btn_save.setOnClickListener {
            viewModel.saveWomenClothingInfo(womenClothing)
            Navigation.findNavController(view!!).navigate(R.id.action_AdditionalInfoFragment_to_MainFragment)
        }
    }

    private fun setUpSeasonsSpinner() {
        val list_of_items = Seasons.values()

        val aa = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        input_season?.adapter = aa
        input_season?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                womenClothing.season_info = list_of_items[p2].value
            }
        }
    }

    private fun setupAutoSuggestionsAdapter() {

        input_women_brand_name.setOnItemClickListener { parent, arg1, pos, id ->
            val rl = arg1 as LinearLayout
            val tv = rl.getChildAt(0) as LinearLayout
            val textView = tv.getChildAt(1) as AppCompatTextView
            val brandName = textView.text.toString()
            input_women_brand_name.setText(brandName)
            womenClothing.brand_name = brandName

            val url = viewModel.getLogoURL(brandName)
            url?.let {
                womenClothing.brand_logo_url = it
                Log.i(TAG, "Logo URL: $it")
            }
        }

        // add the listener so it will tries to suggest while the user types
        input_women_brand_name.addTextChangedListener(CustomAutoCompleteTextChangedListener())

        //Setup adapter
        setupAdapter(arrayListOf())
    }

    private fun refreshListSuggestions(input: String) {
        try {
            Log.i(TAG, "Input: ${input}")

            Observable.fromIterable(listOfWomenLocalBrands)
                    .filter {
                        it.brand.startsWith(input, ignoreCase = true)
                    }
                    .toList()
                    .map {

                        setupAdapter(it)

                        Log.i(TAG, "New\n Size: ${it.size}")

                        it.forEach {
                            Log.i(TAG, "Filtered: ${it.brand}\n")
                        }

                        myAdapter.notifyDataSetChanged()
                    }
                    .subscribe()

        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupAdapter(listOfBrands: List<WomenLocalBrand>) {

        //set up custom ArrayAdapter
        myAdapter = WomenBrandsLocalAdapter(activity!!, R.layout.item_women_brand_local_suggest, listOfBrands)
        input_women_brand_name.setAdapter(myAdapter)
    }

    private fun showDatePicker() {
        val now = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        )
        dpd.show(fragmentManager, "Datepickerdialog")
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = DateTimeUtils.getFullDateString(year, monthOfYear + 1, dayOfMonth)
        input_date_purchased.setText(date)
        womenClothing.date_purchased = date
    }
}