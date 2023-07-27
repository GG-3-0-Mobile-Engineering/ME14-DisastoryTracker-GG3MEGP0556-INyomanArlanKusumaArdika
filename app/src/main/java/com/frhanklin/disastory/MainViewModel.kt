package com.frhanklin.disastory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.frhanklin.disastory.api.ApiConfig
import com.frhanklin.disastory.data.DisastoryDummyData
import com.frhanklin.disastory.data.response.DisasterItems
import com.frhanklin.disastory.data.response.PetaBencanaReports
import com.frhanklin.disastory.utils.DisasterUtils
import com.frhanklin.disastory.utils.ResourceProvider
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val preferences: SettingPreferences,
    private val rp: ResourceProvider
) : ViewModel() {

    private val disasterUtils = DisasterUtils(rp)


    fun getThemeSettings() : LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSettings(darkModeState: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(darkModeState)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }


    private val _disasterItemsArray = MutableLiveData<ArrayList<DisasterItems>>()
    val disasterItemsArray: LiveData<ArrayList<DisasterItems>> = _disasterItemsArray

    private val _filter = MutableLiveData<String>()
    val filter: LiveData<String> = _filter

    private val _cityId = MutableLiveData<String>()
    val cityId: LiveData<String> = _cityId

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


    fun getCityId(): String {
        return cityId.value ?: ""
    }

    fun getRecentDisaster() {
        setLoading(true)
        fetchFromRemote()
//        fetchFromDummy()
        setLoading(false)
    }

    private fun fetchFromDummy() {
        val reports = DisastoryDummyData.getDummyReports()
        _disasterItemsArray.value = (reports.result?.objects?.output?.geometries as ArrayList<DisasterItems>?)!!
    }

    private fun fetchFromRemote() {
        val client = if (getFilter().isNotEmpty() && getCityId().isNotEmpty()) {
            ApiConfig.getApiService().getReportsByLocationAndType(getCityId(), getFilter())
        } else if (getFilter().isNotEmpty() && getCityId().isEmpty())  {
            ApiConfig.getApiService().getReportsByType(getFilter())
        } else if (getFilter().isEmpty() && getCityId().isNotEmpty()) {
            ApiConfig.getApiService().getReportsByLocation(getCityId())
        } else {
            ApiConfig.getApiService().getReports()
        }

        client.enqueue(object : Callback<PetaBencanaReports> {
            override fun onResponse(
                call: Call<PetaBencanaReports>,
                response: Response<PetaBencanaReports>
            ) {
                if (response.isSuccessful) {
                    _disasterItemsArray.value = ArrayList()
                    val responseBody = response.body()
                    val arrayNotEmpty = responseBody?.result?.objects?.output?.geometries?.isNotEmpty() as Boolean
                    if (arrayNotEmpty) {
                        setWarn(false, "")
                        val arrayList = ArrayList<DisasterItems>()
                        for (items in responseBody.result.objects.output.geometries) {
                            arrayList.add(items!!)
                        }
                        _disasterItemsArray.value = arrayList
                    } else {
                        setWarn(true, rp.getString(R.string.warning_no_data))
                    }
                } else {
                    setWarn(true, rp.getString(R.string.warning_not_found))
                }
            }

            override fun onFailure(call: Call<PetaBencanaReports>, t: Throwable) {
                setWarn(true, rp.getString(R.string.warning_exception))
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun setFilter(type: String) {
        this._filter.value = type
        getRecentDisaster()
    }

    fun getFilter(): String {
        return filter.value ?: ""
    }


    fun setLocation(location: String) {
        _cityId.value = disasterUtils.getRegionCode(location)
    }
}