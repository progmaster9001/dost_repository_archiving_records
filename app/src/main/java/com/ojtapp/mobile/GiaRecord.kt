package com.ojtapp.mobile

import kotlin.reflect.full.primaryConstructor

data class GiaRecord(
    val id: Int,
    val projectTitle: String,
    val beneficiary: String,
    val location: String,
    val projectDuration: String,
    val projectCost: String,
    val remarks: String,
    val className: String
): Record

val giaFieldNames = GiaRecord::class.primaryConstructor?.parameters?.map { it.name }

enum class Class{

}

enum class Remark{

}