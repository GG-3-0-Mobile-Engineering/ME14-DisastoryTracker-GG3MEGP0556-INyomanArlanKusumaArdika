package com.frhanklin.disastory.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.frhanklin.disastory.data.local.entity.DisasterModel
import com.frhanklin.disastory.utils.Resource

interface AppRepository {

    fun getReportArchive(
        sortOption: String,
        disasterTypes: String,
        city: String
    ): LiveData<Resource<PagedList<DisasterModel>>>


}