package com.ojtapp.mobile

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class RemoteRecordsRepository(
    private val rarApiService: RarApiService,
    private val sharedPref: SharedPreferences
): RecordsRepository {

    override val setupRecords: MutableStateFlow<List<SetupRecord>>
        get() = MutableStateFlow(emptyList())
    override val giaRecords: MutableStateFlow<List<GiaRecord>>
        get() = MutableStateFlow(emptyList())

    override fun getRecords(type: Type): Flow<Resource<List<Record>>> = flow {
        emit(Resource.Loading)
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
                    Type.GIA -> giaRecords.value = records as List<GiaRecord>
                    Type.SETUP -> setupRecords.value = records as List<SetupRecord>
                }
            } else {
                emit(Resource.Error("Failed to fetch records: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    override suspend fun updateRecord(type: Type) {
        TODO("Not yet implemented")
    }
}