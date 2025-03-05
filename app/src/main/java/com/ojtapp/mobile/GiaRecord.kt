package com.ojtapp.mobile

data class GiaRecord(
    val id: Int,
    val projectTitle: String,
    val beneficiary: String,
    val location: String,
    val projectDuration: String,
    val projectCost: String,
    val remarks: String?,
    val className: String // from gia_class
): Record

enum class Class{

}

enum class Remark{

}