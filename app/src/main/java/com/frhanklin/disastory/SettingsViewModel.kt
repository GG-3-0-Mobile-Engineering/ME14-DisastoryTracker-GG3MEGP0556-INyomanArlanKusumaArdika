package com.frhanklin.disastory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getThemeSettings() : LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSettings(darkModeState: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(darkModeState)
        }
    }

}