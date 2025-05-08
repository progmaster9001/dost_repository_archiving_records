package com.ojtapp.mobile.components

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.ojtapp.mobile.components.util.saveDownloadedFile
import com.ojtapp.mobile.data.ServiceLocator
import kotlinx.coroutines.launch

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
        onResult = { Log.d("FILE_LAUNCH", "Opened with result code: ${it.resultCode}") }
    )
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.clickable(
            onClick = {
                val fullPath = "$path/$fileName"
                if(isDirectory) fileClick(fullPath) else{
                    scope.launch {
                        val file = ServiceLocator.currentRepositoryProvider.value.fileRepository.downloadFile(context, fileName, fullPath)
                        if(file != null){
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
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
                        }else{
                            Log.d("Download Result", "File does not exist.")
                        }
                    }
                }
            }
        )
    ) {
        Row(
            modifier = Modifier.height(48.dp).fillMaxWidth().padding(vertical = 12.dp, horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "file_icon"
            )
            Spacer(Modifier.width(12.dp))
            Text(fileName)
        }
    }
}