package com.frhanklin.disastory.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.concurrent.Volatile

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val KEY_THEME = booleanPreferencesKey("theme_setting")
    private val KEY_NOTIFICATION = booleanPreferencesKey("notification_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[KEY_THEME] ?: false
        }
    }

    fun getNotificationSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[KEY_NOTIFICATION] ?: false
        }
    }

    suspend fun saveThemeSetting(darkModeState: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_THEME] = darkModeState
        }
    }

    suspend fun saveNotificationSetting(notificationEnableState: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_NOTIFICATION] = notificationEnableState
        }
    }



    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }



}