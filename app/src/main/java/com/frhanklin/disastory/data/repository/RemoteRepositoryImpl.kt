package com.frhanklin.disastory.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.frhanklin.disastory.data.remote.ApiResponse
import com.frhanklin.disastory.data.remote.api.ApiService
import com.frhanklin.disastory.data.remote.response.DisasterItems
import com.frhanklin.disastory.data.remote.response.PetaBencanaReports
import com.frhanklin.disastory.domain.repository.RemoteRepository
import com.frhanklin.disastory.utils.EspressoIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val retrofit: ApiService
) : RemoteRepository {

    private val mIdlingResource = EspressoIdlingResource

    companion object {
        private const val TAG = "RemoteRepository"
    }

    override fun getDisastersFromApi(): LiveData<ApiResponse<List<DisasterItems>>> {
        mIdlingResource.increment()
        val result = MutableLiveData<ApiResponse<List<DisasterItems>>>()
        retrofit.getArchive().enqueue(object: Callback<PetaBencanaReports> {
            override fun onResponse(
                call: Call<PetaBencanaReports>,
                response: Response<PetaBencanaReports>
            ) {
                result.value =
                    ApiResponse.success(response.body()?.result?.objects?.output?.geometries as List<DisasterItems>)
                mIdlingResource.decrement()
            }

            override fun onFailure(call: Call<PetaBencanaReports>, t: Throwable) {
                Log.d(TAG, "onFailure getDisastersFromApi: ${t.message}")
                mIdlingResource.decrement()
            }

        })
        return result
    }


}