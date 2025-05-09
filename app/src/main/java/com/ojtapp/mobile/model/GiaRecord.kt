package com.ojtapp.mobile.model

import java.util.UUID
import kotlin.reflect.full.primaryConstructor

data class GiaRecord(
    val id: Int,
    val projectTitle: String,
    val beneficiary: String,
    val location: String,
    val projectDuration: String,
    val projectCost: Double,
    val remarks: String? = null,
    val className: String = "",
    val fileLocation: String
) : Record

val giaFieldNames = GiaRecord::class.primaryConstructor
    ?.parameters
    ?.mapNotNull { it.name }
    ?.filterNot { it.contains("fileLocation") }