package com.ojtapp.mobile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface RecordsRepository {
    val setupRecords: Flow<List<SetupRecord>>
    val giaRecords: Flow<List<GiaRecord>>

    fun getRecords(type: Type): Flow<Resource<List<Record>>>
    suspend fun updateRecord(type: Type)
}