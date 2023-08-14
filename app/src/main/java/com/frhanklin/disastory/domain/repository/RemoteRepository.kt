package com.frhanklin.disastory.domain.repository

import androidx.lifecycle.LiveData
import com.frhanklin.disastory.data.remote.ApiResponse
import com.frhanklin.disastory.data.remote.response.DisasterItems

interface RemoteRepository {
    fun getDisastersFromApi(): LiveData<ApiResponse<List<DisasterItems>>>
}