package com.frhanklin.disastory.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.frhanklin.disastory.R
import com.frhanklin.disastory.api.ApiConfig
import com.frhanklin.disastory.data.DisastoryDummyData
import com.frhanklin.disastory.data.response.DisasterItems
import com.frhanklin.disastory.data.response.PetaBencanaReports
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class NotificationWorker(c: Context, workerParams: WorkerParameters) : Worker(c, workerParams) {

    companion object {
        private val TAG = NotificationWorker::class.java.simpleName

        const val NOTIFICATION_ID_FLOOD_WARNING = 1
        const val CHANNEL_ID = "channel_disastory"
        const val CHANNEL_NAME = "disastory_notification"
    }


    override fun doWork(): Result {
        while (!isStopped) {
            showNotification()
            Thread.sleep(TimeUnit.MINUTES.toMillis(15))
        }

        return Result.success()
    }

    private fun showNotification() {
        val report = getFloodData()

//      Getting recent flood report that has state 3 / 4
        lateinit var recent : DisasterItems
        for (item in report.result?.objects?.output?.geometries!!) {
            if (item != null && item.disasterProperties?.state!! >= 3) {
                recent = item
                break
            }
        }
        showFloodWarningNotification(
            title = "Banjir terdeteksi!",
            description = "Lokasi: ${recent.disasterProperties!!.cityName} | Ketinggian > 70cm"
        )
    }

    private fun getFloodData(): PetaBencanaReports {
//        return getFloodDataFromRemote()
        return getFloodDataFromDummyy()
    }

    private fun getFloodDataFromDummyy(): PetaBencanaReports = DisastoryDummyData.getDummyFloodReports()

    private fun getFloodDataFromRemote(): PetaBencanaReports {
        val client = ApiConfig.getApiService().getFloodsStates()
        var report = PetaBencanaReports(null, null)
        client.enqueue(object: Callback<PetaBencanaReports> {
            override fun onResponse(
                call: Call<PetaBencanaReports>,
                response: Response<PetaBencanaReports>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    report = responseBody as PetaBencanaReports
                } else {

                }
            }

            override fun onFailure(call: Call<PetaBencanaReports>, t: Throwable) {

            }

        })
        return report
    }

    private fun showFloodWarningNotification(title: String, description: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_important_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID_FLOOD_WARNING, notification.build())
    }



}