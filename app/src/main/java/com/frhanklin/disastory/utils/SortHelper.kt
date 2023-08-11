package com.frhanklin.disastory.utils

import androidx.sqlite.db.SimpleSQLiteQuery
import java.lang.StringBuilder

object SortHelper {

    const val RECENT = "Recent First"
    const val OLDEST = "Oldest First"

    const val TABLE_REPORTS = "disaster"

    fun getQuery(
        sortOption: String,
        disasterTypeFilter: String,
        cityFilter: String,
        tableName: String
    ): SimpleSQLiteQuery {
        val query = StringBuilder()
            .append("SELECT * FROM $tableName ")
            .append( if (disasterTypeFilter.isNotEmpty() && cityFilter.isNotEmpty()) {
                "WHERE type IN ($disasterTypeFilter) AND region_code = \'$cityFilter\' "
            } else if (disasterTypeFilter.isNotEmpty() && cityFilter.isNullOrEmpty()) {
                "WHERE type IN ($disasterTypeFilter) "
            } else if (disasterTypeFilter.isNullOrEmpty() && cityFilter.isNotEmpty()) {
                "WHERE region_code = \'$cityFilter\' "
            } else {
                ""
            })
            .append( when (sortOption) {
                RECENT -> "ORDER BY created_at DESC "
                OLDEST -> "ORDER BY created_at ASC "
                else -> ""
            })
        return SimpleSQLiteQuery(query.toString())
    }


}