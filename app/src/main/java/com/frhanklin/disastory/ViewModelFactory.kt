package com.frhanklin.disastory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frhanklin.disastory.utils.ResourceProvider


class ViewModelFactory(
    private val preferences: SettingPreferences,
    private val resourceProvider: ResourceProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(preferences, resourceProvider) as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class " + modelClass.name)
    }
}