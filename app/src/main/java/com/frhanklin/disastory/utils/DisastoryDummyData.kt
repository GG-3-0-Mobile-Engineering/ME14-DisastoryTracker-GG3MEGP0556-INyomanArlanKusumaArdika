package com.frhanklin.disastory.utils

import com.frhanklin.disastory.data.remote.response.DisasterItems
import com.frhanklin.disastory.data.remote.response.DisasterProperties
import com.frhanklin.disastory.data.remote.response.Objects
import com.frhanklin.disastory.data.remote.response.Output
import com.frhanklin.disastory.data.remote.response.PetaBencanaReports
import com.frhanklin.disastory.data.remote.response.ReportData
import com.frhanklin.disastory.data.remote.response.Result
import com.frhanklin.disastory.data.remote.response.Tags
import kotlin.random.Random

object DisastoryDummyData {

    private fun generateRandomCoordinates(minLat: Double, maxLat: Double, minLng: Double, maxLng: Double): List<Double> {
        val lat = Random.nextDouble(minLat, maxLat)
        val lng = Random.nextDouble(minLng, maxLng)
        return listOf(lat, lng)
    }

    // Function to generate multiple DisasterItems with random coordinates within Indonesia
    private fun generateDummyDisasterItems(count: Int, disasterType: String, regionCode: String): List<DisasterItems> {
        val regions = mapOf(
            "ID-AC" to Pair(4.0, 6.0), // Aceh
            "ID-BA" to Pair(-9.0, -8.0), // Bali
            "ID-JK" to Pair(-6.4, -5.9), // Jakarta
            "ID-KS" to Pair(-4.0, -3.0), // Kalimantan Selatan
            "ID-JB" to Pair(-7.7, -5.6), // Jawa Barat
            "ID-JI" to Pair(-8.6, -5.6), // Jawa Timur
            "ID-KB" to Pair(-7.0, -5.6), // Kalimantan Barat
            "ID-KT" to Pair(-7.0, -5.0), // Kalimantan Tengah
            "ID-KI" to Pair(-5.0, -2.5) // Kalimantan Timur
            // Add more regions as needed
        )

        val dummyDisasterItems = mutableListOf<DisasterItems>()

        repeat(count) {
            val coordinates = generateRandomCoordinates(
                regions[regionCode]?.first ?: -11.0,
                regions[regionCode]?.second ?: 6.0,
                95.0,
                141.0
            )
            dummyDisasterItems.add(
                DisasterItems(
                    disasterProperties = DisasterProperties(
                        imageUrl = null,
                        disasterType = disasterType,
                        createdAt = "2023-07-26T00:00:00.000Z",
                        source = "dummy",
                        text = "Dummy data for $disasterType",
                        reportData = ReportData(reportType = disasterType, fireDistance = null),
                        tags = Tags(instanceRegionCode = regionCode)
                    ),
                    coordinates = coordinates
                )
            )
        }

        return dummyDisasterItems
    }

    private fun getDummyReportsItems(): List<DisasterItems> {
        val jsonDisasterItems = mutableListOf<DisasterItems>()
        jsonDisasterItems.addAll(generateDummyDisasterItems(2, "flood", "ID-SU")) // North Sumatera
        jsonDisasterItems.addAll(generateDummyDisasterItems(3, "haze", "ID-BA")) // Bali
        jsonDisasterItems.addAll(generateDummyDisasterItems(1, "wind", "ID-JK")) // Jakarta
        jsonDisasterItems.addAll(generateDummyDisasterItems(4, "earthquake", "ID-KS")) // Kalimantan Selatan
        jsonDisasterItems.addAll(generateDummyDisasterItems(2, "volcano", "ID-JI")) // Jawa Timur
        jsonDisasterItems.addAll(generateDummyDisasterItems(5, "fire", "ID-KT")) // Kalimantan Tengah
        return jsonDisasterItems
    }

