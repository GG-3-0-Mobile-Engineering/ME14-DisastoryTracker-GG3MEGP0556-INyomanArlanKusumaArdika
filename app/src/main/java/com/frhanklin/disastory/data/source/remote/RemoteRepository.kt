package com.frhanklin.disastory.data.source.remote

import androidx.lifecycle.LiveData
import com.frhanklin.disastory.data.source.remote.response.DisasterItems

interface RemoteRepository {
    fun getDisastersFromApi(): LiveData<ApiResponse<List<DisasterItems>>>
}