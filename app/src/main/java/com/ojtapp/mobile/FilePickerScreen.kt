package com.ojtapp.mobile

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

sealed interface FilePickerState{
    data class Success(val file: FileResponse?): FilePickerState
    data class Error(val error: String): FilePickerState
    data object Loading: FilePickerState
}

suspend fun saveDownloadedFile(response: ResponseBody, fileName: String, context: Context): File? {
    return withContext(Dispatchers.IO) {
        try {
            val file = File(context.cacheDir, fileName)
            val inputStream = response.byteStream()
            val outputStream = FileOutputStream(file)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file // return the File object
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

@Composable
fun FilePickerRoute(
    modifier: Modifier = Modifier,
    path: String,
    fileClick: (String) -> Unit,
    back: () -> Unit
) {

    val state by produceState<FilePickerState>(initialValue = FilePickerState.Loading) {
        value = try {
            delay(1000)
            FilePickerState.Success(ServiceLocator.getFileRepository().fetchFiles(path))
        } catch (e: Exception) {
            FilePickerState.Error(e.message ?: "")
        }
    }

    FilePickerScreen(
        state = state,
        path = path,
        currentDirectory = path,
        fileClick = fileClick,
        back = back
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilePickerScreen(
    state: FilePickerState,
    path: String,
    currentDirectory: String,
    fileClick: (String) -> Unit,
    back: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Text(currentDirectory, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
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
            targetState = state,
            modifier = Modifier.padding(innerPadding).padding(horizontal = 8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                when(it){
                    is FilePickerState.Error -> Text(it.error)
                    FilePickerState.Loading -> CircularProgressIndicator()
                    is FilePickerState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(it.file?.files ?: emptyList()){ file ->
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

@Composable
fun FileItem(
    isDirectory: Boolean,
    path: String,
    icon: Int,
    fileName: String,
    fileClick: (String) -> Unit
) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            Log.d("FILE_LAUNCH", "Opened with result code: ${it.resultCode}")
        }
    )
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier.fillMaxWidth().padding(2.dp).clickable(
            onClick = {
                if(isDirectory) fileClick(fileName) else{
                    scope.launch {
                        val response = ServiceLocator.getApiService().downloadFile(path = "$path/$fileName")
                        val file = response.body()?.let { saveDownloadedFile(response = it, fileName = fileName, context) }
                        val uri = file?.let { FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", it)
                        }
                        val mimeType = when (file?.extension?.lowercase()) {
                            "pdf" -> "application/pdf"
                            "jpg", "jpeg" -> "image/jpeg"
                            "png" -> "image/png"
                            else -> "*/*"
                        }

                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, mimeType)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        launcher.launch(intent)
                    }
                }
            }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "file_icon"
        )
        Spacer(Modifier.width(Dimensions.basicSpacing))
        Text(fileName)
    }
}

@Composable
fun OpenWithLauncher(file: File) {
    val context = LocalContext.current
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            Log.d("FILE_LAUNCH", "Opened with result code: ${it.resultCode}")
        }
    )

    LaunchedEffect(file) {
        val mimeType = when (file.extension.lowercase()) {
            "pdf" -> "application/pdf"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            else -> "*/*"
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        launcher.launch(intent)
    }

    Text("Launching file viewer...")
}