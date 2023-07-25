package com.frhanklin.disastory.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeAndDateUtils {

    fun convertTimeStamp(timestamp: String): String {
        val instant = Instant.parse(timestamp)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd \nHH:mm:ss")
        return localDateTime.format(formatter)
    }

}