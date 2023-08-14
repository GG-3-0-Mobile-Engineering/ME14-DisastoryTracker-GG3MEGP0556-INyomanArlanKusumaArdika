package com.frhanklin.disastory.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.frhanklin.disastory.data.remote.ApiResponse
import com.frhanklin.disastory.data.remote.StatusResponse

abstract class NetworkBoundResource<ResultType, RequestType>(private val mExecutors: AppExecutors){

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromRemote(dbSource)
            } else {
                result.addSource(dbSource) {newData ->
                    result.value = Resource.success(newData)
                }
            }
        }
    }

    private fun fetchFromRemote(dbSource: LiveData<ResultType>) {
        val response = createCall()

        result.addSource(dbSource) { newData ->
            result.value = Resource.loading(newData)
        }

        result.addSource(response) { apiResponse ->
            result.removeSource(response)
            result.removeSource(dbSource)

            when (apiResponse.status) {
                StatusResponse.SUCCESS -> {
                    mExecutors.diskIO().execute {
                        saveCallResult(apiResponse.body)
                        mExecutors.mainThread().execute {
                            result.addSource(loadFromDb()) {
                                result.value = Resource.success(it)
                            }
                        }
                    }
                }
                StatusResponse.ERROR -> {
                    result.addSource(dbSource) {
                        result.value = Resource.error(apiResponse.message, it)
                    }
                }
                StatusResponse.EMPTY -> {
                    mExecutors.mainThread().execute {
                        result.addSource(loadFromDb()) {
                            result.value = Resource.success(it)
                        }
                    }
                }
            }
        }

    }


    fun asLiveData() : LiveData<Resource<ResultType>> = result


    abstract fun loadFromDb(): LiveData<ResultType>
    abstract fun shouldFetch(data: ResultType?): Boolean
    abstract fun createCall(): LiveData<ApiResponse<RequestType>>
    abstract fun saveCallResult(data: RequestType)


}