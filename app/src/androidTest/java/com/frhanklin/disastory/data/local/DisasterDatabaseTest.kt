package com.frhanklin.disastory.data.local

import android.content.Context
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.frhanklin.disastory.data.local.dao.DisasterDao
import com.frhanklin.disastory.data.local.entity.DisasterModel
import com.frhanklin.disastory.utils.DummyData
import com.frhanklin.disastory.utils.SortHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
class DisasterDatabaseTest {

    private lateinit var disasterDao: DisasterDao
    private lateinit var database: DisasterDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, DisasterDatabase::class.java).build()
        disasterDao = database.disasterDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndGetDisaster() = runBlocking {
        val disaster = DummyData.getDummyDisasterSingle("1", "flood", -6.931, 108.892, "ID-BA")
        disasterDao.insertDisasters(listOf(disaster))

        val dataSourceFactory = disasterDao.getAllDisaster(SortHelper.getQuery(SortHelper.RECENT, "", "", SortHelper.TABLE_REPORTS))

        val pagedListLiveData = LivePagedListBuilder(
            dataSourceFactory,
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build()
        ).build()

        var loadedDisaster : List<DisasterModel>? = null
        val latch = CountDownLatch(1)

        runBlocking(Dispatchers.Main) {
            val job = launch {
                pagedListLiveData.observeForever { pagedList ->
                    if (pagedList != null) {
                        loadedDisaster = pagedList.snapshot()
                        latch.countDown()
                    }
                }
            }
            withContext(Dispatchers.IO) {
                latch.await()
            }

            assertNotNull(loadedDisaster)
            assertTrue(loadedDisaster?.contains(disaster) == true)

            pagedListLiveData.removeObserver {}
            job.cancel()

        }

    }


}