    private fun getDummyReportsItems(paramType: String, paramValue: String): List<DisasterItems> {
        val jsonDisasterItems = mutableListOf<DisasterItems>()
        if (paramType == "filter") {
//            return getDummyReportsItems(count, disasterType = paramValue, "ÏD-JK")
            jsonDisasterItems.addAll(generateDummyDisasterItems(3, paramValue, "ID-SU"))

        } else {
            jsonDisasterItems.addAll(generateDummyDisasterItems(3, "flood", paramValue))
        }
        return jsonDisasterItems
    }

    private fun getDummyReportsItems(count: Int, disasterType: String, regionCode: String): List<DisasterItems> {
        val jsonDisasterItems = mutableListOf<DisasterItems>()
        if (disasterType == "") {
            return getDummyReportsItems()
        } else {
            jsonDisasterItems.addAll(generateDummyDisasterItems(count, disasterType, regionCode))

        }
        return jsonDisasterItems
    }

    // Function to generate multiple DisasterItems with random coordinates within Indonesia for JSON 2
    private fun generateDummyDisasterFloodsItems(count: Int, regionCode: String): List<DisasterItems> {
        val regions = mapOf(
            "ID-AC" to Pair(4.0, 6.0), // Aceh
            "ID-BA" to Pair(-9.0, -8.0), // Bali
            "ID-JK" to Pair(-6.4, -5.9), // Jakarta
            "ID-KS" to Pair(-4.0, -3.0), // Kalimantan Selatan
            "ID-JB" to Pair(-7.7, -5.6), // Jawa Barat
            "ID-JI" to Pair(-8.6, -5.6), // Jawa Timur
            "ID-KB" to Pair(-7.0, -5.6), // Kalimantan Barat
            "ID-KT" to Pair(-7.0, -5.0), // Kalimantan Tengah
            "ID-KI" to Pair(-5.0, -2.5) // Kalimantan Timur
            // Add more regions as needed
        )

        val dummyDisasterItems = mutableListOf<DisasterItems>()

        repeat(count) {
            val state = (3..4).random() // Generate random state value ranging from 1 to 4
            val coordinates = generateRandomCoordinates(
                regions[regionCode]?.first ?: -11.0,
                regions[regionCode]?.second ?: 6.0,
                106.7917869997,
                106.7950980004
            )
            dummyDisasterItems.add(
                DisasterItems(
                    disasterProperties = DisasterProperties(
                        imageUrl = null,
                        disasterType = "flood",
                        createdAt = "2023-07-26T00:00:00.000Z",
                        source = "dummy",
                        areaId = "5",
                        geomId = "3174040004009000",
                        areaName = "RW 09",
                        parentName = "GROGOL",
                        cityName = "Jakarta",
                        state = state,
                        lastUpdated = "2016-12-19T13:53:52.274Z"
                    )
                )
            )
        }

        return dummyDisasterItems
    }

    private fun getDummyFloodReportsItems(): List<DisasterItems> {
        val jsonDisasterFloodItems = mutableListOf<DisasterItems>()
        jsonDisasterFloodItems.addAll(generateDummyDisasterFloodsItems(2, "ID-SU")) // North Sumatera
        return jsonDisasterFloodItems
    }

    fun getDummyReports(): PetaBencanaReports {
        val additionalItems = getDummyReportsItems()

        return PetaBencanaReports(
            result = Result(
                objects = Objects(
                    output = Output(
                        geometries = additionalItems
                    )
                )
            ),
            statusCode = 200
        )
    }

    fun getDummyReports(param1: String, param2: String): PetaBencanaReports {
        val additionalItems = when (param1) {
            "disasterFilter" -> getDummyReportsItems("filter", param2)
            "cityFilter" -> getDummyReportsItems("code", param2)
            else -> getDummyReportsItems(1, disasterType = param1, regionCode = param2)
        }


        return PetaBencanaReports(
            result = Result(
                objects = Objects(
                    output = Output(
                        geometries = additionalItems
                    )
                )
            ),
            statusCode = 200
        )
    }


    fun getDummyFloodReports(): PetaBencanaReports {
        val additionalItems = getDummyFloodReportsItems()

        return PetaBencanaReports(
            statusCode = 200,
            result = Result(
                objects = Objects(
                    output = Output(
                        geometries = additionalItems
                    )
                )
            )
        )
    }


}