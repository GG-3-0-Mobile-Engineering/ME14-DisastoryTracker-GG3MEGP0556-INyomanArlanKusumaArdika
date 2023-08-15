package com.frhanklin.disastory.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZonedDateTime

class TimeAndDateUtilsTest {

    val util = TimeAndDateUtils

    @Test
    fun convertTimeStamp() {
        val expectedTimeStamp = "2023-08-09 \n11:08:26"
        val input = "2023-08-09T03:08:26.141Z"

        val output = util.convertTimeStamp(input)
        assertEquals(expectedTimeStamp, output)
    }

    @Test
    fun getCurrentDate() {
        val expectedDate = util.formatToIso(ZonedDateTime.now())

        val output = util.getCurrentDate()
        assertEquals(expectedDate, output)
    }

    @Test
    fun getDateOneWeek() {
        val expectedDate = util.formatToIso(ZonedDateTime.now().minusWeeks(1))

        val output = util.getDateOneWeek()
        assertEquals(expectedDate, output)
    }

}