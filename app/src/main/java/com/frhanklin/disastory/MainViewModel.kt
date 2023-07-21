package com.frhanklin.disastory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frhanklin.disastory.api.ApiConfig
import com.frhanklin.disastory.data.response.DisasterItems
import com.frhanklin.disastory.data.response.PetaBencanaReports
import com.mapbox.geojson.Point
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    companion object {
        private val TAG = "MainViewModel"
    }

    private  lateinit var center: Point

    private val _disasterItemsArray = MutableLiveData<ArrayList<DisasterItems>>()
    val disasterItemsArray: LiveData<ArrayList<DisasterItems>> = _disasterItemsArray

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isWarned = MutableLiveData<Boolean>()
    val isWarned: LiveData<Boolean> = _isWarned

    private val _warningText = MutableLiveData<String>()
    val warningText: LiveData<String> = _warningText

    private fun setLoading(state: Boolean) {
        _isLoading.value = state
    }

    private fun setWarn(state: Boolean, msg: String) {
        _isWarned.value = state
        _warningText.value = msg
    }

    fun setCenter(center: Point) {
        this.center = center
    }

    fun getRecentDisaster() {
        setLoading(true)
        val client = ApiConfig.getApiService().getReports()
        client.enqueue(object : Callback<PetaBencanaReports> {
            override fun onResponse(
                call: Call<PetaBencanaReports>,
                response: Response<PetaBencanaReports>
            ) {
                setLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val arrayNotEmpty = responseBody?.result?.objects?.output?.geometries?.isNotEmpty() as Boolean
                    if (arrayNotEmpty) {
                        setWarn(false, "")
                        val arrayList = ArrayList<DisasterItems>()
                        for (items in responseBody.result.objects.output.geometries) {
                            arrayList.add(items!!)
                        }
                        _disasterItemsArray.value = arrayList
                    }
                } else {
                    setWarn(true, "Data tidak ditemukan")
                }
            }

            override fun onFailure(call: Call<PetaBencanaReports>, t: Throwable) {
                setLoading(false)
                setWarn(true, "Error fetching data")
                Log.e(TAG, "onFailure: ${t.message}", )
            }

        })

    }
}