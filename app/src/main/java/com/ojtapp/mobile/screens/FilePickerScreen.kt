package com.ojtapp.mobile.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ojtapp.mobile.R
import com.ojtapp.mobile.components.FileItem
import com.ojtapp.mobile.components.RarLoadingProgressIndicator
import com.ojtapp.mobile.data.ServiceLocator
import com.ojtapp.mobile.model.FileResponse
import com.ojtapp.mobile.model.Resource
import kotlinx.coroutines.delay

sealed interface FilePickerState{
    data class Success(val response: FileResponse?): FilePickerState
    data class Error(val error: String): FilePickerState
    data object Loading: FilePickerState
}

@Composable
fun FilePickerRoute(
    modifier: Modifier = Modifier,
    currentPath: String,
    fileClick: (String) -> Unit,
    back: () -> Unit
) {

    val state by produceState<FilePickerState>(initialValue = FilePickerState.Loading) {
        value = try {
            delay(1000)
            val resource = ServiceLocator.currentRepositoryProvider.value.fileRepository.fetchFiles(currentPath)
            when(resource){
                is Resource.Error -> FilePickerState.Error(resource.message)
                Resource.Loading -> FilePickerState.Success(null)
                is Resource.Success -> FilePickerState.Success(resource.data)
            }
        } catch (e: Exception) {
            FilePickerState.Error(e.message ?: "Unknown Error.")
        }
    }

    FilePickerScreen(
        state = state,
        currentDirectory = currentPath,
        fileClick = fileClick,
        back = back
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilePickerScreen(
    state: FilePickerState,
    currentDirectory: String,
    fileClick: (String) -> Unit,
    back: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Row { Text(currentDirectory.ifEmpty { "File Server" }, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                },
                navigationIcon = {
                    IconButton(
                        onClick = back
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back_icon"
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->
        AnimatedContent(
            targetState = state
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                when(it){
                    is FilePickerState.Error -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(painter = painterResource(R.drawable.error_data), contentDescription = "error_data")
                            Spacer(Modifier.height(2.dp))
                            Text(it.error, fontSize = 16.sp)
                        }
                    }
                    FilePickerState.Loading -> RarLoadingProgressIndicator()
                    is FilePickerState.Success -> {
                        val files = it.response?.files ?: emptyList()
                        if(files.isEmpty()){
                            Image(painter = painterResource(R.drawable.no_data), contentDescription = "no_data")
                        }else{
                            LazyColumn(
                                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(bottom = 32.dp),
                            ) {
                                items(files) { file ->
                                    FileItem(
                                        isDirectory = file.isDirectory,
                                        path = currentDirectory,
                                        icon = if(file.isDirectory) R.drawable.folder_open else R.drawable.file,
                                        fileName = file.name,
                                        fileClick = fileClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}