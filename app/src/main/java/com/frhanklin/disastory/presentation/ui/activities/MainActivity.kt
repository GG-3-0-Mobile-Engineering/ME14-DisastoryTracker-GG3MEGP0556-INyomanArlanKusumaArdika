package com.frhanklin.disastory.presentation.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.frhanklin.disastory.presentation.ui.viewmodel.MainViewModel
import com.frhanklin.disastory.R
import com.frhanklin.disastory.data.local.entity.DisasterModel
import com.frhanklin.disastory.data.remote.response.DisasterItems
import com.frhanklin.disastory.databinding.ActivityMainBinding
import com.frhanklin.disastory.databinding.BottomDisasterListBinding
import com.frhanklin.disastory.presentation.ui.adapters.DisasterAdapter
import com.frhanklin.disastory.utils.BitmapUtils
import com.frhanklin.disastory.utils.DisasterUtils
import com.frhanklin.disastory.utils.NotificationWorker
import com.frhanklin.disastory.utils.Resource
import com.frhanklin.disastory.utils.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.from
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DisasterAdapter.OnItemClickCallback {

    private lateinit var mapFragment: SupportMapFragment

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var bottomSheetBinding: BottomDisasterListBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var disasterAdapter: DisasterAdapter

    private lateinit var workManager: WorkManager

    @Inject
    internal lateinit var disasterUtils: DisasterUtils
    private lateinit var disasterObserver: Observer<Resource<PagedList<DisasterModel>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        bottomSheetBinding = mainBinding.bottomSheet
        bottomSheetBehavior = from(bottomSheetBinding.bottomSheetLayout)

        initiateObjects()
        setObservable()
        setViews()
    }

    private fun setObservable() {
        viewModel.getThemeSettings().observe(this) { nightState ->
            if (nightState) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                mapFragment.getMapAsync {
                    val darkStyle = MapStyleOptions.loadRawResourceStyle(this, R.raw.dark_mode_map_styles)
                    it.setMapStyle(darkStyle)
                }
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

        disasterObserver = Observer<Resource<PagedList<DisasterModel>>> {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        showLoading(true)
                    }
                    Status.SUCCESS -> {
                        showLoading(false)
                        disasterAdapter.submitList(it.data)
                        disasterAdapter.setOnItemClickCallback(this)
                        disasterAdapter.notifyDataSetChanged()
                        if (it.data.isNullOrEmpty()) {
                            viewModel.setWarn(true, applicationContext.getString(R.string.warning_no_data))
                        } else {
                            viewModel.setWarn(false, "")
                        }

                        bottomSheetBinding.rvDisasterList.adapter = disasterAdapter
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        viewModel.setWarn(true, applicationContext.getString(R.string.warning_exception))
                    }
                }
            }
        }
        viewModel.getDisaster().observe(this, disasterObserver)

        viewModel.filter.observe(this) {
            viewModel.getDisaster().observe(this, disasterObserver)
        }
        viewModel.filterArray.observe(this) {
            updateDisasterFilterButtons(it)
        }
        viewModel.cityId.observe(this) {
            viewModel.getDisaster().observe(this, disasterObserver)
        }
        viewModel.isWarned.observe(this) {isWarned ->
            bottomSheetBinding.warningLayout.visibility = if (isWarned) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        viewModel.warningText.observe(this) { newWarning ->
            bottomSheetBinding.tvWarning.text = newWarning
            bottomSheetBinding.imgWarning.setImageDrawable(disasterUtils.getWarningImage(newWarning))
        }
    }

    private fun updateDisasterFilterButtons(it: ArrayList<String>) {
        for (item in it) {
            val view = when (item)  {
                "flood" -> mainBinding.btnSearchFlood
                "haze" -> mainBinding.btnSearchHaze
                "wind" -> mainBinding.btnSearchWind
                "earthquake" -> mainBinding.btnSearchEarthquake
                "volcano" -> mainBinding.btnSearchVolcano
                "fire" -> mainBinding.btnSearchFire
                else -> mainBinding.btnSearchFlood
            }
            view.isSelected = true
            view.background = getDrawable(R.drawable.round_fill_green)
//            if (!isAlreadySelected) {
//                view.isSelected = true
//
//            } else {
//                view.isSelected = false
//                view.background = getDrawable(R.drawable.round_fill)
//            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            mainBinding.loading.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                mainBinding.loading.visibility = View.GONE
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }, 1000)
        }
    }

    private fun initiateObjects() {
        bottomSheetBinding.rvDisasterList.layoutManager = LinearLayoutManager(this)
        mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        disasterAdapter = DisasterAdapter(disasterUtils)

    }

    private fun setUpPeriodicNotification() {

        val periodicNotificationRequest = PeriodicWorkRequest.Builder(
            NotificationWorker::class.java,
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).build()

        workManager.enqueue(periodicNotificationRequest)
    }

    @SuppressLint("SetTextI18n")
    private fun setViews() {

        bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                bottomSheetBinding.btnBack.visibility = when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        View.INVISIBLE
                    }
                    else -> {
                        View.INVISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })
        bottomSheetBinding.btnBack.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        mainBinding.btnSearchFlood.text = "+ " + getString(R.string.type_flood)
        mainBinding.btnSearchHaze.text = "+ " + getString(R.string.type_haze)
        mainBinding.btnSearchWind.text = "+ " + getString(R.string.type_wind)
        mainBinding.btnSearchEarthquake.text = "+ " + getString(R.string.type_earthquake)
        mainBinding.btnSearchVolcano.text = "+ " + getString(R.string.type_volcano)
        mainBinding.btnSearchFire.text = "+ " + getString(R.string.type_fire)

        mainBinding.btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        mainBinding.btnSearchFlood.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.addFilter("flood")
            } else {
                viewModel.removeFilter("flood")
            }
        }
        mainBinding.btnSearchHaze.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.addFilter("haze")
            } else {
                viewModel.removeFilter("haze")
            }
        }
        mainBinding.btnSearchWind.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.addFilter("wind")
            } else {
                viewModel.removeFilter("wind")
            }
        }
        mainBinding.btnSearchEarthquake.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.addFilter("earthquake")
            } else {
                viewModel.removeFilter("earthquake")
            }
        }
        mainBinding.btnSearchVolcano.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.addFilter("volcano")
            } else {
                viewModel.removeFilter("volcano")
            }
        }
        mainBinding.btnSearchFire.setOnClickListener{
            val filterOn = onCustomRadioButtonClick(it)
            if (filterOn) {
                viewModel.addFilter("fire")
            } else {
                viewModel.removeFilter("fire")
            }
        }

        val cityNamesArray = resources.getStringArray(R.array.city_names)
        val searchAdapter = ArrayAdapter(this, R.layout.item_row_city, cityNamesArray)
        mainBinding.listSearchSuggestion.adapter = searchAdapter
        mainBinding.searchMain.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the search submission
                viewModel.setLocation(query ?: "")
                mainBinding.searchMain.clearFocus()
                viewModel.getDisaster().observe(this@MainActivity, disasterObserver)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewModel.setLocation("")
                    viewModel.getDisaster().observe(this@MainActivity, disasterObserver)
                }
                searchAdapter.filter.filter(newText)
                return true
            }

        })
        mainBinding.searchMain.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mainBinding.listSearchSuggestion.visibility = View.VISIBLE
                mainBinding.btnSettings.visibility = View.GONE
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetBinding.root.visibility = View.GONE
            } else {
                mainBinding.listSearchSuggestion.visibility = View.GONE
                mainBinding.btnSettings.visibility = View.VISIBLE
                bottomSheetBinding.root.visibility = View.VISIBLE
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
        mainBinding.listSearchSuggestion.visibility = View.GONE

        mainBinding.listSearchSuggestion.setOnItemClickListener{ _, _, position, _ ->
            val selectedItem = searchAdapter.getItem(position) as String
            mainBinding.searchMain.setQuery(selectedItem, false)
            viewModel.setLocation(selectedItem)
            mainBinding.searchMain.clearFocus()
        }

        bottomSheetBinding.rvDisasterList.addOnItemTouchListener(object: RecyclerView.OnItemTouchListener {
            private val gestureDetector = GestureDetector(bottomSheetBinding.rvDisasterList.context, object: GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
            })

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val childView = rv.findChildViewUnder(e.x, e.y)
                if (childView != null && gestureDetector.onTouchEvent(e)) {
                    childView.callOnClick()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    return true
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                if (gestureDetector.onTouchEvent(e)) {
                    bottomSheetBinding.btnBack.visibility = View.INVISIBLE
                }
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {  }

        })

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onCustomRadioButtonClick(view: View): Boolean {
        val isAlreadySelected = view.isSelected

        if (!isAlreadySelected) {
            view.isSelected = true
            view.background = getDrawable(R.drawable.round_fill_green)
        } else {
            view.isSelected = false
            view.background = getDrawable(R.drawable.round_fill)
        }

        return view.isSelected
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

    override fun onItemClicked(latitude: Double, longitude: Double) {
        mapFragment.getMapAsync {
            it.animateCamera(CameraUpdateFactory.zoomOut())
            it.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(latitude, longitude), 10f)
            )
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }
    }




}