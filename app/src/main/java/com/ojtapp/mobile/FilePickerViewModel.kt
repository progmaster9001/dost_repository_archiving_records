package com.ojtapp.mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

sealed interface FileClickEvent{
    data class Navigate(val path: String = ""): FileClickEvent
    data object OpenFile: FileClickEvent
}

class FilePickerViewModel(
    private val fileRepository: FileRepository
): ViewModel() {
//
//    private val currentPath = fileRepository.currentPath.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.Eagerly,
//        initialValue = ""
//    )
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    val files = currentPath.flatMapLatest {
//        fileRepository.fetchFiles(it)
//    }.map {
//        when(it){
//            is Resource.Error -> FilePickerState.Failed(it.message)
//            Resource.Loading -> FilePickerState.Loading
//            is Resource.Success -> FilePickerState.Success(it.data ?: emptyList())
//        }
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.Eagerly,
//        initialValue = FilePickerState.Loading
//    )
//
//    fun fileClickEvent(event: FileClickEvent){
//        when(event){
//            is FileClickEvent.Navigate -> {
//                fileRepository.updatePath(event.path)
//            }
//            FileClickEvent.OpenFile -> {}
//        }
//    }
}