package com.ojtapp.mobile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface RecordsRepository {
    val giaRecordsFlow: Flow<List<GiaRecord>>
    val setupRecordsFlow: Flow<List<SetupRecord>>

    fun getRecords(type: Type): Flow<Resource<List<Record>>>
    fun getGiaRecords(): Flow<Resource<List<Record>>>
    fun getSetupRecords(): Flow<Resource<List<Record>>>
}