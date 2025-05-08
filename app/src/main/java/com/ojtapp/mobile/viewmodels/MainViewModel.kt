package com.ojtapp.mobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ojtapp.mobile.components.util.FilterCriteria
import com.ojtapp.mobile.components.util.GiaRecordFilterCriteria
import com.ojtapp.mobile.model.Record
import com.ojtapp.mobile.components.RecordNavigationEvent
import com.ojtapp.mobile.repositories.RecordsRepository
import com.ojtapp.mobile.model.Resource
import com.ojtapp.mobile.components.util.SetupRecordFilterCriteria
import com.ojtapp.mobile.model.Type
import com.ojtapp.mobile.model.User
import com.ojtapp.mobile.repositories.UserRepository
import com.ojtapp.mobile.components.util.filterRecords
import com.ojtapp.mobile.data.ServiceLocator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

sealed interface RecordState{
    data class Success(val records: List<Record>): RecordState
    data object Loading: RecordState
    data class Error(val error: String): RecordState
}

enum class DialogState{
    OPENED,
    CLOSED
}

enum class Layout{
    CARD,
    TABLE
}

sealed interface FilterEvent{
    data object ResetFilter: FilterEvent
    data class ApplyFilter(val criteria: FilterCriteria): FilterEvent
}

sealed interface DialogEvent{
    data object CloseDialog: DialogEvent
    data object OpenDialog: DialogEvent
}

class MainViewModel(
    serviceLocator: ServiceLocator = ServiceLocator
): ViewModel(){

    private val userRepository = serviceLocator.currentRepositoryProvider.value.userRepository
    private val recordsRepository = ServiceLocator.currentRepositoryProvider.value.recordsRepository

    val user = userRepository.user.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = User()
    )

    private val _currentTab = MutableStateFlow(Type.GIA)
    val currentTab = _currentTab.asStateFlow()

    private val _currentLayout = MutableStateFlow(Layout.TABLE)
    val currentLayout = _currentLayout.asStateFlow()

    private val _dialogState = MutableStateFlow(DialogState.CLOSED)
    val dialogState = _dialogState.asStateFlow()

    private val _giaRecordFilter = MutableStateFlow(GiaRecordFilterCriteria())
    val giaRecordFilter = _giaRecordFilter.asStateFlow()

    private val _setupRecordFilter = MutableStateFlow(SetupRecordFilterCriteria())
    val setupRecordFilter = _setupRecordFilter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val records: StateFlow<RecordState> = _currentTab
        .flatMapLatest { tab ->
            when(tab){
                Type.GIA -> recordsRepository.getGiaRecords()
                Type.SETUP -> recordsRepository.getSetupRecords()
            }.map { resource ->
                when (resource) {
                    is Resource.Error -> RecordState.Error(resource.message)
                    Resource.Loading -> RecordState.Loading
                    is Resource.Success -> RecordState.Success(resource.data)
                }
            }.catch { e -> emit(RecordState.Error(e.message ?: "Unknown Error")) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = RecordState.Loading
        )

    val filteredRecords = combine(records, _setupRecordFilter, _giaRecordFilter){ state, setup, gia ->
        filterRecords(state, gia, setup)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = RecordState.Loading
    )

    fun setCurrentLayout(){
        _currentLayout.update { if(_currentLayout.value == Layout.CARD) Layout.TABLE else Layout.CARD }
    }

    fun setCurrentTab(tab: Type){
        _currentTab.update { tab }
    }

    fun toggleDialog(event: DialogEvent){
        when(event){
            DialogEvent.CloseDialog -> _dialogState.update { DialogState.CLOSED }
            DialogEvent.OpenDialog -> _dialogState.update { DialogState.OPENED }
        }
    }

    fun clearUser(){
        userRepository.clearUser()
    }

    fun filterEvent(event: FilterEvent){
        when(event){
            is FilterEvent.ApplyFilter -> if(_currentTab.value == Type.GIA) _giaRecordFilter.update { event.criteria as GiaRecordFilterCriteria } else _setupRecordFilter.update { event.criteria as SetupRecordFilterCriteria }
            FilterEvent.ResetFilter -> if(_currentTab.value == Type.GIA) _giaRecordFilter.update { GiaRecordFilterCriteria() } else _setupRecordFilter.update { SetupRecordFilterCriteria() }
        }
    }
}