package com.ojtapp.mobile.repositories.local

import com.ojtapp.mobile.repositories.FileRepository
import com.ojtapp.mobile.model.FileEntry
import com.ojtapp.mobile.model.FileResponse

class LocalFileRepository: FileRepository {
    override suspend fun fetchFiles(path: String): FileResponse? {
        return Dummy.fileResponses.find { it.path == path }
    }
}