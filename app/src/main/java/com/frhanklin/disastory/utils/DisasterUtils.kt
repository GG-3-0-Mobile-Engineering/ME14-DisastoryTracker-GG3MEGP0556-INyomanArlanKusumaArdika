package com.frhanklin.disastory.utils

import android.graphics.drawable.Drawable
import com.frhanklin.disastory.R
import javax.inject.Inject

class DisasterUtils @Inject constructor(private val rp: ResourceProvider) {

    fun getRegionCode(location: String): String {
        return when(location) {
            "Bali" -> {
                "ID-BA"
            }
            rp.getString(R.string.city_aceh) -> {
                "ID-AC"
            }
            rp.getString(R.string.city_bali) -> {
                "ID-BA"
            }

            rp.getString(R.string.city_bangka_belitung) -> {
                "ID-BB"
            }
            rp.getString(R.string.city_banten) -> {
                "ID-BT"
            }
            rp.getString(R.string.city_bengkulu) -> {
                "ID-BE"
            }
            rp.getString(R.string.city_jawa_tengah) -> {
                "ID-JT"
            }
            rp.getString(R.string.city_kalimantan_tengah) -> {
                "ID-KT"
            }
            rp.getString(R.string.city_sulawesi_tengah) -> {
                "ID-ST"
            }
            rp.getString(R.string.city_jawa_timur) -> {
                "ID-JI"
            }
            rp.getString(R.string.city_kalimantan_timur) -> {
                "ID-KI"
            }
            rp.getString(R.string.city_nusa_tenggara_timur) -> {
                "ID-NT"
            }
            rp.getString(R.string.city_gorontalo) -> {
                "ID-GO"
            }
            rp.getString(R.string.city_dki_jakarta) -> {
                "ID-JK"
            }
            rp.getString(R.string.city_jambi) -> {
                "ID-JA"
            }
            rp.getString(R.string.city_lampung) -> {
                "ID-LA"
            }
            rp.getString(R.string.city_maluku) -> {
                "ID-MA"
            }
            rp.getString(R.string.city_kalimantan_utara) -> {
                "ID-KU"
            }
            rp.getString(R.string.city_maluku_utara) -> {
                "ID-MU"
            }
            rp.getString(R.string.city_sulawesi_utara) -> {
                "ID-SA"
            }
            rp.getString(R.string.city_sumatera_utara) -> {
                "ID-SU"
            }
            rp.getString(R.string.city_papua) -> {
                "ID-PA"
            }
            rp.getString(R.string.city_riau) -> {
                "ID-RI"
            }
            rp.getString(R.string.city_kepulauan_riau) -> {
                "ID-KR"
            }
            rp.getString(R.string.city_sulawesi_tenggara) -> {
                "ID-SG"
            }
            rp.getString(R.string.city_kalimantan_selatan) -> {
                "ID-KS"
            }
            rp.getString(R.string.city_sulawesi_selatan) -> {
                "ID-SN"
            }
            rp.getString(R.string.city_sumatera_selatan) -> {
                "ID-SS"
            }
            rp.getString(R.string.city_di_yogyakarta) -> {
                "ID-YO"
            }
            rp.getString(R.string.city_jawa_barat) -> {
                "ID-JB"
            }
            rp.getString(R.string.city_kalimantan_barat) -> {
                "ID-KB"
            }
            rp.getString(R.string.city_nusa_tenggara_barat) -> {
                "ID-NB"
            }
            rp.getString(R.string.city_papua_barat) -> {
                "ID-PB"
            }
            rp.getString(R.string.city_sulawesi_barat) -> {
                "ID-SR"
            }
            rp.getString(R.string.city_sumatera_barat) -> {
                "ID-SB"
            }
            else -> {
                ""
            }
        }
    }

