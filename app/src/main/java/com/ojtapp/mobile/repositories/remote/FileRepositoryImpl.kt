package com.ojtapp.mobile.repositories.remote

import com.ojtapp.mobile.repositories.FileRepository
import com.ojtapp.mobile.model.FileResponse
import com.ojtapp.mobile.model.RarApiService

class FileRepositoryImpl(private val api: RarApiService) : FileRepository {

    override suspend fun fetchFiles(path: String): FileResponse? {
        try {
            val response = api.getFiles(path)
            return if(response.isSuccessful){
                response.body()
            }else {
                null
            }
        }catch (e: Exception){
            return null
        }
    }

}