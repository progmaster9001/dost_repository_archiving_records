package com.ojtapp.mobile

interface FilePickerRepository {
    suspend fun getFiles(): List<File>
}