    fun getRegionString(code: String): String {
        val location = when (code) {
            "ID-AC" -> {
                "Aceh"
            }
            "ID-BA" -> {
                rp.getString(R.string.city_bali)
            }
            "ID-BB" -> {
                rp.getString(R.string.city_bangka_belitung)
            }
            "ID-BT" -> {
                rp.getString(R.string.city_banten)
            }
            "ID-BE" -> {
                rp.getString(R.string.city_bengkulu)
            }
            "ID-JT" -> {
                rp.getString(R.string.city_jawa_tengah)
            }
            "ID-KT" -> {
                rp.getString(R.string.city_kalimantan_tengah)
            }
            "ID-ST" -> {
                rp.getString(R.string.city_sulawesi_tengah)
            }
            "ID-JI" -> {
                rp.getString(R.string.city_jawa_timur)
            }
            "ID-KI" -> {
                rp.getString(R.string.city_kalimantan_timur)
            }
            "ID-NT" -> {
                rp.getString(R.string.city_nusa_tenggara_timur)
            }
            "ID-GO" -> {
                rp.getString(R.string.city_gorontalo)
            }
            "ID-JK" -> {
                rp.getString(R.string.city_dki_jakarta)
            }
            "ID-JA" -> {
                rp.getString(R.string.city_jambi)
            }
            "ID-LA" -> {
                rp.getString(R.string.city_lampung)
            }
            "ID-MA" -> {
                rp.getString(R.string.city_maluku)
            }
            "ID-KU" -> {
                rp.getString(R.string.city_kalimantan_utara)
            }
            "ID-MU" -> {
                rp.getString(R.string.city_maluku_utara)
            }
            "ID-SA" -> {
                rp.getString(R.string.city_sulawesi_utara)
            }
            "ID-SU" -> {
                rp.getString(R.string.city_sumatera_utara)
            }
            "ID-PA" -> {
                rp.getString(R.string.city_papua)
            }
            "ID-RI" -> {
                rp.getString(R.string.city_riau)
            }
            "ID-KR" -> {
                rp.getString(R.string.city_kepulauan_riau)
            }
            "ID-SG" -> {
                rp.getString(R.string.city_sulawesi_tenggara)
            }
            "ID-KS" -> {
                rp.getString(R.string.city_kalimantan_selatan)
            }
            "ID-SN" -> {
                rp.getString(R.string.city_sulawesi_selatan)
            }
            "ID-SS" -> {
                rp.getString(R.string.city_sumatera_selatan)
            }
            "ID-YO" -> {
                rp.getString(R.string.city_di_yogyakarta)
            }
            "ID-JB" -> {
                rp.getString(R.string.city_jawa_barat)
            }
            "ID-KB" -> {
                rp.getString(R.string.city_kalimantan_barat)
            }
            "ID-NB" -> {
                rp.getString(R.string.city_nusa_tenggara_barat)
            }
            "ID-PB" -> {
                rp.getString(R.string.city_papua_barat)
            }
            "ID-SR" -> {
                rp.getString(R.string.city_sulawesi_barat)
            }
            "ID-SB" -> {
                rp.getString(R.string.city_sumatera_barat)
            }
            else -> {
                ""
            }
        }
        return location
    }

    fun getDisasterType(disasterType: String?): String {
        return when (disasterType) {
            "flood" -> "Banjir"
            rp.getString(R.string.flood) -> rp.getString(R.string.type_flood)
            rp.getString(R.string.haze) -> rp.getString(R.string.type_haze)
            rp.getString(R.string.wind) -> rp.getString(R.string.type_wind)
            rp.getString(R.string.earthquake) -> rp.getString(R.string.type_earthquake)
            rp.getString(R.string.volcano) -> rp.getString(R.string.type_volcano)
            rp.getString(R.string.fire) -> rp.getString(R.string.type_fire)
            else -> rp.getString(R.string.unknown)
        }
    }

    fun getMarkerIcon(disasterType: String?): Int {
        return when (disasterType) {
            rp.getString(R.string.flood) -> R.drawable.ic_location_flood
            rp.getString(R.string.haze) -> R.drawable.ic_location_haze
            rp.getString(R.string.wind) -> R.drawable.ic_location_wind
            rp.getString(R.string.earthquake) -> R.drawable.ic_location_earthquake
            rp.getString(R.string.volcano) -> R.drawable.ic_location_volcano
            rp.getString(R.string.fire) -> R.drawable.ic_location_fire
            else -> R.drawable.ic_not_listed_location_24
        }
    }

    fun getDisasterDefaultImg(disasterType: String?): Int {

        return when(disasterType) {
            rp.getString(R.string.flood) -> R.drawable.img_flood
            rp.getString(R.string.haze) -> R.drawable.img_haze
            rp.getString(R.string.wind) -> R.drawable.img_wind
            rp.getString(R.string.earthquake) -> R.drawable.img_earthquake
            rp.getString(R.string.volcano) -> R.drawable.img_volcano
            rp.getString(R.string.fire) -> R.drawable.img_fire
            else -> R.drawable.ic_not_listed_location_24
        }

    }

    fun getWarningImage(warningText: String): Drawable {
        return when (warningText) {
            rp.getString(R.string.warning_no_data) -> rp.getDrawable(R.drawable.img_no_data)
            rp.getString(R.string.warning_not_found) -> rp.getDrawable(R.drawable.img_not_found)
            rp.getString(R.string.warning_exception) -> rp.getDrawable(R.drawable.img_exception)
            else -> rp.getDrawable(R.drawable.img_exception)
        }
    }

}