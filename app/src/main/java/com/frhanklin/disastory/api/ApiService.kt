package com.frhanklin.disastory.api

import com.frhanklin.disastory.data.response.PetaBencanaReports
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

//    @GET("reports")
//    fun getReports(): Call<PetaBencanaReports>

    @GET("reports")
    fun getReports(
        @Query("timeperiod") timePeriod: Int = 259200
    ): Call<PetaBencanaReports>

    @GET("reports")
    fun getReportsByLocationAndType(
        @Query("admin") admin: String,
        @Query("disaster") disaster: String,
        @Query("timeperiod") timePeriod: Int = 259200
    ): Call<PetaBencanaReports>

    @GET("reports")
    fun getReportsByLocation(
        @Query("admin") admin: String,
        @Query("timeperiod") timePeriod: Int = 259200
    ): Call<PetaBencanaReports>

    @GET("reports")
    fun getReportsByType(
        @Query("disaster") disaster: String,
        @Query("timeperiod") timePeriod: Int = 259200
    ): Call<PetaBencanaReports>

    @GET("floods/states")
    fun getFloodsStates(): Call<PetaBencanaReports>



}