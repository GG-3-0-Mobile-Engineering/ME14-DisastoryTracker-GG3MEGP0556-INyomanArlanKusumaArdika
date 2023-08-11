package com.frhanklin.disastory.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.frhanklin.disastory.data.source.NetworkBoundResource
import com.frhanklin.disastory.data.source.local.LocalRepository
import com.frhanklin.disastory.data.source.local.entity.DisasterModel
import com.frhanklin.disastory.data.source.remote.ApiResponse
import com.frhanklin.disastory.data.source.remote.RemoteRepository
import com.frhanklin.disastory.data.source.remote.response.DisasterItems
import com.frhanklin.disastory.utils.AppExecutors
import com.frhanklin.disastory.utils.Resource
import javax.inject.Inject


class AppRepositoryImpl @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val appExecutors: AppExecutors
) : AppRepository {
    override fun getReportArchive(
        sortOption: String,
        disasterTypes: String,
        city: String
    ): LiveData<Resource<PagedList<DisasterModel>>> {
        return object : NetworkBoundResource<PagedList<DisasterModel>, List<DisasterItems>>(appExecutors) {
            override fun loadFromDb(): LiveData<PagedList<DisasterModel>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()

                return LivePagedListBuilder(localRepository.getAllDisasters(sortOption, disasterTypes, city), config).build()
            }

            override fun createCall(): LiveData<ApiResponse<List<DisasterItems>>> {
                return remoteRepository.getDisastersFromApi()
//                return liveData {
//                    val response = remoteRepository.getDisastersFromApi()
//                    response.value?.let { emit(it) }
//                }
            }

            override fun saveCallResult(data: List<DisasterItems>) {
                val disasterList = ArrayList<DisasterModel>()
                for (disasterItem in data) {
                    val disaster = DisasterModel(
                        key = disasterItem.disasterProperties?.pkey.toString(),
                        createdAt = disasterItem.disasterProperties?.createdAt.toString(),
                        type = disasterItem.disasterProperties?.disasterType.toString(),
                        image = disasterItem.disasterProperties?.imageUrl.toString(),
                        title = disasterItem.disasterProperties?.title.toString(),
                        text = disasterItem.disasterProperties?.text.toString(),
                        regionCode = disasterItem.disasterProperties?.tags?.instanceRegionCode.toString(),
                        latitude = disasterItem.coordinates?.get(0) ?: 0.0,
                        longitude = disasterItem.coordinates?.get(1) ?: 0.0,
                        lastUpdated = disasterItem.disasterProperties?.lastUpdated.toString()
                    )
                    disasterList.add(disaster)
                }
                localRepository.insertDisasters(disasterList)
            }

            override fun shouldFetch(data: PagedList<DisasterModel>?): Boolean {
                return data.isNullOrEmpty()
            }

        }.asLiveData()
    }


//    companion object {
//        @Volatile
//        private var INSTANCE: AppRepositoryImpl? = null
//        fun getInstance(
//            remoteRepository: RemoteRepository,
//            localRepository: LocalRepository,
//            appExecutors: AppExecutors
//        ) : AppRepositoryImpl {
//            return INSTANCE ?: synchronized(this) {
//                INSTANCE ?: AppRepositoryImpl(remoteRepository, localRepository, appExecutors).apply { INSTANCE = this }
//            }
//        }
//    }


}