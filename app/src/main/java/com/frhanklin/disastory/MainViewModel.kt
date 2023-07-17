package com.frhanklin.disastory

import androidx.lifecycle.ViewModel
import com.mapbox.geojson.Point

class MainViewModel : ViewModel() {
    private  lateinit var center: Point

    fun setCenter(center: Point) {
        this.center = center
    }
}