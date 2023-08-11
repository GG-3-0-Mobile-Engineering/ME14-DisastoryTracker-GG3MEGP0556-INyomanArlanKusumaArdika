package com.frhanklin.disastory.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
object TimeAndDateUtils {

    fun convertTimeStamp(timestamp: String): String {
        val instant = Instant.parse(timestamp)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd \nHH:mm:ss")
        return localDateTime.format(formatter)
    }

    fun getCurrentDate(): String {
        return formatToIso(ZonedDateTime.now())
    }

    fun getDateOneWeek(): String {
        val currentTime = ZonedDateTime.now()
        val oneWeekAgo = currentTime.minusWeeks(1)
        return formatToIso(oneWeekAgo)
    }

    private fun formatToIso(dateTime: ZonedDateTime): String {
        val isoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        return dateTime.format(isoFormat)
    }


}