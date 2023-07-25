package com.frhanklin.disastory.data

import android.content.Context
import com.frhanklin.disastory.R

class Region {

    companion object {
        fun getRegionCode(c: Context, location: String): String {
            return when(location) {
                c.getString(R.string.city_aceh) -> {
                    "ID-AC"
                }
                c.getString(R.string.city_bali) -> {
                    "ID-BA"
                }
                c.getString(R.string.city_bangka_belitung) -> {
                    "ID-BB"
                }
                c.getString(R.string.city_banten) -> {
                    "ID-BT"
                }
                c.getString(R.string.city_bengkulu) -> {
                    "ID-BE"
                }
                c.getString(R.string.city_jawa_tengah) -> {
                    "ID-JT"
                }
                c.getString(R.string.city_kalimantan_tengah) -> {
                    "ID-KT"
                }
                c.getString(R.string.city_sulawesi_tengah) -> {
                    "ID-ST"
                }
                c.getString(R.string.city_jawa_timur) -> {
                    "ID-JI"
                }
                c.getString(R.string.city_kalimantan_timur) -> {
                    "ID-KI"
                }
                c.getString(R.string.city_nusa_tenggara_timur) -> {
                    "ID-NT"
                }
                c.getString(R.string.city_gorontalo) -> {
                    "ID-GO"
                }
                c.getString(R.string.city_dki_jakarta) -> {
                    "ID-JK"
                }
                c.getString(R.string.city_jambi) -> {
                    "ID-JA"
                }
                c.getString(R.string.city_lampung) -> {
                    "ID-LA"
                }
                c.getString(R.string.city_maluku) -> {
                    "ID-MA"
                }
                c.getString(R.string.city_kalimantan_utara) -> {
                    "ID-KU"
                }
                c.getString(R.string.city_maluku_utara) -> {
                    "ID-MU"
                }
                c.getString(R.string.city_sulawesi_utara) -> {
                    "ID-SA"
                }
                c.getString(R.string.city_sumatera_utara) -> {
                    "ID-SU"
                }
                c.getString(R.string.city_papua) -> {
                    "ID-PA"
                }
                c.getString(R.string.city_riau) -> {
                    "ID-RI"
                }
                c.getString(R.string.city_kepulauan_riau) -> {
                    "ID-KR"
                }
                c.getString(R.string.city_sulawesi_tenggara) -> {
                    "ID-SG"
                }
                c.getString(R.string.city_kalimantan_selatan) -> {
                    "ID-KS"
                }
                c.getString(R.string.city_sulawesi_selatan) -> {
                    "ID-SN"
                }
                c.getString(R.string.city_sumatera_selatan) -> {
                    "ID-SS"
                }
                c.getString(R.string.city_di_yogyakarta) -> {
                    "ID-YO"
                }
                c.getString(R.string.city_jawa_barat) -> {
                    "ID-JB"
                }
                c.getString(R.string.city_kalimantan_barat) -> {
                    "ID-KB"
                }
                c.getString(R.string.city_nusa_tenggara_barat) -> {
                    "ID-NB"
                }
                c.getString(R.string.city_papua_barat) -> {
                    "ID-PB"
                }
                c.getString(R.string.city_sulawesi_barat) -> {
                    "ID-SR"
                }
                c.getString(R.string.city_sumatera_barat) -> {
                    "ID-SB"
                }
                else -> {
                    ""
                }
            }
        }

        fun getRegionString(c: Context, code: String): String {
            val location = when (code) {
                "ID-AC" -> {
                    c.getString(R.string.city_aceh)
                }
                "ID-BA" -> {
                    c.getString(R.string.city_bali)
                }
                "ID-BB" -> {
                    c.getString(R.string.city_bangka_belitung)
                }
                "ID-BT" -> {
                    c.getString(R.string.city_banten)
                }
                "ID-BE" -> {
                    c.getString(R.string.city_bengkulu)
                }
                "ID-JT" -> {
                    c.getString(R.string.city_jawa_tengah)
                }
                "ID-KT" -> {
                    c.getString(R.string.city_kalimantan_tengah)
                }
                "ID-ST" -> {
                    c.getString(R.string.city_sulawesi_tengah)
                }
                "ID-JI" -> {
                    c.getString(R.string.city_jawa_timur)
                }
                "ID-KI" -> {
                    c.getString(R.string.city_kalimantan_timur)
                }
                "ID-NT" -> {
                    c.getString(R.string.city_nusa_tenggara_timur)
                }
                "ID-GO" -> {
                    c.getString(R.string.city_gorontalo)
                }
                "ID-JK" -> {
                    c.getString(R.string.city_dki_jakarta)
                }
                "ID-JA" -> {
                    c.getString(R.string.city_jambi)
                }
                "ID-LA" -> {
                    c.getString(R.string.city_lampung)
                }
                "ID-MA" -> {
                    c.getString(R.string.city_maluku)
                }
                "ID-KU" -> {
                    c.getString(R.string.city_kalimantan_utara)
                }
                "ID-MU" -> {
                    c.getString(R.string.city_maluku_utara)
                }
                "ID-SA" -> {
                    c.getString(R.string.city_sulawesi_utara)
                }
                "ID-SU" -> {
                    c.getString(R.string.city_sumatera_utara)
                }
                "ID-PA" -> {
                    c.getString(R.string.city_papua)
                }
                "ID-RI" -> {
                    c.getString(R.string.city_riau)
                }
                "ID-KR" -> {
                    c.getString(R.string.city_kepulauan_riau)
                }
                "ID-SG" -> {
                    c.getString(R.string.city_sulawesi_tenggara)
                }
                "ID-KS" -> {
                    c.getString(R.string.city_kalimantan_selatan)
                }
                "ID-SN" -> {
                    c.getString(R.string.city_sulawesi_selatan)
                }
                "ID-SS" -> {
                    c.getString(R.string.city_sumatera_selatan)
                }
                "ID-YO" -> {
                    c.getString(R.string.city_di_yogyakarta)
                }
                "ID-JB" -> {
                    c.getString(R.string.city_jawa_barat)
                }
                "ID-KB" -> {
                    c.getString(R.string.city_kalimantan_barat)
                }
                "ID-NB" -> {
                    c.getString(R.string.city_nusa_tenggara_barat)
                }
                "ID-PB" -> {
                    c.getString(R.string.city_papua_barat)
                }
                "ID-SR" -> {
                    c.getString(R.string.city_sulawesi_barat)
                }
                "ID-SB" -> {
                    c.getString(R.string.city_sumatera_barat)
                }
                else -> {
                    ""
                }
            }
            return location
        }
    }

}