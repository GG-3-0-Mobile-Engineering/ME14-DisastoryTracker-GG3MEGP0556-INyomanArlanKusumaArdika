package com.frhanklin.disastory.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.frhanklin.disastory.utils.NetworkBoundResource
import com.frhanklin.disastory.data.local.entity.DisasterModel
import com.frhanklin.disastory.data.remote.ApiResponse
import com.frhanklin.disastory.data.remote.response.DisasterItems
import com.frhanklin.disastory.domain.repository.AppRepository
import com.frhanklin.disastory.domain.repository.LocalRepository
import com.frhanklin.disastory.domain.repository.RemoteRepository
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

}