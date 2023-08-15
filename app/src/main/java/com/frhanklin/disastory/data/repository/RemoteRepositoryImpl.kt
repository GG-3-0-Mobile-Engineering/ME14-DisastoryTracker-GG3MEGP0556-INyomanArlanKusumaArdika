package com.frhanklin.disastory.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.frhanklin.disastory.data.remote.ApiResponse
import com.frhanklin.disastory.data.remote.api.ApiService
import com.frhanklin.disastory.data.remote.response.DisasterItems
import com.frhanklin.disastory.data.remote.response.DisasterProperties
import com.frhanklin.disastory.data.remote.response.PetaBencanaReports
import com.frhanklin.disastory.data.remote.response.Tags
import com.frhanklin.disastory.domain.repository.RemoteRepository
import com.frhanklin.disastory.utils.DummyData
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
//        Api Call
        retrofit.getArchive().enqueue(object: Callback<PetaBencanaReports> {
            override fun onResponse(
                call: Call<PetaBencanaReports>,
                response: Response<PetaBencanaReports>
            ) {
                result.value =
                    ApiResponse.success(response.body()?.result?.objects?.output?.geometries as List<DisasterItems>)
            }

            override fun onFailure(call: Call<PetaBencanaReports>, t: Throwable) {
                Log.d(TAG, "onFailure getDisastersFromApi: ${t.message}")

            }

        })

//        Dummy call
//        val fetchedDummy = DummyData.getDummyDisaster()
//        val listDisasterItems = ArrayList<DisasterItems>()
//        var id = 1
//        for (item in fetchedDummy) {
//            val insertToList = DisasterItems(
//                listOf(item.latitude, item.longitude),
//                DisasterProperties(
//                    "",
//                    item.type,
//                    item.createdAt,
//                    "",
//                    item.title,
//                    "",
//                    Tags(
//                        item.regionCode
//                    ),
//                    null,
//                    id.toString(),
//                    item.text,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null
//                )
//            )
//            listDisasterItems.add(insertToList)
//            id++
//        }
//
//        val dummyResponse = ApiResponse.success(listDisasterItems as List<DisasterItems>)
//        result.value = dummyResponse

        mIdlingResource.decrement()
        return result
    }


}