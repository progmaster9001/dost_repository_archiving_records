package com.ojtapp.mobile

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