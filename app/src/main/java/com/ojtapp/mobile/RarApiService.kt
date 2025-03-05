package com.ojtapp.mobile

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RarApiService {
    @POST("api/login.php")
    suspend fun login(@Body request: Map<String, String>): Response<AuthResponse>

    @GET("api/get_gia_records.php")
    suspend fun getGiaRecords(@Header("Authorization") token: String): Response<List<GiaRecord>>

    @GET("api/get_sectors.php")
    suspend fun getSetupRecords(@Header("Authorization") token: String): Response<List<SetupRecord>>
}