package com.ojtapp.mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

sealed interface RecordState{
    data class Success(val records: List<Record>): RecordState
    data object Loading: RecordState
    data class Error(val error: String): RecordState
}

class MainViewModel(
    userRepository: UserRepository,
    recordsRepository: RecordsRepository
): ViewModel(){

    val user = userRepository.user.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = User()
    )

    private val _currentTab = MutableStateFlow(Type.GIA)
    val currentTab = _currentTab.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val records: StateFlow<RecordState> = _currentTab
        .flatMapLatest { tab ->
            recordsRepository.getRecords(tab)
                .map { resource ->
                    when (resource) {
                        is Resource.Error -> RecordState.Error(resource.message)
                        Resource.Loading -> RecordState.Loading
                        is Resource.Success -> RecordState.Success(resource.data)
                    }
                }
                .catch { e -> emit(RecordState.Error(e.message ?: "Unknown Error")) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = RecordState.Loading
        )

    fun setCurrentTab(tab: Type){
        _currentTab.update { tab }
    }

}