package com.ojtapp.mobile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun FilePickerRoute(modifier: Modifier = Modifier) {
    FilePickerScreen(
        files = emptyList()
    )
}

@Composable
fun FilePickerScreen(
    files: List<File>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(files){
            FileItem(
                icon = if(it.files != null) R.drawable.file else R.drawable.folder_open,
                fileName = it.name
            ){}
        }
    }
}

@Composable
fun FileItem(
    icon: Int,
    fileName: String,
    modifier: Modifier = Modifier,
    onFileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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