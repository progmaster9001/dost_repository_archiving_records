package com.ojtapp.mobile.repositories.remote

import android.content.Context
import com.ojtapp.mobile.components.util.saveDownloadedFile
import com.ojtapp.mobile.data.ServiceLocator
import com.ojtapp.mobile.repositories.FileRepository
import com.ojtapp.mobile.model.FileResponse
import com.ojtapp.mobile.model.RarApiService
import com.ojtapp.mobile.model.Resource
import java.io.File

class FileRepositoryImpl(private val api: RarApiService) : FileRepository {
    override suspend fun fetchFiles(path: String): Resource<FileResponse?> {
        try {
            val response = api.getFiles(path)
            return if(response.isSuccessful){ Resource.Success(response.body()) }else { Resource.Error(response.message()) }
        }catch (e: Exception){
            return Resource.Error(e.message ?: "Unknown Error")
        }
    }

    override suspend fun downloadFile(context: Context, fileName: String, path: String): File? {
        val response = api.downloadFile(path = path)
        return response.body()?.let { saveDownloadedFile(response = it, fileName = fileName, context) }
    }
}