package com.frhanklin.disastory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.frhanklin.disastory.databinding.ActivityMainBinding
import com.mapbox.maps.Style
import com.mapbox.maps.extension.observable.eventdata.CameraChangedEventData
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener

class MainActivity : AppCompatActivity(), OnCameraChangeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapMain.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.setCenter(binding.mapMain.getMapboxMap().cameraState.center)

//        val listener = OnCameraChangeListener { eventData: CameraChangedEventData ->
//            viewModel.center = binding.mapMain.getMapboxMap().cameraState.center
//        }

    }

    override fun onCameraChanged(eventData: CameraChangedEventData) {
        viewModel.setCenter(binding.mapMain.getMapboxMap().cameraState.center)
    }
}