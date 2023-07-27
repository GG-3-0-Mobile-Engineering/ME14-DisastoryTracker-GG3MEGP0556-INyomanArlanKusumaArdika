package com.frhanklin.disastory

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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.frhanklin.disastory.data.response.DisasterItems
import com.frhanklin.disastory.databinding.ActivityMainBinding
import com.frhanklin.disastory.utils.AndroidResourceProvider
import com.frhanklin.disastory.utils.BitmapUtils
import com.frhanklin.disastory.utils.DisasterUtils
import com.frhanklin.disastory.utils.DisastoryWorker
import com.frhanklin.disastory.utils.ResourceProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

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





    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission rejected", Toast.LENGTH_SHORT).show()
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SettingPreferences.getInstance(application.dataStore)
        rp = AndroidResourceProvider(applicationContext)
        disasterUtils = DisasterUtils(rp)

//        if (Build.VERSION.SDK_INT >= 33) {
//            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//        }
//        workManager = WorkManager.getInstance(this)
//        setUpPeriodicNotification()



        viewModel = ViewModelProvider(this, ViewModelFactory(pref, rp)).get(
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

        setViews()

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
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.loading.visibility = View.GONE
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

    private fun setUpPeriodicNotification() {
        val repeatInterval = 1L
        val repeatIntervalTimeUnit = TimeUnit.HOURS

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            DisastoryWorker::class.java,
            repeatInterval,
            repeatIntervalTimeUnit
        ).build()

        workManager.enqueue(periodicWorkRequest)
    }

    private fun setViews() {

        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout)
        btnCloseBottomSheet = findViewById(R.id.btn_back)
        warningLayout = findViewById(R.id.warning_layout)
        tvWarning = findViewById(R.id.tv_warning)
        imgWarning = findViewById(R.id.img_warning)

        mSheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout)
        mSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
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
            } else {
                binding.listSearchSuggestion.visibility = View.GONE
                binding.btnSettings.visibility = View.VISIBLE

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
        rvDisasterList.adapter = ListDisasterAdapter(list, mapFragment, mSheetBehavior, this, rp)
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

            val bitmap = BitmapUtils().vectorToBitmap(vectorDrawable)
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


}