package com.frhanklin.disastory.data.source.local

import androidx.paging.DataSource
import com.frhanklin.disastory.data.source.local.entity.DisasterModel

interface LocalRepository {

    fun getAllDisasters(sortOption: String, disasterTypes: String, city: String): DataSource.Factory<Int, DisasterModel>

    fun getDisastersByDate(startDate: String, endDate: String): DataSource.Factory<Int, DisasterModel>

    fun insertDisasters(disasters: List<DisasterModel>)

}