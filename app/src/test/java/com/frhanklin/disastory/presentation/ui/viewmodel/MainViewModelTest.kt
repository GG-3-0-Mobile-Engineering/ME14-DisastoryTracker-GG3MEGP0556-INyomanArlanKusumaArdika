package com.frhanklin.disastory.presentation.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.frhanklin.disastory.data.local.entity.DisasterModel
import com.frhanklin.disastory.domain.repository.AppRepository
import com.frhanklin.disastory.domain.repository.LocalRepository
import com.frhanklin.disastory.utils.Resource
import com.frhanklin.disastory.utils.ResourceProvider
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var preferences: SettingPreferences
    private lateinit var rp: ResourceProvider
    private lateinit var localRepository: LocalRepository
    private lateinit var repository: AppRepository
    private lateinit var viewModel: MainViewModel


    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        preferences = mockk()
        rp = mockk()
        repository = mockk()
        localRepository = mockk()

        viewModel = MainViewModel(preferences, rp, repository)
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
    fun `getNotificationSettings `() {
        val expectedNotificationSetting: Flow<Boolean> = flowOf(true)
        coEvery { preferences.getNotificationSetting() } returns expectedNotificationSetting

        val observer = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.getNotificationSettings().observeForever(observer)
        verify { observer.onChanged(true) }
        coVerify { preferences.getNotificationSetting() }
    }

    @Test
    fun `saveThemeSettings `() {
        val newThemeState = true
        coEvery { preferences.saveThemeSetting(any()) } just Runs

        viewModel.saveThemeSettings(newThemeState)
        coVerify { preferences.saveThemeSetting(newThemeState) }
    }

    @Test
    fun `addFilter `() {
        val initialFilter = arrayListOf("flood", "fire")

        for (item in initialFilter) {
            viewModel.addFilter(item)
        }
        assertEquals(initialFilter, viewModel.filterArray.value)
    }

    @Test
    fun `removeFilter `() {
        val initialFilter = arrayListOf("flood", "fire")
        initialFilter.add("earthquake")

        for (item in initialFilter) {
            viewModel.addFilter(item)
        }
        val removedFilter = "fire"
        viewModel.removeFilter(removedFilter)
        val resultFilter = initialFilter
        resultFilter.remove(removedFilter)

        assertEquals(resultFilter, viewModel.filterArray.value)
    }

    @Test
    fun `setWarn `() {
        val initialWarningState = false
        val initialWarningMsg = ""
        viewModel.setWarn(initialWarningState, initialWarningMsg)

        assertEquals(viewModel.isWarned.value, initialWarningState)
        assertEquals(viewModel.warningText.value, initialWarningMsg)

        val newWarningState = true
        val newWarningMsg = "Ab error occured"
        viewModel.setWarn(newWarningState, newWarningMsg)

        assertEquals(viewModel.isWarned.value, newWarningState)
        assertEquals(viewModel.warningText.value, newWarningMsg)
    }

    @Test
    fun `getDisaster `() {
        val testData = mockk<PagedList<DisasterModel>>()
        val testResource = liveData<Resource<PagedList<DisasterModel>>> { Resource.success(testData) }
        coEvery { repository.getReportArchive(any(), any(), any()) } returns testResource

        val observer: Observer<Resource<PagedList<DisasterModel>>> = mockk(relaxUnitFun = true)
        viewModel.getDisaster().observeForever(observer)

        verify { repository.getReportArchive(any(), any(), any()) }

    }

}