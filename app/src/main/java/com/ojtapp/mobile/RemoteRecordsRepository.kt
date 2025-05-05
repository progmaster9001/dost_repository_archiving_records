package com.ojtapp.mobile

import android.content.SharedPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class RemoteRecordsRepository(
    private val rarApiService: RarApiService,
    private val sharedPref: SharedPreferences
): RecordsRepository {

    override val setupRecordsFlow: MutableStateFlow<List<SetupRecord>>
        get() = MutableStateFlow(emptyList())
    override val giaRecordsFlow: MutableStateFlow<List<GiaRecord>>
        get() = MutableStateFlow(emptyList())

    override fun getRecords(type: Type): Flow<Resource<List<Record>>> = flow {
        emit(Resource.Loading)
        delay(1500)
        try {
            val token = sharedPref.getString("jwt_token", "") ?: ""
            if (token.isEmpty()) throw Exception("Unauthorized")

            val response = when (type) {
                Type.GIA -> rarApiService.getGiaRecords("Bearer $token")
                Type.SETUP -> rarApiService.getSetupRecords("Bearer $token")
            }

            if (response.isSuccessful) {
                val records = response.body()?.map { it as Record } ?: emptyList()
                emit(Resource.Success(records))

                when (type) {
                    Type.GIA -> giaRecordsFlow.value = records as List<GiaRecord>
                    Type.SETUP -> setupRecordsFlow.value = records as List<SetupRecord>
                }
            } else {
                emit(Resource.Error("Failed to fetch records: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    override fun getGiaRecords(): Flow<Resource<List<Record>>> = flow {
        emit(Resource.Loading)
        delay(1500)
        try {

            val token = sharedPref.getString("jwt_token", "") ?: ""
            if (token.isEmpty()) throw Exception("Unauthorized")

            val response = rarApiService.getGiaRecords("Bearer $token")

            if (response.isSuccessful) {
                val records = response.body()?.map { it as Record } ?: emptyList()
                emit(Resource.Success(records))

                giaRecordsFlow.value = records as List<GiaRecord>
            } else {
                emit(Resource.Error("Failed to fetch gia_records: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    override fun getSetupRecords(): Flow<Resource<List<Record>>> = flow {
        emit(Resource.Loading)
        delay(1500)
        try {
            val token = sharedPref.getString("jwt_token", "") ?: ""
            if (token.isEmpty()) throw Exception("Unauthorized")

            val response = rarApiService.getSetupRecords("Bearer $token")

            if (response.isSuccessful) {
                val records = response.body()?.map { it as Record } ?: emptyList()
                emit(Resource.Success(records))

                setupRecordsFlow.value = records as List<SetupRecord>
            } else {
                emit(Resource.Error("Failed to fetch setup_records: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }
}