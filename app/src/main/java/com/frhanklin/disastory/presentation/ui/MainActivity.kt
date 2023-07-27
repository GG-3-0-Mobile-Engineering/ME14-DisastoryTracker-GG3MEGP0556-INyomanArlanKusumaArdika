package com.frhanklin.disastory.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.frhanklin.disastory.presentation.adapter.ListDisasterAdapter
import com.frhanklin.disastory.presentation.viewmodel.MainViewModel
import com.frhanklin.disastory.R
import com.frhanklin.disastory.utils.SettingPreferences
import com.frhanklin.disastory.utils.ViewModelFactory
import com.frhanklin.disastory.data.response.DisasterItems
import com.frhanklin.disastory.databinding.ActivityMainBinding
import com.frhanklin.disastory.utils.AndroidResourceProvider
import com.frhanklin.disastory.utils.BitmapUtils
import com.frhanklin.disastory.utils.DisasterUtils
import com.frhanklin.disastory.utils.NotificationWorker
import com.frhanklin.disastory.utils.ResourceProvider
import com.frhanklin.disastory.utils.dataStore
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var mapFragment: SupportMapFragment

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var workManager: WorkManager

    private lateinit var mBottomSheetLayout: LinearLayout
    private lateinit var mSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var rvDisasterList: RecyclerView
    private lateinit var btnCloseBottomSheet: ImageView
    private lateinit var warningLayout: ConstraintLayout
    private lateinit var imgWarning: ImageView
    private lateinit var tvWarning: TextView

    private lateinit var pref: SettingPreferences
    private lateinit var rp: ResourceProvider
    private lateinit var disasterUtils: DisasterUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initiateObjects()
        setObservable()
        setViews()
    }

    private fun setObservable() {
        viewModel.getThemeSettings().observe(this) { nightState ->
            if (nightState) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            viewModel.saveThemeSettings(nightState)
        }
        viewModel.getNotificationSettings().observe(this) { notificationIsEnabled ->
            println("Notif state: "+notificationIsEnabled)
            if (notificationIsEnabled) {
                workManager = WorkManager.getInstance(this)
                setUpPeriodicNotification()
            }
        }

        viewModel.disasterItemsArray.observe(this) {
            mapFragment.getMapAsync {
                it.clear()
            }
            setDisasterData(it)
            mSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        viewModel.isLoading.observe(this) {isLoading ->
            if (isLoading) {
                binding.loading.visibility = View.VISIBLE
                mSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.loading.visibility = View.GONE
                    mSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                }, 1000)
            }
        }
        viewModel.isWarned.observe(this) {isWarned ->
            warningLayout.visibility = if (isWarned) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        viewModel.warningText.observe(this) { newWarning ->
            tvWarning.text = newWarning
            imgWarning.setImageDrawable(disasterUtils.getWarningImage(newWarning))
        }
        viewModel.getRecentDisaster()
    }

    private fun initiateObjects() {
        pref = SettingPreferences.getInstance(application.dataStore)
        rp = AndroidResourceProvider(applicationContext)
        disasterUtils = DisasterUtils(rp)

        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref, rp)).get(MainViewModel::class.java)

        mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment

        rvDisasterList = findViewById(R.id.rv_disaster_list)
        rvDisasterList.layoutManager = LinearLayoutManager(this)

        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout)
        btnCloseBottomSheet = findViewById(R.id.btn_back)
        warningLayout = findViewById(R.id.warning_layout)
        tvWarning = findViewById(R.id.tv_warning)
        imgWarning = findViewById(R.id.img_warning)

        mSheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout)

    }

    private fun setUpPeriodicNotification() {

        val periodicNotificationRequest = PeriodicWorkRequest.Builder(
            NotificationWorker::class.java,
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).build()

        workManager.enqueue(periodicNotificationRequest)
    }

    private fun setViews() {

        mSheetBehavior.addBottomSheetCallback(object: BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        btnCloseBottomSheet.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        btnCloseBottomSheet.visibility = View.INVISIBLE
                    }
                    else -> {
                        btnCloseBottomSheet.visibility = View.INVISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })
        btnCloseBottomSheet.setOnClickListener {
            mSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

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
                viewModel.setLocation(query ?: "")
                binding.searchMain.clearFocus()
                viewModel.getRecentDisaster()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewModel.setLocation("")
                    viewModel.getRecentDisaster()
                }
                searchAdapter.filter.filter(newText)
                return true
            }

        })
        binding.searchMain.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.listSearchSuggestion.visibility = View.VISIBLE
                binding.btnSettings.visibility = View.GONE
                mSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                mBottomSheetLayout.visibility = View.GONE
            } else {
                binding.listSearchSuggestion.visibility = View.GONE
                binding.btnSettings.visibility = View.VISIBLE
                mBottomSheetLayout.visibility = View.VISIBLE
                mSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
        binding.listSearchSuggestion.visibility = View.GONE

        binding.listSearchSuggestion.setOnItemClickListener{ _, _, position, _ ->
            val selectedItem = searchAdapter.getItem(position) as String
            binding.searchMain.setQuery(selectedItem, false)
            viewModel.setLocation(selectedItem)
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
                    mSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    return true
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                if (gestureDetector.onTouchEvent(e)) {
                    btnCloseBottomSheet.visibility = View.INVISIBLE
                }
            }

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
        rvDisasterList.adapter = ListDisasterAdapter(list, mapFragment, mSheetBehavior, rp)
        setMapPoints(list)
    }

    private fun setMapPoints(list: ArrayList<DisasterItems>) {
        var lastLoc = LatLng(-0.7893, 113.9213)
        for (item in list) {
            val latitude = String.format("%.2f", item.coordinates?.get(0)?:0.0).toDouble()
            val longitude = String.format("%.2f", item.coordinates?.get(1)?:0.0).toDouble()
            val location = LatLng(latitude, longitude)
            val vectorDrawable = VectorDrawableCompat.create(
                resources,
                disasterUtils.getMarkerIcon(item.disasterProperties?.disasterType),
                null
            )

            val markerTitle = disasterUtils.getDisasterType(item.disasterProperties?.disasterType)
            val markerSubtitle = disasterUtils.getRegionString(item.disasterProperties?.tags?.instanceRegionCode ?:"")




            val bitmap = BitmapUtils().vectorToBitmap(vectorDrawable)

            mapFragment.getMapAsync {googleMap ->
                googleMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(markerTitle)
                        .snippet(markerSubtitle)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                )
            }
//            lastLoc = location
        }
        mapFragment.getMapAsync {
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, 5f))
        }

    }


}