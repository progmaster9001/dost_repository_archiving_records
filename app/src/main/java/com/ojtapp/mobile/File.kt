package com.ojtapp.mobile

data class File(
    val icon: Int,
    val name: String = "",
    val files: List<File>? = null
)