package com.ojtapp.mobile

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming

interface RarApiService {

    @POST("api/login")
    suspend fun login(@Body request: Map<String, String>): Response<AuthResponse>

    @GET("api/files")
    suspend fun getFiles(@Query("path") path: String = ""): Response<FileResponse>

    @GET("api/file")
    @Streaming
    suspend fun downloadFile(@Query("path") path: String): Response<ResponseBody>

    @GET("api/gia_records")
    suspend fun getGiaRecords(
        @Header("Authorization") token: String
    ): Response<List<GiaRecord>>

    @GET("api/setup_records")
    suspend fun getSetupRecords(
        @Header("Authorization") token: String
    ): Response<List<SetupRecord>>
}