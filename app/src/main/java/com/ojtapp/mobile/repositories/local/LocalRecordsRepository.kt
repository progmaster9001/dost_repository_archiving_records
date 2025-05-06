package com.ojtapp.mobile.repositories.local

import com.ojtapp.mobile.repositories.RecordsRepository
import com.ojtapp.mobile.model.GiaRecord
import com.ojtapp.mobile.model.Record
import com.ojtapp.mobile.model.Resource
import com.ojtapp.mobile.model.SetupRecord
import com.ojtapp.mobile.model.Type
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class LocalRecordsRepository: RecordsRepository {
    private val _setupRecords = MutableStateFlow(Dummy.setupRecordsList)
    private val _giaRecords = MutableStateFlow(Dummy.giaRecordsList)

    override val giaRecordsFlow: StateFlow<List<GiaRecord>> = _giaRecords.asStateFlow()
    override val setupRecordsFlow: StateFlow<List<SetupRecord>> = _setupRecords.asStateFlow()

    override fun getGiaRecords(): Flow<Resource<List<Record>>> = giaRecordsFlow.map { records -> Resource.Success(records) as Resource<List<Record>> }
        .onStart {
            emit(Resource.Loading)
            delay(1000)
        }

    override fun getSetupRecords(): Flow<Resource<List<Record>>> = setupRecordsFlow.map { records -> Resource.Success(records) as Resource<List<Record>> }
        .onStart {
            emit(Resource.Loading)
            delay(1000)
        }

    override fun getRecords(type: Type): Flow<Resource<List<Record>>> =
        (if (type == Type.GIA) giaRecordsFlow else setupRecordsFlow)
            .map { records -> Resource.Success(records) as Resource<List<Record>> }
            .onStart {
                emit(Resource.Loading)
                delay(1000)
            }
}