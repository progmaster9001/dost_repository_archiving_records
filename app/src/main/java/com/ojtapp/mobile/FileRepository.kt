package com.ojtapp.mobile

interface FileRepository {
    suspend fun fetchFiles(path: String = ""): FileResponse?
}