package com.ojtapp.mobile.repositories

import com.ojtapp.mobile.model.FileResponse

interface FileRepository {
    suspend fun fetchFiles(path: String = ""): FileResponse?
}