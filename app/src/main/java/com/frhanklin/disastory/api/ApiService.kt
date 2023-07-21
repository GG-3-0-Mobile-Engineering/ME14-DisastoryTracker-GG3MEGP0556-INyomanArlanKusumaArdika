package com.frhanklin.disastory.api

import com.frhanklin.disastory.data.response.PetaBencanaReports
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("reports")
    fun getReports(): Call<PetaBencanaReports>

}