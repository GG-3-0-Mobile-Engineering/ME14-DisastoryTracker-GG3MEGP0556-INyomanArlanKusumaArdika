package com.frhanklin.disastory.presentation.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.frhanklin.disastory.utils.SettingPreferences
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var preferences: SettingPreferences
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        preferences = mockk()
        viewModel = SettingsViewModel(preferences)
    }

    @Test
    fun `getThemeSettings `() {
        val expectedThemeSetting: Flow<Boolean> = flowOf(true)
        coEvery { preferences.getThemeSetting() } returns expectedThemeSetting

        val observer = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.getThemeSettings().observeForever(observer)

        viewModel.saveThemeSettings(true)
        verify { observer.onChanged(true) }
        coVerify { preferences.getThemeSetting() }
    }

    @Test
    fun `saveThemeSettings `() {
        val newThemeState = true
        coEvery { preferences.saveThemeSetting(any()) } just Runs

        viewModel.saveThemeSettings(newThemeState)
        coVerify { preferences.saveThemeSetting(newThemeState) }
    }

    @Test
    fun `getNotificationSettings `() {
        val expectedNotificationSetting: Flow<Boolean> = flowOf(true)
        coEvery { preferences.getNotificationSetting() } returns expectedNotificationSetting

        val observer = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.getNotificationSettings().observeForever(observer)

        viewModel.saveNotificationSettings(true)
        verify { observer.onChanged(true) }
        coVerify { preferences.getNotificationSetting() }
    }

    @Test
    fun `saveNotificationSettings `() {
        val newNotificationState = true
        coEvery { preferences.saveNotificationSetting(any()) } just Runs

        viewModel.saveNotificationSettings(newNotificationState)
        coVerify { preferences.saveNotificationSetting(newNotificationState) }
    }
}