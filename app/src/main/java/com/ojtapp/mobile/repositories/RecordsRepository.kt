package com.ojtapp.mobile.repositories

import com.ojtapp.mobile.model.GiaRecord
import com.ojtapp.mobile.model.Record
import com.ojtapp.mobile.model.Resource
import com.ojtapp.mobile.model.SetupRecord
import com.ojtapp.mobile.model.Type
import kotlinx.coroutines.flow.Flow

interface RecordsRepository {
    val giaRecordsFlow: Flow<List<GiaRecord>>
    val setupRecordsFlow: Flow<List<SetupRecord>>

    fun getRecords(type: Type): Flow<Resource<List<Record>>>
    fun getGiaRecords(): Flow<Resource<List<Record>>>
    fun getSetupRecords(): Flow<Resource<List<Record>>>
}