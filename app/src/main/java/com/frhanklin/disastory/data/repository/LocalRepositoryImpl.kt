package com.frhanklin.disastory.data.repository

import androidx.paging.DataSource
import com.frhanklin.disastory.data.local.entity.DisasterModel
import com.frhanklin.disastory.data.local.dao.DisasterDao
import com.frhanklin.disastory.domain.repository.LocalRepository
import com.frhanklin.disastory.utils.SortHelper
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val mDisasterDao: DisasterDao
): LocalRepository {

    companion object {
        private var INSTANCE: LocalRepositoryImpl? = null

        fun getInstance(disasterDao: DisasterDao): LocalRepositoryImpl =
            INSTANCE ?: LocalRepositoryImpl(disasterDao)
    }

    override fun getAllDisasters(
        sortOption: String,
        disasterTypes: String,
        city: String
    ): DataSource.Factory<Int, DisasterModel> {
        return mDisasterDao.getAllDisaster(SortHelper.getQuery(sortOption, disasterTypes, city, SortHelper.TABLE_REPORTS))
    }

    override fun getDisastersByDate(
        startDate: String,
        endDate: String
    ): DataSource.Factory<Int, DisasterModel> {
        return mDisasterDao.getDisasterRange(startDate, endDate)
    }

    override fun insertDisasters(disasters: List<DisasterModel>) {
        return mDisasterDao.insertDisasters(disasters)
    }


}