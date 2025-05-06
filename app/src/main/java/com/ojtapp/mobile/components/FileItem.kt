package com.ojtapp.mobile.components

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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