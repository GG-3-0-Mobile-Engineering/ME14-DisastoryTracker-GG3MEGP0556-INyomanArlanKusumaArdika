package com.frhanklin.disastory

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.frhanklin.disastory.data.response.DisasterItems
import com.frhanklin.disastory.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.selects.select

class MainActivity : AppCompatActivity() {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var mBottomSheetLayout: LinearLayout
    private lateinit var mSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var rvDisasterList: RecyclerView

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = SettingPreferences.getInstance(application.dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(preferences)).get(
            MainViewModel::class.java
        )
        viewModel.getThemeSettings().observe(this) { nightState ->
            if (nightState) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            viewModel.saveThemeSettings(nightState)
        }


        mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
        })

        rvDisasterList = findViewById(R.id.rv_disaster_list)
        rvDisasterList.layoutManager = LinearLayoutManager(this)
        viewModel.disasterItemsArray.observe(this) {
            setDisasterData(it)
        }
        viewModel.isLoading.observe(this) {
            setLoadingScreen(it)
        }
        viewModel.isWarned.observe(this) {

        }
        viewModel.warningText.observe(this) {

        }
        viewModel.getRecentDisaster()
        setViews()

        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout)
        mSheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout)

    }

    private fun setLoadingScreen(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setViews() {

        binding.btnSearchFlood.text = "+ " + getString(R.string.type_flood)
        binding.btnSearchHaze.text = "+ " + getString(R.string.type_haze)
        binding.btnSearchWind.text = "+ " + getString(R.string.type_wind)
        binding.btnSearchEarthquake.text = "+ " + getString(R.string.type_earthquake)
        binding.btnSearchVolcano.text = "+ " + getString(R.string.type_volcano)
        binding.btnSearchFire.text = "+ " + getString(R.string.type_fire)

        binding.btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        binding.btnSearchFlood.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.setFilter("flood")
            } else {
                viewModel.setFilter("")
            }
        }
        binding.btnSearchHaze.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.setFilter("haze")
            } else {
                viewModel.setFilter("")
            }
        }
        binding.btnSearchWind.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.setFilter("wind")
            } else {
                viewModel.setFilter("")
            }
        }
        binding.btnSearchEarthquake.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.setFilter("earthquake")
            } else {
                viewModel.setFilter("")
            }
        }
        binding.btnSearchVolcano.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.setFilter("volcano")
            } else {
                viewModel.setFilter("")
            }
        }
        binding.btnSearchFire.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.setFilter("fire")
            } else {
                viewModel.setFilter("")
            }
        }

        val cityNamesArray = resources.getStringArray(R.array.city_names)
        val searchAdapter = ArrayAdapter(this, R.layout.item_row_city, cityNamesArray)
        binding.listSearchSuggestion.adapter = searchAdapter
        binding.searchMain.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the search submission
                viewModel.setLocation(applicationContext, query ?: "")
                binding.searchMain.clearFocus()
                viewModel.getRecentDisaster()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewModel.setLocation(applicationContext, "")
                    viewModel.getRecentDisaster()
                }
                searchAdapter.filter.filter(newText)
                return true
            }

        })
        binding.searchMain.setOnQueryTextFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.listSearchSuggestion.visibility = View.VISIBLE

            } else {
                binding.listSearchSuggestion.visibility = View.GONE

            }
        }
        binding.listSearchSuggestion.visibility = View.GONE

        binding.listSearchSuggestion.setOnItemClickListener{ _, _, position, _ ->
            val selectedItem = searchAdapter.getItem(position) as String
            binding.searchMain.setQuery(selectedItem, false)
            viewModel.setLocation(applicationContext, selectedItem ?: "")
            binding.searchMain.clearFocus()
            viewModel.getRecentDisaster()
        }

        rvDisasterList.addOnItemTouchListener(object: RecyclerView.OnItemTouchListener {
            private val gestureDetector = GestureDetector(rvDisasterList.context, object: GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
            })

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val childView = rv.findChildViewUnder(e.x, e.y)
                if (childView != null && gestureDetector.onTouchEvent(e)) {
                    childView.callOnClick()

                    mSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED


                    return true
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {  }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {  }

        })

    }

    private fun onCustomRadioButtonClick(view: View): Boolean {
        val isAlreadySelected = view.isSelected

        for (i in 0 until binding.btnFilters.childCount) {
            val child = binding.btnFilters.getChildAt(i)
            child.background = getDrawable(R.drawable.round_fill)
            child.isSelected = false
        }

        if (!isAlreadySelected) {
            view.isSelected = true
            view.background = getDrawable(R.drawable.round_fill_green)
        } else {
            view.isSelected = false
        }

        return view.isSelected
    }


    private fun setDisasterData(listDisasterItems: List<DisasterItems>) {
        val list = ArrayList<DisasterItems>()
        for (disaster in listDisasterItems) {
            list.add(
                DisasterItems(
                    disaster.coordinates,
                    disaster.type,
                    disaster.disasterProperties
                )
            )
        }
        rvDisasterList.adapter = ListDisasterAdapter(list, mapFragment, this)
        setMapPoints(list)
    }

    private fun setMapPoints(list: ArrayList<DisasterItems>) {
        var lastLoc = LatLng(0.0, 0.0)
        for (item in list) {
            val latitude = String.format("%.2f", item.coordinates?.get(0)?:0.0).toDouble()
            val longitude = String.format("%.2f", item.coordinates?.get(1)?:0.0).toDouble()
            val location = LatLng(latitude, longitude)
            val vectorDrawable = VectorDrawableCompat.create(
                resources,
                when (item.type) {
                "flood" -> R.drawable.ic_location_flood
                "haze" -> R.drawable.ic_location_haze
                "fire" -> R.drawable.ic_location_fire
                "wind" -> R.drawable.ic_location_wind
                "earthquake" -> R.drawable.ic_location_earthquake
                "volcano" -> R.drawable.ic_location_volcano
                else -> R.drawable.ic_not_listed_location_24
            },null)

            val bitmap = vectorToBitmap(vectorDrawable)
            println("Location value: $location")

            mapFragment.getMapAsync {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(item.type ?: "Unknown")
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                )
            }
            lastLoc = location
        }
        mapFragment.getMapAsync {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, 5f))
        }

    }
    private fun vectorToBitmap(vectorDrawable: VectorDrawableCompat?): Bitmap {
        if (vectorDrawable == null) {
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }

        // Create the Bitmap and the Canvas to draw on it
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        // Draw the Vector XML onto the canvas
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        return bitmap
    }

}