package com.frhanklin.disastory.data

import com.frhanklin.disastory.data.response.DisasterItems
import com.frhanklin.disastory.data.response.DisasterProperties
import com.frhanklin.disastory.data.response.Objects
import com.frhanklin.disastory.data.response.Output
import com.frhanklin.disastory.data.response.PetaBencanaReports
import com.frhanklin.disastory.data.response.ReportData
import com.frhanklin.disastory.data.response.Result
import com.frhanklin.disastory.data.response.Tags
import com.frhanklin.disastory.data.response.Transform
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
                    type = "Point",
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
                    type = "Polygon",
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
                    ),
                    arcs = listOf(listOf(0))
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
                        geometries = additionalItems,
                        type = "GeometryCollection"
                    )
                ),
                type = "Topology",
                arcs = emptyList(),
                bbox = listOf(106.8329505207, -6.9813433971, 110.3178483392, -6.1807951369),
                transform = Transform(
                    scale = listOf(0.0000003311331833192323, 0.00000032713269326930546),
                    translate = listOf(106.7917869997, -6.158925)
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
                        geometries = additionalItems,
                        type = "GeometryCollection"
                    )
                ),
                type = "Topology",
                arcs = emptyList(),
                bbox = listOf(106.8329505207, -6.9813433971, 110.3178483392, -6.1807951369),
                transform = Transform(
                    scale = listOf(0.0000003311331833192323, 0.00000032713269326930546),
                    translate = listOf(106.7917869997, -6.158925)
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
                type = "Topology",
                objects = Objects(
                    output = Output(
                        type = "GeometryCollection",
                        geometries = additionalItems
                    )
                ),
                arcs = listOf(
                    listOf(
                        listOf(9999, 7847),
                        listOf(-507, -6),
                        listOf(-695, -70),
                        listOf(-317, -221),
                        listOf(-761, -18),
                        listOf(-516, 98),
                        listOf(-641, -61),
                        listOf(-649, -119),
                        listOf(-169, -762),
                        listOf(-181, -519),
                        listOf(48, -602),
                        listOf(-130, -162),
                        listOf(64, -1235),
                        listOf(81, -2351),
                        listOf(136, -1098),
                        listOf(15, -675),
                        listOf(-1250, -40),
                        listOf(-879, -6),
                        listOf(-924, 217),
                        listOf(-924, 425),
                        listOf(-1800, 138),
                        listOf(830, 1540),
                        listOf(565, 1455),
                        listOf(764, 1975),
                        listOf(1018, 2079),
                        listOf(384, 788),
                        listOf(389, 1061),
                        listOf(1398, -76),
                        listOf(296, -25),
                        listOf(360, 6),
                        listOf(392, -9),
                        listOf(360, -9),
                        listOf(377, 12),
                        listOf(323, 25),
                        listOf(354, 76),
                        listOf(296, 89),
                        listOf(211, 42),
                        listOf(290, 52),
                        listOf(217, 28),
                        listOf(341, 110),
                        listOf(43, -67),
                        listOf(57, -174),
                        listOf(109, -159),
                        listOf(154, -257),
                        listOf(115, -370),
                        listOf(99, -346),
                        listOf(124, -357),
                        listOf(133, -422)
                    )
                ),
                transform = Transform(
                    scale = listOf(0.0000003311331833192323, 0.00000032713269326930546),
                    translate = listOf(106.7917869997, -6.158925)
                ),
                bbox = listOf(106.7917869997, -6.158925, 106.7950980004, -6.1556540002)
            )
        )
    }


}