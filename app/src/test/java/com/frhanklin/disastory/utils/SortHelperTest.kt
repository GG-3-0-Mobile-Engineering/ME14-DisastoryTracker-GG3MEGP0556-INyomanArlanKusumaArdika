package com.frhanklin.disastory.utils

import org.junit.Assert.*

import org.junit.Test

class SortHelperTest {

    val helper = SortHelper

    @Test
    fun getQuery() {
        val disasterTypes = "\'haze\', \'flood\'"
        val regionCode = "ID-BA"

        val expectedQuery = "SELECT * FROM disaster WHERE type IN ($disasterTypes) AND region_code = \'$regionCode\' ORDER BY created_at DESC "

        val outputQuery = helper.getQuery(helper.RECENT, disasterTypes, regionCode, helper.TABLE_REPORTS)

        assertEquals(expectedQuery, outputQuery.sql)

    }
}