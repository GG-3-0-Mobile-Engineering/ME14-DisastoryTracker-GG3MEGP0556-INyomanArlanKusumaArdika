package com.frhanklin.disastory.utils

import com.frhanklin.disastory.data.local.entity.DisasterModel

object DummyData {

    fun getDummyDisaster(): List<DisasterModel> {
        val dummyFlood = arrayListOf<DisasterModel>(
            getDummyDisasterSingle("1", "flood", -8.4095, 115.1889, "ID-BA"),
            getDummyDisasterSingle("2", "flood", 4.6951, 96.7494, "ID-AC"),
            getDummyDisasterSingle("3", "flood", 0.5387, 116.4194, "ID-KI"),
            getDummyDisasterSingle("4", "flood", -6.2088, 106.8456, "ID-JK"),
            getDummyDisasterSingle("5", "flood", -3.0926, 115.2838, "ID-KS")
        )

        val dummyHaze = arrayListOf(
            getDummyDisasterSingle("6", "haze", -6.4058, 106.0640, "ID-BT") // Banten
        )

        val dummyVolcano = arrayListOf(
            getDummyDisasterSingle("7", "volcano", -8.0652, 115.4207, "ID-BA"), // Bali
            getDummyDisasterSingle("8", "volcano", -7.2575, 112.7521, "ID-JI") // East Java
        )

        val dummyEarthquake = arrayListOf(
            getDummyDisasterSingle("9", "earthquake", -8.5833, 116.1167, "ID-NB"), // Nusa Tenggara Barat
            getDummyDisasterSingle("10", "earthquake", -0.5021, 117.1535, "ID-KT") // Kalimantan Tengah
        )

        val dummyWind = arrayListOf(
            getDummyDisasterSingle("11", "wind", -7.2575, 112.7521, "ID-JI") // East Java
        )

        val dummyFire = arrayListOf(
            getDummyDisasterSingle("12", "fire", -6.2088, 106.8456, "ID-JK"), // Jakarta
            getDummyDisasterSingle("13", "fire", -7.2575, 112.7521, "ID-JI") // East Java
        )

        val returnDummyData = dummyFlood + dummyHaze + dummyVolcano + dummyEarthquake + dummyWind + dummyFire
        returnDummyData.shuffled()

        // Combine all dummy data
        return returnDummyData

    }

    fun getDummyDisasterSingle(key: String, type: String, lat: Double, lng: Double, city: String): DisasterModel{
        return DisasterModel(
            key,
            "2023-08-14T07:09:10.538Z",
            type,
            "",
            "Dummy Title",
            "Dummy Text",
            city,
            latitude = lat,
            longitude = lng,
            ""
        )
    }
}