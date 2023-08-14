package com.frhanklin.disastory.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.frhanklin.disastory.utils.SettingPreferences
import com.frhanklin.disastory.data.local.entity.DisasterModel
import com.frhanklin.disastory.domain.repository.AppRepository
import com.frhanklin.disastory.utils.DisasterUtils
import com.frhanklin.disastory.utils.Resource
import com.frhanklin.disastory.utils.ResourceProvider
import com.frhanklin.disastory.utils.SortHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: SettingPreferences,
    private val rp: ResourceProvider,
    private val repository: AppRepository
) : ViewModel() {

    fun getThemeSettings() : LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun getNotificationSettings(): LiveData<Boolean> {
        return preferences.getNotificationSetting().asLiveData()
    }

    fun saveThemeSettings(darkModeState: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(darkModeState)
        }
    }


    private val _filter = MutableLiveData<String>()
    private val _filterArray = MutableLiveData<ArrayList<String>>()
    val filter: LiveData<String> = _filter
    val filterArray: LiveData<ArrayList<String>> = _filterArray

    fun addFilter(disasterType: String) {
        val listFilter = _filterArray.value ?: ArrayList()
        if (!listFilter.contains(disasterType)) {
            listFilter.add(disasterType)
        }
        _filterArray.value = listFilter
        updateFilter()
    }

    fun removeFilter(disasterType: String) {
        val listFilter = _filterArray.value ?: ArrayList()
        if (listFilter.contains(disasterType)) {
            listFilter.remove(disasterType)
        }
        _filterArray.value = listFilter
        updateFilter()
    }

    private fun updateFilter() {
        val listFilter = _filterArray.value as ArrayList<String>
        if (listFilter.isNotEmpty()) {
            val beginEnd = "\'"
            val filterArgs = listFilter.joinToString("\', \'")
            _filter.value = beginEnd + filterArgs + beginEnd
        } else {
            _filter.value = ""
        }
    }

    private fun getFilter(): String {
        println("Type: ${_filter.value ?: ""}")
        return filter.value ?: ""
    }

    private val _cityId = MutableLiveData<String>()
    val cityId: LiveData<String> = _cityId

    private fun getCity(): String {
        return _cityId.value ?: ""
    }

    fun setLocation(location: String) {
        _cityId.value = DisasterUtils(rp).getRegionCode(location)
    }

    private val _isWarned = MutableLiveData<Boolean>()
    private val _warningText = MutableLiveData<String>()
    val isWarned: LiveData<Boolean> = _isWarned
    val warningText: LiveData<String> = _warningText

    fun setWarn(state: Boolean, msg: String) {
        _isWarned.value = state
        _warningText.value = msg
    }

    fun getDisaster(): LiveData<Resource<PagedList<DisasterModel>>> {
        return repository.getReportArchive(SortHelper.RECENT, getFilter(), getCity())
    }

}