package com.ojtapp.mobile

data class FileResponse(
    val path: String,
    val files: List<FileEntry>
)

data class FileEntry(
    val name: String,
    val isDirectory: Boolean
)