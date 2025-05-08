package com.ojtapp.mobile.repositories.local

import android.content.Context
import android.util.Log
import com.ojtapp.mobile.repositories.FileRepository
import com.ojtapp.mobile.model.FileEntry
import com.ojtapp.mobile.model.FileResponse
import com.ojtapp.mobile.model.Resource
import java.io.File

class LocalFileRepository: FileRepository {
    override suspend fun fetchFiles(path: String): Resource<FileResponse?> {
        val response = Dummy.fileResponsesMap[path]
        return if(response != null){
            Resource.Success(response)
        }else{
            Resource.Error("Empty Directory.")
        }
    }

    override suspend fun downloadFile(context: Context, fileName: String, path: String): File? {
        return null
    }
}