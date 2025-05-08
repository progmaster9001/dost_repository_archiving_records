package com.ojtapp.mobile.repositories

import android.content.Context
import com.ojtapp.mobile.model.FileResponse
import com.ojtapp.mobile.model.Resource
import java.io.File

interface FileRepository {
    suspend fun fetchFiles(path: String = ""): Resource<FileResponse?>
    suspend fun downloadFile(context: Context, fileName: String, path: String): File?
}